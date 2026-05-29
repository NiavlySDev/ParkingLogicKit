import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { App as CapacitorApp } from '@capacitor/app';
import { Browser } from '@capacitor/browser';
import { Capacitor } from '@capacitor/core';
import { firstValueFrom } from 'rxjs';

interface GitHubReleaseAsset {
  name: string;
  browser_download_url: string;
}

interface GitHubRelease {
  tag_name: string;
  html_url: string;
  assets: GitHubReleaseAsset[];
  prerelease: boolean;
  draft: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class UpdateCheckService {
  private static readonly latestReleaseUrl =
    'https://api.github.com/repos/NiavlySDev/ParkingLogicKit/releases/latest';

  private hasChecked = false;

  constructor(private http: HttpClient) {}

  async checkOnStartup(): Promise<void> {
    if (this.hasChecked || !Capacitor.isNativePlatform()) {
      return;
    }

    this.hasChecked = true;

    try {
      const [appInfo, release] = await Promise.all([
        CapacitorApp.getInfo(),
        firstValueFrom(this.http.get<GitHubRelease>(UpdateCheckService.latestReleaseUrl)),
      ]);

      if (release.draft || release.prerelease) {
        return;
      }

      const installedVersion = this.parseVersion(appInfo.version);
      const latestVersion = this.parseVersion(release.tag_name);

      if (!this.isNewerVersion(latestVersion, installedVersion)) {
        return;
      }

      const apkAsset = release.assets.find((asset) => asset.name.toLowerCase().endsWith('.apk'));
      const downloadUrl = apkAsset?.browser_download_url ?? release.html_url;
      const wantsUpdate = window.confirm(
        `Une nouvelle version de ParkingLogicKit est disponible (${release.tag_name}). Voulez-vous la telecharger ?`
      );

      if (wantsUpdate) {
        await Browser.open({ url: downloadUrl });
      }
    } catch (error) {
      console.warn('Verification de mise a jour impossible', error);
    }
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
