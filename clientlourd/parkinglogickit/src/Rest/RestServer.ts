import { Injectable } from '@angular/core';
import { BadgeService } from './BadgeAttribution';
import { DriverService } from './DriverService';
import { VehicleService } from './VehicleService';

// author Ethan

@Injectable({
  providedIn: 'root',
})
export class RestServer {
  constructor(private badgeService: BadgeService, private driverService: DriverService, private vehicleService: VehicleService) {}

  getBadgeService(): BadgeService {
    return this.badgeService;
  }

  getDriverService(): DriverService {
    return this.driverService;
  }

  getVehicleService(): VehicleService {
    return this.vehicleService;
  }
}
