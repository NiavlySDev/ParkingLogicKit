import { Injectable } from '@angular/core';
import { BadgeService } from './BadgeAttribution';
import { DriverService } from './DriverService';
import { VehicleService } from './VehicleService';
import { ParkingService } from './ParkingService';
// author Ethan

@Injectable({
  providedIn: 'root',
})
export class RestServer {
  constructor(private badgeService: BadgeService, private driverService: DriverService, private vehicleService: VehicleService, private parkingService: ParkingService) {}

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
}
