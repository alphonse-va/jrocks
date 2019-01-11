import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Example} from "../../../model/example";
import {ExampleService} from "../../../service/example.service";


@Component({
  selector: 'app-edit',
  templateUrl: './edit-example-dialog.component.html',
  styleUrls: ['./edit-example-dialog.component.scss']
})
export class EditExampleDialogComponent implements OnInit {

  form: FormGroup;

  constructor(
    private exampleService: ExampleService,
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<EditExampleDialogComponent>,
    @Inject(MAT_DIALOG_DATA) {id, firstname, lastname, username}: Example) {

    this.form = fb.group({
      id: [id, Validators.required],
      firstname: [firstname, ],
      lastname: [lastname, Validators.required],
      username: [username, Validators.required],
    });

  }

  ngOnInit() {
  }

  save() {
    if (this.form.valid) {
      this.exampleService.saveExample(this.form.value)
        .subscribe(saved => console.log(saved));
      this.dialogRef.close(this.form.value);
    }
  }

  close() {
    this.dialogRef.close();
  }

}
