import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Example} from "../../../model/example";
import {ExampleService} from "../../../service/example.service";


@Component({
  selector: 'app-edit',
  templateUrl: './new.component.html',
  styleUrls: ['./new.component.scss']
})
export class NewComponent implements OnInit {

  form: FormGroup;
  example: Example;

  constructor(
    private exampleService: ExampleService,
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<NewComponent>) {


    this.form = fb.group({
      firstname: ["", Validators.required],
      lastname: ["", Validators.required],
      username: ["", Validators.required],
    });

  }

  ngOnInit() {
  }

  save() {
    this.exampleService.saveNewExample(this.form.value);
    this.dialogRef.close(this.form.value);
  }

  close() {
    this.dialogRef.close();
  }

}
