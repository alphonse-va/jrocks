import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditExampleDialogComponent } from './edit-example-dialog.component';

describe('DeleteExampleDialogComponent', () => {
  let component: EditExampleDialogComponent;
  let fixture: ComponentFixture<EditExampleDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditExampleDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditExampleDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
