import { Injectable } from '@angular/core';
import { BadgeAttribution } from './BadgeAttribution';
import { DriverService } from './DriverService';
import { VehicleService } from './VehicleService';
import { ParkingService } from './ParkingService';
import { AssociateService } from './AssociateService';

@Injectable({
  providedIn: 'root',
})
export class RestServer {
  constructor(
    // SECURISATION : "readonly" empêche la réassignation malveillante des instances en mémoire
    private readonly badgeService: BadgeAttribution,
    private readonly driverService: DriverService,
    private readonly vehicleService: VehicleService,
    private readonly parkingService: ParkingService,
    private readonly associateService: AssociateService
  ) {}

  getBadgeService(): BadgeAttribution {
    return this.badgeService;
  }

  getDriverService(): DriverService {
    return this.driverService;
  }

  getVehicleService(): VehicleService {
    return this.vehicleService;
  }

  getParkingService(): ParkingService {
    return this.parkingService;
  }

  getAssociateService(): AssociateService {
    return this.associateService;
  }
}
