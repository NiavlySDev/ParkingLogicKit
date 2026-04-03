import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteVehicle } from './delete-vehicle';

describe('DeleteVehicle', () => {
  let component: DeleteVehicle;
  let fixture: ComponentFixture<DeleteVehicle>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeleteVehicle]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeleteVehicle);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
