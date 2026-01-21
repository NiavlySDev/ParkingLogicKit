import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Reception } from './reception';

describe('Reception', () => {
  let component: Reception;
  let fixture: ComponentFixture<Reception>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Reception]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Reception);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
