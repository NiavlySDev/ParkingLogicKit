import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReceptionAdmin } from './reception-admin';

describe('ReceptionAdmin', () => {
  let component: ReceptionAdmin;
  let fixture: ComponentFixture<ReceptionAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReceptionAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReceptionAdmin);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
