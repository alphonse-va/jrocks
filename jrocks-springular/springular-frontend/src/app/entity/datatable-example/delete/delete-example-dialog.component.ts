import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Example} from "../../../model/example";
import {UserService} from "../../../service";
import {ExampleService} from "../../../service/example.service";


@Component({
  selector: 'app-edit',
  templateUrl: './delete-example-dialog.component.html',
  styleUrls: ['./delete-example-dialog.component.scss']
})
export class DeleteExampleDialogComponent implements OnInit {

  id: number;

  constructor(
    private exampleService: ExampleService,
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<DeleteExampleDialogComponent>,
    @Inject(MAT_DIALOG_DATA) {id, firstname, lastname, username}: Example) {

    this.id = id;
  }

  ngOnInit() {
  }

  delete() {
    this.exampleService.deleteExample(this.id);
    this.dialogRef.close("Delete example with id " + this.id);
  }

  close() {
    this.dialogRef.close();
  }

}