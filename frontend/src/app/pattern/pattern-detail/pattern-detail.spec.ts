import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PatternDetail } from './pattern-detail';

describe('PatternDetail', () => {
  let component: PatternDetail;
  let fixture: ComponentFixture<PatternDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PatternDetail],
    }).compileComponents();

    fixture = TestBed.createComponent(PatternDetail);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
