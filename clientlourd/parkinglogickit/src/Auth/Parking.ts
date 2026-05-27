export class Parking {
  id: number;
  isFull: boolean;
  placeCount: number;
  totalPlace: number;
  class: string = '';

  constructor(
    id: number,
    isFull: boolean = false,
    placeCount: number = 0,
    totalPlace: number = 0,
    className: string = ''
  ) {
    this.id = id;
    this.isFull = isFull;
    this.placeCount = placeCount;
    this.totalPlace = totalPlace;
    this.class = className;
  }

  getId(): number {
    return this.id;
  }

  getIsFull(): boolean {
    return this.isFull;
  }

  getPlaceCount(): number {
    return this.placeCount;
  }

  getTotalPlace(): number {
    return this.totalPlace;
  }

  setIsFull(isFull: boolean): void {
    this.isFull = isFull;
  }

  setPlaceCount(placeCount: number): void {
    if (placeCount >= 0) {
      this.placeCount = placeCount;
    }
  }

  setTotalPlace(totalPlace: number): void {
    if (totalPlace >= 0) {
      this.totalPlace = totalPlace;
    }
  }
}
