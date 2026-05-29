import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { App as CapacitorApp } from '@capacitor/app';
import { Capacitor, PluginListenerHandle, registerPlugin } from '@capacitor/core';
import { BehaviorSubject } from 'rxjs';

interface ApkInstallerPlugin {
  installFromUrl(options: { url: string; fileName: string }): Promise<{ status: string }>;
  addListener(
    eventName: 'downloadProgress',
    listenerFunc: (event: DownloadProgressEvent) => void
  ): Promise<PluginListenerHandle>;
}

const ApkInstaller = registerPlugin<ApkInstallerPlugin>('ApkInstaller');

export interface GitHubReleaseAsset {
  name: string;
  browser_download_url: string;
}

export interface GitHubRelease {
  name: string | null;
  tag_name: string;
  html_url: string;
  published_at: string;
  body: string | null;
  assets: GitHubReleaseAsset[];
  prerelease: boolean;
  draft: boolean;
}

export interface DownloadProgressEvent {
  bytesRead: number;
  totalBytes: number;
  progress: number;
}

export interface UpdateCheckResult {
  currentVersion: string;
  latestVersion: string;
  updateAvailable: boolean;
  release: GitHubRelease | null;
  apkUrl: string | null;
  message: string;
}

interface UpdateManifestRelease {
  version: string;
  title: string;
  publishedAt: string;
  body: string;
  apkUrl: string;
  htmlUrl: string;
}

interface UpdateManifest {
  latest: UpdateManifestRelease;
  releases: UpdateManifestRelease[];
}

@Injectable({
  providedIn: 'root',
})
export class UpdateCheckService {
  private static readonly latestReleaseUrl =
    'https://api.github.com/repos/NiavlySDev/ParkingLogicKit/releases/latest';
  private static readonly manifestUrl =
    'https://raw.githubusercontent.com/NiavlySDev/ParkingLogicKit/main/docs/update-manifest.json';
  private static readonly manifestCacheKey = 'plk-update-manifest-cache';
  private static readonly manifestCacheTtlMs = 10 * 60 * 1000;

  private hasChecked = false;
  private manifestPromise: Promise<UpdateManifest | null> | null = null;
  private readonly lastResultSubject = new BehaviorSubject<UpdateCheckResult | null>(null);
  readonly lastResult$ = this.lastResultSubject.asObservable();

  constructor(
    private router: Router
  ) {}

  async checkOnStartup(): Promise<void> {
    if (this.hasChecked || !Capacitor.isNativePlatform()) {
      return;
    }

    this.hasChecked = true;

    try {
      const result = await this.checkForUpdate();
      if (result.updateAvailable) {
        window.alert(
          `Nouvelle mise a jour Disponible, (${result.currentVersion} -> ${result.latestVersion}), Cliquez pour y acceder`
        );
        await this.router.navigate(['/settings']);
      }
    } catch (error) {
      console.warn('Verification de mise a jour impossible', error);
    }
  }

  async getCurrentVersion(): Promise<string> {
    if (!Capacitor.isNativePlatform()) {
      return 'Version web';
    }

    const appInfo = await CapacitorApp.getInfo();
    return appInfo.version;
  }

  async getLatestRelease(): Promise<GitHubRelease> {
    const manifest = await this.getUpdateManifest();
    if (manifest?.latest) {
      return this.releaseFromManifest(manifest.latest);
    }

    return this.fetchGitHub<GitHubRelease>(UpdateCheckService.latestReleaseUrl);
  }

  async getRecentReleases(limit: number = 5): Promise<GitHubRelease[]> {
    const manifest = await this.getUpdateManifest();
    if (manifest?.releases?.length) {
      return manifest.releases.slice(0, limit).map((release) => this.releaseFromManifest(release));
    }

    const releases = await this.fetchGitHub<GitHubRelease[]>(
      `https://api.github.com/repos/NiavlySDev/ParkingLogicKit/releases?per_page=${limit}`
    );

    return releases.filter((release) => !release.draft && !release.prerelease);
  }

  async getReleaseForVersion(version: string): Promise<GitHubRelease | null> {
    if (!version || version === 'Version web') {
      return null;
    }

    const normalizedVersion = version.replace(/^v/i, '');
    const manifest = await this.getUpdateManifest();
    const manifestRelease = manifest?.releases?.find(
      (release) => release.version.replace(/^v/i, '') === normalizedVersion
    );
    if (manifestRelease) {
      return this.releaseFromManifest(manifestRelease);
    }

    const tagsToTry = [`v${normalizedVersion}`, normalizedVersion];

    for (const tag of tagsToTry) {
      try {
        return await this.fetchGitHub<GitHubRelease>(
          `https://api.github.com/repos/NiavlySDev/ParkingLogicKit/releases/tags/${tag}`
        );
      } catch {
        // Le depot utilise normalement les tags vX.Y.Z, mais on tente aussi X.Y.Z.
      }
    }

    return null;
  }

  async checkForUpdate(): Promise<UpdateCheckResult> {
    const [currentVersion, release] = await Promise.all([
      this.getCurrentVersion(),
      this.getLatestRelease(),
    ]);

    if (release.draft || release.prerelease) {
      return this.storeResult({
        currentVersion,
        latestVersion: release.tag_name,
        updateAvailable: false,
        release: null,
        apkUrl: null,
        message: 'La derniere release est une preversion ou un brouillon.',
      });
    }

    const apkUrl = this.findApkUrl(release);
    const updateAvailable = this.isNewerVersion(
      this.parseVersion(release.tag_name),
      this.parseVersion(currentVersion)
    );

    return this.storeResult({
      currentVersion,
      latestVersion: release.tag_name,
      updateAvailable,
      release,
      apkUrl,
      message: updateAvailable
        ? `Mise a jour disponible : ${currentVersion} -> ${release.tag_name}.`
        : `Application deja a jour (${currentVersion}).`,
    });
  }

  async installRelease(
    release: GitHubRelease,
    onProgress?: (event: DownloadProgressEvent) => void
  ): Promise<void> {
    if (!Capacitor.isNativePlatform()) {
      throw new Error("L'installation automatique est disponible uniquement sur Android.");
    }

    const apkUrl = this.findApkUrl(release);
    if (!apkUrl) {
      throw new Error('Aucun fichier APK trouve dans les ressources de la release.');
    }

    const listener = onProgress
      ? await ApkInstaller.addListener('downloadProgress', onProgress)
      : null;

    try {
      await ApkInstaller.installFromUrl({
        url: apkUrl,
        fileName: `ParkingLogicKit-${release.tag_name}.apk`,
      });
    } finally {
      await listener?.remove();
    }
  }

  private findApkUrl(release: GitHubRelease): string | null {
    return (
      release.assets.find((asset) => asset.name.toLowerCase().endsWith('.apk'))
        ?.browser_download_url ?? null
    );
  }

  private storeResult(result: UpdateCheckResult): UpdateCheckResult {
    this.lastResultSubject.next(result);
    return result;
  }

  private async getUpdateManifest(): Promise<UpdateManifest | null> {
    if (!this.manifestPromise) {
      this.manifestPromise = this.loadUpdateManifest();
    }

    return this.manifestPromise;
  }

  private async loadUpdateManifest(): Promise<UpdateManifest | null> {
    const cached = this.readCachedManifest();
    if (cached) {
      return cached;
    }

    try {
      const manifest = await this.fetchPublicJson<UpdateManifest>(UpdateCheckService.manifestUrl);
      localStorage.setItem(
        UpdateCheckService.manifestCacheKey,
        JSON.stringify({ savedAt: Date.now(), manifest })
      );
      return manifest;
    } catch (error) {
      console.warn('Manifest de mise a jour indisponible', error);
      return null;
    }
  }

  private readCachedManifest(): UpdateManifest | null {
    try {
      const rawCache = localStorage.getItem(UpdateCheckService.manifestCacheKey);
      if (!rawCache) {
        return null;
      }

      const cache = JSON.parse(rawCache) as { savedAt?: number; manifest?: UpdateManifest };
      if (
        !cache.savedAt ||
        !cache.manifest ||
        Date.now() - cache.savedAt > UpdateCheckService.manifestCacheTtlMs
      ) {
        return null;
      }

      return cache.manifest;
    } catch {
      return null;
    }
  }

  private releaseFromManifest(release: UpdateManifestRelease): GitHubRelease {
    return {
      name: release.title,
      tag_name: release.version,
      html_url: release.htmlUrl,
      published_at: release.publishedAt,
      body: release.body,
      draft: false,
      prerelease: false,
      assets: [
        {
          name: 'plk.apk',
          browser_download_url: release.apkUrl,
        },
      ],
    };
  }

  private async fetchPublicJson<T>(url: string): Promise<T> {
    const response = await fetch(url, {
      headers: { Accept: 'application/json' },
      cache: 'no-store',
    });

    if (!response.ok) {
      throw new Error(`${response.status} ${response.statusText}`);
    }

    return (await response.json()) as T;
  }

  private async fetchGitHub<T>(url: string): Promise<T> {
    const response = await fetch(url, {
      headers: {
        Accept: 'application/vnd.github+json',
        'X-GitHub-Api-Version': '2022-11-28',
      },
      cache: 'no-store',
    });

    if (!response.ok) {
      let message = response.statusText;
      try {
        const payload = await response.json();
        message = payload?.message || message;
      } catch {
        // On garde le statusText si GitHub ne renvoie pas de JSON lisible.
      }
      throw new Error(`GitHub ${response.status} : ${message}`);
    }

    return (await response.json()) as T;
  }

  private parseVersion(version: string): number[] {
    return version
      .replace(/^v/i, '')
      .split('.')
      .map((part) => Number.parseInt(part, 10))
      .map((part) => (Number.isNaN(part) ? 0 : part));
  }

  private isNewerVersion(candidate: number[], current: number[]): boolean {
    const length = Math.max(candidate.length, current.length);

    for (let index = 0; index < length; index += 1) {
      const candidatePart = candidate[index] ?? 0;
      const currentPart = current[index] ?? 0;

      if (candidatePart > currentPart) {
        return true;
      }

      if (candidatePart < currentPart) {
        return false;
      }
    }

    return false;
  }
}
