import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewExampleDialogComponent } from './new-example-dialog.component';

describe('DeleteExampleDialogComponent', () => {
  let component: NewExampleDialogComponent;
  let fixture: ComponentFixture<NewExampleDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewExampleDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewExampleDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
