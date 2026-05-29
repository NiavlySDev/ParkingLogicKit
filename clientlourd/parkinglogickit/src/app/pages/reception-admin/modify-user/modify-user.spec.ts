import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifyUser } from './modify-user';

describe('ModifyUser', () => {
  let component: ModifyUser;
  let fixture: ComponentFixture<ModifyUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModifyUser],
    }).compileComponents();

    fixture = TestBed.createComponent(ModifyUser);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
