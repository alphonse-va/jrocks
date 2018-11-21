import {AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
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
import {animate, state, style, transition, trigger} from "@angular/animations";


@Component({
  selector: 'example',
  templateUrl: './example.component.html',
  styleUrls: ['./example.component.scss'],
  animations: [
    trigger('listUpdate', [
      state('initial', style({
        backgroundColor: 'green',
        width: '100px',
        height: '100px'
      })),
      state('final', style({
        backgroundColor: 'red',
        width: '200px',
        height: '200px'
      })),
      transition('initial=>final', animate('1500ms')),
      transition('final=>initial', animate('1000ms'))
    ])
  ]
})
export class ExampleComponent implements OnInit, AfterViewInit {

  dataSource: ExampleDataSource;

  @Input() title: string = 'examples';
  @Input() subTitle: string = 'Search, add, edit or modify examples';
  @Input() pageSize: number = 10;
  @Input() sortField: string = 'username';
  @Input() sortDirection: string = 'asc';
  @Input() minHeight: string = '300px';
  @Input() maxHeight: string = '560px';
  @Input() readonly: boolean = false;
  @Input() displayedColumns: string[] = ["id", "username", "firstname", "lastname", "actions"];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild('usernameCriteria') usernameCriteria: ElementRef;

  constructor(private route: ActivatedRoute,
              private coursesService: ExampleService,
              private dialog: MatDialog,
              private snackBar: MatSnackBar) {
  }

  ngOnInit() {
    if (this.readonly) {
      this.displayedColumns = this.displayedColumns.filter(obj => obj !== "actions");
    }

    this.dataSource = new ExampleDataSource(this.coursesService);
    this.dataSource.loadExamples('', '', '', this.sortDirection, this.sortField, 0, this.pageSize);
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    if (!this.readonly) {
      this.subscribeOnKeyUp(this.usernameCriteria.nativeElement);
    }

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(tap(() => this.loadExamplesPage()))
      .subscribe();

  }

  private subscribeOnKeyUp(element) {
    fromEvent(element, 'keyup')
      .pipe(
        debounceTime(200),
        distinctUntilChanged(),
        tap(() => {
          this.paginator.pageIndex = 0;
          this.loadExamplesPage();
        })
      ).subscribe();
  }

  loadExamplesPage() {
    this.dataSource.loadExamples(
      this.readonly ? '' : this.usernameCriteria.nativeElement.value,
      '', '',
      this.sort.direction,
      this.sort.active,
      this.paginator.pageIndex,
      this.paginator.pageSize);
  }

  newExample() {
    this.dialog.open(NewComponent).afterClosed()
      .subscribe(val => {
        if (val) {
          this.sort.direction = 'desc';
          this.sort.active = 'id';
          this.paginator._changePageSize(this.paginator.pageSize);
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
}
