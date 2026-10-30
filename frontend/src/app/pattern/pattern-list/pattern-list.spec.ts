import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PatternList } from './pattern-list';

describe('PatternList', () => {
  let component: PatternList;
  let fixture: ComponentFixture<PatternList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PatternList],
    }).compileComponents();

    fixture = TestBed.createComponent(PatternList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
