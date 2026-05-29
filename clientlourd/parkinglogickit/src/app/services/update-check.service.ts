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

@Injectable({
  providedIn: 'root',
})
export class UpdateCheckService {
  private static readonly latestReleaseUrl =
    'https://api.github.com/repos/NiavlySDev/ParkingLogicKit/releases/latest';

  private hasChecked = false;
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
    return this.fetchGitHub<GitHubRelease>(UpdateCheckService.latestReleaseUrl);
  }

  async getRecentReleases(limit: number = 5): Promise<GitHubRelease[]> {
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
