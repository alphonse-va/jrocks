import {AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {MatSnackBar} from "@angular/material";
import {ExampleService} from "../../service/example.service";
import {debounceTime, distinctUntilChanged, tap} from 'rxjs/operators';
import {fromEvent} from 'rxjs/observable/fromEvent';
import {Example} from "../../model/example";
import {FormBuilder} from "@angular/forms";

@Component({
  selector: 'list-example',
  templateUrl: './list-example.component.html',
  styleUrls: ['./list-example.component.scss']
})
export class ListExampleComponent implements OnInit, AfterViewInit {

  @Input() title: string = 'examples';
  @Input() subTitle: string = 'Search, add, edit or modify examples';
  @Input() pageSize: number = 5;
  @Input() sortField: string = 'username';
  @Input() sortDirection: string = 'asc';

  @ViewChild('filter') filter: ElementRef;

  examples: Example[];

  editMode: boolean = false;

  constructor(private route: ActivatedRoute,
              private formBuider: FormBuilder,
              private exampleService: ExampleService,
              private snackBar: MatSnackBar) {
  }

  ngOnInit() {
  }

  ngAfterViewInit() {
    this.subscribeOnKeyUp(this.filter.nativeElement);
    this.loadExamplesPage();
  }

  edit() {
    this.editMode = true;
  }

  save() {
    this.editMode = false;
    this.loadExamplesPage();
  }

  private subscribeOnKeyUp(element) {
    fromEvent(element, 'keyup')
      .pipe(
        debounceTime(200),
        distinctUntilChanged(),
        tap(() => {
          this.loadExamplesPage();
        })
      ).subscribe();
  }

  select(example: Example) {
    this.editMode = false;
  }

  loadExamplesPage() {
    this.exampleService.findExamples(this.filter.nativeElement.value, this.sortDirection, this.sortField, 0, this.pageSize)
      .subscribe(result => this.examples = result['examples']);
  }
}
