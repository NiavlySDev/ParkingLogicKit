import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifyVehicle } from './modify-vehicle';

describe('ModifyVehicle', () => {
  let component: ModifyVehicle;
  let fixture: ComponentFixture<ModifyVehicle>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModifyVehicle],
    }).compileComponents();

    fixture = TestBed.createComponent(ModifyVehicle);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
