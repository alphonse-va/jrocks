import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Example} from "../../../model/example";
import {ExampleService} from "../../../service/example.service";


@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.scss']
})
export class EditComponent implements OnInit {

  form: FormGroup;
  username: string;

  constructor(
    private exampleService: ExampleService,
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<EditComponent>,
    @Inject(MAT_DIALOG_DATA) {id, firstname, lastname, username}: Example) {

    this.username = username;

    this.form = fb.group({
      id: [id, Validators.required],
      firstname: [firstname, Validators.required],
      lastname: [lastname, Validators.required],
      username: [username, Validators.required],
    });

  }

  ngOnInit() {
  }

  save() {
    this.exampleService.saveExample(this.form.value);
    this.dialogRef.close(this.form.value);
  }

  close() {
    this.dialogRef.close();
  }

}
