import { VehicleType as VehicleType } from './VehicleType';

// author Virgile

export class Vehicle {
  id: number;
  brand: string;
  numberPlate: string;
  type: VehicleType;
  class: string = '';

  constructor(
    id: number,
    brand: string,
    numberPlate: string,
    type: VehicleType = VehicleType.Voiture,
    class_: string = '',
  ) {
    this.id = id;
    this.brand = brand;
    this.numberPlate = numberPlate;
    this.type = type;
  }

  getId(): number {
    return this.id;
  }

  getBrand(): string {
    return this.brand;
  }

  getVehicleType(): VehicleType {
    return this.type;
  }

  getNumberPlate(): string {
    return this.numberPlate;
  }

  setVehicleType(vehicleType: VehicleType) {
    this.type = vehicleType;
  }
}
