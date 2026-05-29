import { VehicleType } from './VehicleType';

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
    className: string = ''
  ) {
    this.id = id;
    this.brand = brand;
    this.numberPlate = numberPlate;
    this.type = type;
    this.class = className;
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

  setVehicleType(vehicleType: VehicleType): void {
    this.type = vehicleType;
  }

  // Ajout des setters indispensables pour les futurs ecrans de modification
  setBrand(brand: string): void {
    if (brand && brand.trim().length > 0) {
      this.brand = brand.trim();
    }
  }

  setNumberPlate(numberPlate: string): void {
    if (numberPlate && numberPlate.trim().length > 0) {
      // Normalisation automatique de la plaque en majuscules
      this.numberPlate = numberPlate.trim().toUpperCase();
    }
  }
}
