import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from "@angular/material";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Example} from "../../../../model/example";
import {ExampleService} from "../../../../service/example.service";


@Component({
  selector: 'app-edit',
  templateUrl: './new-example-dialog.component.html',
  styleUrls: ['./new-example-dialog.component.scss'],

})
export class NewExampleDialogComponent implements OnInit {

  form: FormGroup;
  example: Example;

  constructor(
    private exampleService: ExampleService,
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<NewExampleDialogComponent>) {

    this.form = fb.group({
      firstname: ["", Validators.required],
      lastname: ["", Validators.required],
      username: ["", Validators.required],
    });

  }

  ngOnInit() {
  }

  save() {
    this.exampleService.saveNewExample(this.form.value)
      .subscribe(saved => this.dialogRef.close(saved));
  }

  close() {
    this.dialogRef.close();
  }

}
