import { TestBed } from '@angular/core/testing';
import { PatternApi } from './pattern-api';

describe('PatternApi', () => {
  let service: PatternApi;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PatternApi);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
