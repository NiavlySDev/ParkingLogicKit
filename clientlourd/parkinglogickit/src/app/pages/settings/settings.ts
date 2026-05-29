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
    this.username = (await this.authService.getUsername()) || '';
    this.role = (await this.authService.getRole()) || 'Driver';

    if (!this.username) {
      this.logout();
      return;
    }

    this.currentVersion = await this.updateCheckService.getCurrentVersion();
    this.steps[0].state = 'done';
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
    this.steps[2].state = 'active';
    this.statusMessage = 'Telechargement de la mise a jour...';
    this.cdr.detectChanges();

    try {
      await this.updateCheckService.installRelease(this.release);
      this.steps[2].state = 'done';
      this.steps[3].state = 'done';
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

  checkFromInformation(): void {
    this.activeTab = 'updates';
    void this.checkForUpdate();
  }

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  goProfile(): void {
    this.menuOpen = false;
    this.router.navigate(['/user-profile']);
  }

  goSettings(): void {
    this.menuOpen = false;
    this.router.navigate(['/settings']);
  }

  goHome(): void {
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
    this.updateAvailable = result.updateAvailable && !!result.apkUrl;
    this.statusMessage = result.apkUrl
      ? result.message
      : 'Release trouvee, mais aucun fichier APK nest attache.';
  }

  private formatError(error: unknown): string {
    if (error instanceof Error) {
      return error.message;
    }

    return String(error || 'Erreur inconnue pendant la mise a jour.');
  }
}
