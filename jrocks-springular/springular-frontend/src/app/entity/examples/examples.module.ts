import {NgModule} from '@angular/core';
import {
  MatAutocompleteModule,
  MatButtonModule,
  MatCardModule,
  MatCheckboxModule,
  MatDialogModule,
  MatGridListModule,
  MatIconModule, MatIconRegistry,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatPaginatorModule,
  MatProgressSpinnerModule,
  MatSnackBarModule,
  MatSortModule,
  MatTableModule,
  MatToolbarModule,
  MatTooltipModule
} from "@angular/material";
import {FlexLayoutModule} from "@angular/flex-layout";
import {LayoutModule} from "@angular/cdk/layout";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ExamplesRoutingModule} from './examples-routing.module';
import {ExampleComponent} from "./datatable-example/example.component";
import {NewExampleDialogComponent} from "./datatable-example/new/new-example-dialog.component";
import {DeleteExampleDialogComponent} from "./datatable-example/delete/delete-example-dialog.component";
import {EditExampleDialogComponent} from "./datatable-example/edit/edit-example-dialog.component";
import {CommonModule} from "@angular/common";

@NgModule({
  declarations: [ExampleComponent, NewExampleDialogComponent, DeleteExampleDialogComponent, EditExampleDialogComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatMenuModule,
    MatTooltipModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatToolbarModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatDialogModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatCheckboxModule,
    MatAutocompleteModule,
    ExamplesRoutingModule,
  ],
  providers: [
    MatIconRegistry,
  ],
  entryComponents: [NewExampleDialogComponent, EditExampleDialogComponent, DeleteExampleDialogComponent]
})
export class ExamplesModule {
}
