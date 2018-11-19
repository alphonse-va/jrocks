import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {MatDialog, MatDialogConfig, MatPaginator, MatSnackBar, MatSort} from "@angular/material";
import {ExampleService} from "../../service/example.service";
import {debounceTime, distinctUntilChanged, tap} from 'rxjs/operators';
import {merge} from "rxjs/observable/merge";
import {fromEvent} from 'rxjs/observable/fromEvent';
import {ExampleDataSource} from "../../service/example.datasource";
import {Example} from "../../model/example";
import {EditComponent} from "./edit/edit.component";
import {DeleteExampleDialogComponent} from "./delete/delete-example-dialog.component";
import {NewComponent} from "./new/new.component";


@Component({
  selector: 'example',
  templateUrl: './example.component.html',
  styleUrls: ['./example.component.scss']
})
export class ExampleComponent implements OnInit, AfterViewInit {

  dataSource: ExampleDataSource;

  displayedColumns = ["id", "username", "firstname", "lastname", "actions"];

  @ViewChild(MatPaginator) paginator: MatPaginator;

  @ViewChild(MatSort) sort: MatSort;

  @ViewChild('input') input: ElementRef;

  constructor(private route: ActivatedRoute,
              private coursesService: ExampleService,
              private dialog: MatDialog,
              private snackBar: MatSnackBar,
  ) {

  }

  ngOnInit() {
    this.dataSource = new ExampleDataSource(this.coursesService);
    this.dataSource.loadExampples('', 'asc', 'username', 0, 5);
  }

  ngAfterViewInit() {

    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    fromEvent(this.input.nativeElement, 'keyup')
      .pipe(
        debounceTime(200),
        distinctUntilChanged(),
        tap(() => {
          this.paginator.pageIndex = 0;

          this.loadExamplesPage();
        })
      ).subscribe();

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

  newExample() {
    this.dialog.open(NewComponent).afterClosed()
      .subscribe(val => {
        this.paginator._changePageSize(this.paginator.pageSize);
        if (val) {
          this.snackBar.open('Example ' + val.username + ' added with success!');
        }
      });
  }

  editExample({id, firstname, lastname, username}: Example) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {id, firstname, lastname, username};

    this.dialog.open(EditComponent, dialogConfig).afterClosed()
      .subscribe(val => {
        if (val) {
          this.paginator._changePageSize(this.paginator.pageSize);
          this.snackBar.open('Example ' + firstname + " " + lastname + ' saved with success!');
        }
      });
  }

  deleteExample({id, firstname, lastname, username}: Example) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {id, firstname, lastname, username};

    this.dialog.open(DeleteExampleDialogComponent, dialogConfig).afterClosed()
      .subscribe(val => {
        if (val) {
          this.paginator._changePageSize(this.paginator.pageSize);
          this.snackBar.open('Example ' + firstname + " " + lastname + ' deleted with success!', 'Close');
        }
      });
  }

  // workaround for new added entry
}
