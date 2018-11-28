import {AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ExampleService} from "../../service/example.service";
import {debounceTime, distinctUntilChanged, tap} from 'rxjs/operators';
import {fromEvent} from 'rxjs/observable/fromEvent';
import {Example} from "../../model/example";
import {MatSnackBar} from "@angular/material";

@Component({
  selector: 'list-example',
  templateUrl: './example-list.component.html',
  styleUrls: ['./example-list.component.scss']
})
export class ExampleListComponent implements OnInit, AfterViewInit {

  @Input() title: string = 'examples';
  @Input() subTitle: string = 'Search, add, edit or modify examples';
  @Input() pageSize: number = 5;
  @Input() sortField: string = 'username';
  @Input() sortDirection: string = 'asc';

  @ViewChild('filter') filter: ElementRef;

  examples: Example[];
  pageNumber: number = 0;
  private totalNumberOfElement: number;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private exampleService: ExampleService,
              private snackBar: MatSnackBar) {

    this.exampleService.afterSave().subscribe(example => {

      this.snackBar.open('Example ' + example.firstname + " " + example.lastname + ' saved with success!', 'Cancel');

      this.loadExamplesPage();
    });
  }

  ngOnInit() {
  }

  ngAfterViewInit() {
    this.subscribeOnKeyUp(this.filter.nativeElement);
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
    this.router.navigate(["/list-examples/" + example.id]);
  }

  loadExamplesPage() {
    this.exampleService.findExamples(this.filter.nativeElement.value, this.sortDirection, this.sortField, this.pageNumber, this.pageSize)
      .subscribe(result => {
        this.examples = result['examples'];
        this.totalNumberOfElement = result['count'];
      });
  }

  next() {
    this.pageNumber++;
    this.loadExamplesPage();
  }

  canNext() {
    return this.pageSize + this.pageNumber * this.pageSize <= this.totalNumberOfElement;
  }

  preview() {
    this.pageNumber--;
    this.loadExamplesPage();
  }

  canPreview() {
    return this.pageNumber > 0;
  }
}
