import { Injectable } from '@angular/core';
import { BadgeService } from './BadgeAttribution';
import { DriverService } from './DriverService';
import { VehicleService } from './VehicleService';
import { ParkingService } from './ParkingService';
import { AssociateService } from './AssociateService';

@Injectable({
  providedIn: 'root',
})
export class RestServer {

  constructor(
    private badgeService: BadgeService,
    private driverService: DriverService,
    private vehicleService: VehicleService,
    private parkingService: ParkingService,
    private associateService: AssociateService
  ) {}

  getBadgeService(): BadgeService {
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