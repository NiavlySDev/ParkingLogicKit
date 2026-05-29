import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { App as CapacitorApp } from '@capacitor/app';
import { Capacitor, registerPlugin } from '@capacitor/core';
import { BehaviorSubject, firstValueFrom } from 'rxjs';

interface ApkInstallerPlugin {
  installFromUrl(options: { url: string; fileName: string }): Promise<{ status: string }>;
}

const ApkInstaller = registerPlugin<ApkInstallerPlugin>('ApkInstaller');

export interface GitHubReleaseAsset {
  name: string;
  browser_download_url: string;
}

export interface GitHubRelease {
  tag_name: string;
  html_url: string;
  assets: GitHubReleaseAsset[];
  prerelease: boolean;
  draft: boolean;
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

  constructor(private http: HttpClient) {}

  async checkOnStartup(): Promise<void> {
    if (this.hasChecked || !Capacitor.isNativePlatform()) {
      return;
    }

    this.hasChecked = true;

    try {
      const result = await this.checkForUpdate();
      if (result.updateAvailable) {
        window.alert(`${result.message}\nVa dans Parametres > Mises a jour pour l installer.`);
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

  async checkForUpdate(): Promise<UpdateCheckResult> {
    const [currentVersion, release] = await Promise.all([
      this.getCurrentVersion(),
      firstValueFrom(this.http.get<GitHubRelease>(UpdateCheckService.latestReleaseUrl)),
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

  async installRelease(release: GitHubRelease): Promise<void> {
    if (!Capacitor.isNativePlatform()) {
      throw new Error("L'installation automatique est disponible uniquement sur Android.");
    }

    const apkUrl = this.findApkUrl(release);
    if (!apkUrl) {
      throw new Error('Aucun fichier APK trouve dans les ressources de la release.');
    }

    await ApkInstaller.installFromUrl({
      url: apkUrl,
      fileName: `ParkingLogicKit-${release.tag_name}.apk`,
    });
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
