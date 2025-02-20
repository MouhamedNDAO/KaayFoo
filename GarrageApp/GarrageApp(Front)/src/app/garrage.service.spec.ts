import { TestBed } from '@angular/core/testing';

import { GarrageService } from './garrage.service';

describe('GarrageService', () => {
  let service: GarrageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GarrageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
