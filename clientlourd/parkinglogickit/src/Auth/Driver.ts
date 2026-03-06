import { DriverType as DriverType } from './DriverType';

// author Ethan

export class Driver {
  id: number;
  lastName: string;
  firstName: string;
  age: number;
  masculin: number;
  userType: DriverType;

  login: string;
  password: string;

  constructor(
    id: number,
    lastName: string,
    firstName: string,
    login: string,
    password: string = '',
    age: number = 0,
    masculin: number = 0,
    userType: DriverType = DriverType.Driver
  ) {
    this.id = id;
    this.lastName = lastName;
    this.firstName = firstName;
    this.login = login;
    this.password = password;
    this.age = age;
    this.masculin = masculin;
    this.userType = userType;
  }

  getId(): number {
    return this.id;
  }

  getLastName(): string {
    return this.lastName;
  }

  getfirstName(): string {
    return this.firstName;
  }

  getLogin(): string {
    return this.login;
  }

  getPassword(): string {
    return this.password;
  }

  getAge(): number {
    return this.age;
  }

  getGender(): number {
    return this.masculin;
  }

  getUserType(): DriverType {
    return this.userType;
  }

  setLastName(lastName: string) {
    this.lastName = lastName;
  }

  setfirstName(firstName: string) {
    this.firstName = firstName;
  }

  setLogin() {
    this.login = this.firstName.toLowerCase().substring(0, 1) + '.' + this.lastName.toLowerCase();
  }

  setPassword(password: string) {
    this.password = password;
  }

  setAge(age: number) {
    this.age = age;
  }

  setGender(masculin: number) {
    this.masculin = masculin;
  }

  setUserType(userType: DriverType) {
    this.userType = userType;
  }
}
