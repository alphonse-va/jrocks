import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteExampleDialogComponent } from './delete-example-dialog.component';

describe('DeleteExampleDialogComponent', () => {
  let component: DeleteExampleDialogComponent;
  let fixture: ComponentFixture<DeleteExampleDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeleteExampleDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteExampleDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
