import { Injectable } from '@angular/core';
import { AttributionService } from '../rest/AttributionService';
import { BadgeService } from '../rest/BadgeService';
import { LocalService } from '../rest/LocalService';
import { TemperatureService } from '../rest/TemperatureService';
import { UtilisateurService } from '../rest/UtilisateurService';

@Injectable({
  providedIn: 'root',
})
export class RestServer {
  constructor(
    public utilisateurService: UtilisateurService,
    public badgeService: BadgeService,
    public attributionService: AttributionService,
    public localService: LocalService,
    public temperatureService: TemperatureService
  ) {}

  getUtilisateurService(): UtilisateurService {
    return this.utilisateurService;
  }

  getBadgeService(): BadgeService {
    return this.badgeService;
  }

  getAttributionService(): AttributionService {
    return this.attributionService;
  }

  getLocalService(): LocalService {
    return this.localService;
  }

  getTemperatureService(): TemperatureService {
    return this.temperatureService;
  }
}
