import { DriverType } from './DriverType';

export class Driver {
  id: number;
  lastName: string;
  firstName: string;
  age: number;
  isMale: boolean;
  type: DriverType;
  username: string;
  password: string;
  class: string;

  constructor(
    id: number,
    lastName: string,
    firstName: string,
    username: string,
    password: string = '',
    age: number = 0,
    isMale: boolean = false,
    type: DriverType = DriverType.Driver,
    className: string = ''
  ) {
    this.id = id;
    this.lastName = lastName;
    this.firstName = firstName;
    this.username = username;
    this.password = password;
    this.age = age;
    this.isMale = isMale;
    this.type = type;
    this.class = className;
  }

  getId(): number {
    return this.id;
  }

  getLastName(): string {
    return this.lastName;
  }

  getFirstName(): string {
    return this.firstName;
  }

  getUsername(): string {
    return this.username;
  }

  getPassword(): string {
    return this.password;
  }

  getAge(): number {
    return this.age;
  }

  getIsMale(): boolean {
    return this.isMale;
  }

  getUserType(): DriverType {
    return this.type;
  }

  setLastName(lastName: string): void {
    if (lastName && lastName.trim().length > 0) {
      this.lastName = lastName.trim();
    }
  }

  setFirstName(firstName: string): void {
    if (firstName && firstName.trim().length > 0) {
      this.firstName = firstName.trim();
    }
  }

  /**
   * Genere un identifiant de secours standardise de maniere securisee
   */
  generateAndSetDefaultUsername(): void {
    const validFirst = (this.firstName || '').trim().toLowerCase();
    const validLast = (this.lastName || '').trim().toLowerCase();

    if (validFirst.length > 0 && validLast.length > 0) {
      this.username = validFirst.substring(0, 1) + '.' + validLast;
    }
  }

  setUsername(username: string): void {
    if (username && username.trim().length > 0) {
      this.username = username.trim().replace(/\s/g, '');
    }
  }

  setPassword(password: string): void {
    this.password = password;
  }

  setAge(age: number): void {
    if (age >= 1 && age <= 120) {
      this.age = age;
    }
  }

  setIsMale(isMale: boolean): void {
    this.isMale = isMale;
  }

  setUserType(userType: DriverType): void {
    this.type = userType;
  }
}
