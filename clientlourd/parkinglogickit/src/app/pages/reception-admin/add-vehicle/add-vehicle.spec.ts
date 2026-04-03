import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddVehicle } from './add-vehicle';

describe('AddVehicle', () => {
  let component: AddVehicle;
  let fixture: ComponentFixture<AddVehicle>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddVehicle]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddVehicle);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
