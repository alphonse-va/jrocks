import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Example} from "../../../model/example";
import {UserService} from "../../../service";
import {ExampleService} from "../../../service/example.service";


@Component({
  selector: 'app-edit',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.scss']
})
export class DeleteComponent implements OnInit {

  id: number;

  constructor(
    private exampleService: ExampleService,
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<DeleteComponent>,
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
