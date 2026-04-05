// author Virgile

export class Parking {
  id: number;
  isFull: string;
  placeCount: string;
  totalPlace: string;
  class: string = '';

  constructor(
    id: number,
    isFull: string,
    placeCount: string,
    totalPlace: string = '',
    class_: string = '',
  ) {
    this.id = id;
    this.isFull = isFull;
    this.placeCount = placeCount;
    this.totalPlace = totalPlace;
  }

  getId(): number {
    return this.id;
  }

  getIsFull(): string {
    return this.isFull;
  }

  getPlaceCount(): string {
    return this.placeCount;
  }

  getTotalPlace(): string {
    return this.totalPlace;
  }
}
