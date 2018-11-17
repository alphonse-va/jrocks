import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {MatPaginator, MatSort} from "@angular/material";
import {ExampleService} from "../../service/example.service";
import {debounceTime, distinctUntilChanged, tap} from 'rxjs/operators';
import {merge} from "rxjs/observable/merge";
import {fromEvent} from 'rxjs/observable/fromEvent';
import {ExampleDataSource} from "../../service/example.datasource";


@Component({
  selector: 'example',
  templateUrl: './example.component.html',
  styleUrls: ['./example.component.css']
})
export class ExampleComponent implements OnInit, AfterViewInit {

  dataSource: ExampleDataSource;

  displayedColumns = ["username", "firstname", "lastname"];

  @ViewChild(MatPaginator) paginator: MatPaginator;

  @ViewChild(MatSort) sort: MatSort;

  @ViewChild('input') input: ElementRef;

  constructor(private route: ActivatedRoute,
              private coursesService: ExampleService) {

  }

  ngOnInit() {
    this.dataSource = new ExampleDataSource(this.coursesService);
    this.dataSource.loadExampples('', 'username','asc', 0, 3);
  }

  ngAfterViewInit() {

    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    fromEvent(this.input.nativeElement, 'keyup')
      .pipe(
        debounceTime(150),
        distinctUntilChanged(),
        tap(() => {
          this.paginator.pageIndex = 0;

          this.loadExamplesPage();
        })
      )
      .subscribe();

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this.loadExamplesPage())
      )
      .subscribe();

  }

  loadExamplesPage() {
    this.dataSource.loadExampples(
      this.input.nativeElement.value,
      this.sort.direction,
      this.sort.active,
      this.paginator.pageIndex,
      this.paginator.pageSize);
  }
}
