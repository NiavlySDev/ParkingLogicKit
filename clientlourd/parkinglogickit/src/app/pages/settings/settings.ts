//
import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from '../../../Auth/auth.service';
import {
  GitHubRelease,
  UpdateCheckResult,
  UpdateCheckService,
} from '../../services/update-check.service';

type SettingsTab = 'updates' | 'information';
type StepState = 'pending' | 'active' | 'done' | 'error';

interface UpdateStep {
  label: string;
  state: StepState;
}

interface ChangelogEntry {
  version: string;
  title: string;
  date: string;
  description: string;
}

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './settings.html',
  styleUrl: './settings.css',
})
export class Settings implements OnInit, OnDestroy {
  username = '';
  role = 'Driver';
  menuOpen = false;
  activeTab: SettingsTab = 'updates';

  currentVersion = 'Chargement...';
  latestVersion = '-';
  statusMessage = '';
  isChecking = false;
  isInstalling = false;
  showSteps = false;
  downloadProgress = 0;
  currentReleaseDate = 'Chargement...';
  latestReleaseDate = 'Chargement...';
  changelog: ChangelogEntry[] = [];
  isLoadingChangelog = false;
  release: GitHubRelease | null = null;
  updateAvailable = false;
  private updateSubscription: Subscription | null = null;

  steps: UpdateStep[] = [
    { label: 'Lecture de la version actuelle', state: 'pending' },
    { label: 'Recherche de la derniere release GitHub', state: 'pending' },
    { label: 'Telechargement de l APK', state: 'pending' },
    { label: 'Ouverture de l installation Android', state: 'pending' },
  ];

  readonly programmers = [
    { name: 'Sylvain Crocquevieille', role: 'Serveur Web Wildfly' },
    { name: 'Ethan Chandebois', role: 'Angular Android Mobile' },
    { name: 'Phily Seck', role: 'Couche Metier, Relation BDD' },
    { name: 'Virgile Alari', role: 'Couche Physique, Connexion BDD' },
  ];

  constructor(
    private authService: AuthService,
    private router: Router,
    private updateCheckService: UpdateCheckService,
    private cdr: ChangeDetectorRef
  ) {}

  async ngOnInit(): Promise<void> {
    const isLoggedIn = await this.authService.isLoggedIn();
    this.username = isLoggedIn ? await this.authService.getUsername() : 'Invite';
    this.role = isLoggedIn ? (await this.authService.getRole()) || 'Driver' : 'Driver';

    this.currentVersion = await this.updateCheckService.getCurrentVersion();
    this.steps[0].state = 'done';
    await Promise.all([this.loadReleaseDates(), this.loadChangelog()]);
    this.updateSubscription = this.updateCheckService.lastResult$.subscribe((result) => {
      if (result) {
        this.applyUpdateResult(result);
        this.cdr.detectChanges();
      }
    });
    this.cdr.detectChanges();
  }

  ngOnDestroy(): void {
    this.updateSubscription?.unsubscribe();
  }

  async checkForUpdate(): Promise<void> {
    this.isChecking = true;
    this.statusMessage = '';
    this.release = null;
    this.updateAvailable = false;
    this.latestVersion = '-';
    this.downloadProgress = 0;
    this.showSteps = true;
    this.resetSteps();
    this.steps[0].state = 'active';
    this.cdr.detectChanges();

    try {
      this.currentVersion = await this.updateCheckService.getCurrentVersion();
      this.steps[0].state = 'done';
      this.steps[1].state = 'active';
      this.cdr.detectChanges();

      const result: UpdateCheckResult = await this.updateCheckService.checkForUpdate();
      this.steps[1].state = 'done';
      this.applyUpdateResult(result);
    } catch (error) {
      this.steps[1].state = 'error';
      this.statusMessage = this.formatError(error);
    } finally {
      this.isChecking = false;
      this.cdr.detectChanges();
    }
  }

  async installUpdate(): Promise<void> {
    if (!this.release) {
      return;
    }

    this.isInstalling = true;
    this.showSteps = true;
    this.downloadProgress = 0;
    this.steps[2].state = 'active';
    this.statusMessage = 'Telechargement de la mise a jour...';
    this.cdr.detectChanges();

    try {
      await this.updateCheckService.installRelease(this.release, (progress) => {
        this.downloadProgress = progress.progress;
        this.statusMessage =
          progress.totalBytes > 0
            ? `Telechargement de l APK... ${progress.progress}%`
            : `Telechargement de l APK... ${this.formatBytes(progress.bytesRead)}`;
        this.cdr.detectChanges();
      });
      this.steps[2].state = 'done';
      this.steps[3].state = 'done';
      this.downloadProgress = 100;
      this.statusMessage = 'Installation Android ouverte. Valide la mise a jour pour terminer.';
    } catch (error) {
      this.steps[2].state = 'error';
      this.steps[3].state = 'error';
      this.statusMessage = this.formatError(error);
    } finally {
      this.isInstalling = false;
      this.cdr.detectChanges();
    }
  }

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  goProfile(): void {
    this.menuOpen = false;
    if (this.username === 'Invite') {
      this.router.navigate(['/sign-in']);
      return;
    }

    this.router.navigate(['/user-profile']);
  }

  goSettings(): void {
    this.menuOpen = false;
    this.router.navigate(['/settings']);
  }

  goHome(): void {
    if (this.username === 'Invite') {
      this.router.navigate(['/']);
      return;
    }

    if (this.role === 'Admin') {
      this.router.navigate(['/reception-admin']);
    } else {
      this.router.navigate(['/reception']);
    }
  }

  logout(): void {
    this.menuOpen = false;
    this.authService.logout();
    this.router.navigate(['/sign-in']);
  }

  private resetSteps(): void {
    this.steps = this.steps.map((step) => ({ ...step, state: 'pending' }));
  }

  private applyUpdateResult(result: UpdateCheckResult): void {
    this.currentVersion = result.currentVersion;
    this.latestVersion = result.latestVersion;
    this.release = result.release;
    if (result.release?.published_at) {
      this.latestReleaseDate = this.formatDateTime(result.release.published_at);
    }
    this.updateAvailable = result.updateAvailable && !!result.apkUrl;
    this.statusMessage = result.apkUrl
      ? result.message
      : 'Release trouvee, mais aucun fichier APK nest attache.';
  }

  private async loadReleaseDates(): Promise<void> {
    try {
      const [currentRelease, latestRelease] = await Promise.all([
        this.updateCheckService.getReleaseForVersion(this.currentVersion),
        this.updateCheckService.getLatestRelease(),
      ]);

      this.currentReleaseDate = currentRelease?.published_at
        ? this.formatDateTime(currentRelease.published_at)
        : 'Release introuvable';
      this.latestReleaseDate = latestRelease.published_at
        ? this.formatDateTime(latestRelease.published_at)
        : 'Release introuvable';
      this.cdr.detectChanges();
    } catch {
      this.currentReleaseDate = 'Indisponible';
      this.latestReleaseDate = 'Indisponible';
    }
  }

  private async loadChangelog(): Promise<void> {
    this.isLoadingChangelog = true;
    this.cdr.detectChanges();

    try {
      const releases = await this.updateCheckService.getRecentReleases(6);
      this.changelog = releases.map((release) => ({
        version: release.tag_name,
        title: release.name || release.tag_name,
        date: this.formatDateTime(release.published_at),
        description: this.formatReleaseDescription(release.body),
      }));
    } catch {
      this.changelog = [
        {
          version: '-',
          title: 'Changelog indisponible',
          date: '-',
          description: 'Impossible de recuperer les releases GitHub pour le moment.',
        },
      ];
    } finally {
      this.isLoadingChangelog = false;
      this.cdr.detectChanges();
    }
  }

  private formatReleaseDescription(body: string | null): string {
    if (!body || !body.trim()) {
      return 'Aucune description disponible pour cette release.';
    }

    return body
      .replace(/\r/g, '')
      .split('\n')
      .map((line) => line.replace(/^#+\s*/, '').trim())
      .filter((line) => line.length > 0)
      .join('\n');
  }

  private formatDateTime(value: string): string {
    return new Intl.DateTimeFormat('fr-FR', {
      dateStyle: 'long',
      timeStyle: 'short',
    }).format(new Date(value));
  }

  private formatBytes(bytes: number): string {
    if (bytes < 1024 * 1024) {
      return `${Math.round(bytes / 1024)} Ko`;
    }

    return `${(bytes / (1024 * 1024)).toFixed(1)} Mo`;
  }

  private formatError(error: unknown): string {
    if (error instanceof Error) {
      return error.message;
    }

    return String(error || 'Erreur inconnue pendant la mise a jour.');
  }
}
