import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SwimrankingNumberComponent } from './swimranking-number.component';

describe('SwimrankingNumberComponent', () => {
  let component: SwimrankingNumberComponent;
  let fixture: ComponentFixture<SwimrankingNumberComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SwimrankingNumberComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SwimrankingNumberComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
