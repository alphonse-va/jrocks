import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {ExampleService} from "../../../service/example.service";
import {Subject} from "rxjs";
import {Example} from "../../../model/example";

@Component({
  selector: 'app-edit-example-detail',
  templateUrl: './edit-example.component.html',
  styleUrls: ['./edit-example.component.scss']
})
export class EditExampleComponent implements OnInit {

  private changeSubject = new Subject<Example>();

  form: FormGroup = new FormGroup({});

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: ExampleService,
    private fb: FormBuilder) {

    this.form = this.fb.group({
      id: '',
      username: ['', Validators.required],
      firstname: ['', Validators.required],
      lastname: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.route.params
      .subscribe(p => {
        this.service.findById(p && p['id'])
          .subscribe(example => {
            this.form = this.fb.group({
              id: example.id,
              username: [example.username, Validators.required],
              firstname: [example.firstname, Validators.required],
              lastname: [example.lastname, Validators.required]
            });
          });

      });
  }

  save() {
    this.service.saveExample(this.form.value)
      .subscribe(saved => {
        this.changeSubject.next(saved);
        this.router.navigate(["/list-examples/" + saved.id])
      })
  }

  cancel() {
    this.router.navigate(["/list-examples/" + this.form.value.id])
  }

  afterSave() {
    return this.changeSubject.asObservable();
  }
}
