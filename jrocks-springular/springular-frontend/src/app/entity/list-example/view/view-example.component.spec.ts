import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewExampleComponent } from './view-example.component';

describe('EditExampleComponent', () => {
  let component: ViewExampleComponent;
  let fixture: ComponentFixture<ViewExampleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ViewExampleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewExampleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
