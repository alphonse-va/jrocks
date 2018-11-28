import {AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {MatDialog, MatDialogConfig, MatPaginator, MatSnackBar, MatSort} from "@angular/material";
import {ExampleService} from "../../service/example.service";
import {debounceTime, distinctUntilChanged, tap} from 'rxjs/operators';
import {merge} from "rxjs/observable/merge";
import {fromEvent} from 'rxjs/observable/fromEvent';
import {ExampleDataSource} from "../../service/example.datasource";
import {Example} from "../../model/example";
import {EditExampleDialogComponent} from "./edit/edit-example-dialog.component";
import {DeleteExampleDialogComponent} from "./delete/delete-example-dialog.component";
import {NewExampleDialogComponent} from "./new/new-example-dialog.component";


@Component({
  selector: 'example',
  templateUrl: './example.component.html',
  styleUrls: ['./example.component.scss'],
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
  @ViewChild('filter') filter: ElementRef;

  constructor(private route: ActivatedRoute,
              private exampleService: ExampleService,
              private dialog: MatDialog,
              private snackBar: MatSnackBar) {
  }

  ngOnInit() {
    if (this.readonly) {
      this.displayedColumns = this.displayedColumns.filter(obj => obj !== "actions");
    }

    this.dataSource = new ExampleDataSource(this.exampleService);
    this.dataSource.loadExamples('', this.sortDirection, this.sortField, 0, this.pageSize);
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    if (!this.readonly) {
      this.subscribeOnKeyUp(this.filter.nativeElement);
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
      this.readonly ? '' : this.filter.nativeElement.value,
      this.sort.direction,
      this.sort.active,
      this.paginator.pageIndex,
      this.paginator.pageSize);
  }

  newExample() {
    this.dialog.open(NewExampleDialogComponent).afterClosed()
      .subscribe(val => {
        if (val) {
          this.sort.direction = 'desc';
          this.sort.active = 'id';
          this.updateDataTable();
          this.snackBar.open('Example ' + val.username + ' added with success!', 'Undo')
            .onAction().subscribe(() => {
              this.exampleService.deleteExample(val.id)
                .subscribe(() => this.updateDataTable());
          })
        }
      });
  }

  editExample({id, firstname, lastname, username}: Example) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {id, firstname, lastname, username};
    const backup = new Example(id, username, firstname, lastname);

    this.dialog.open(EditExampleDialogComponent, dialogConfig).afterClosed()
      .subscribe(val => {
        if (val) {
          this.updateDataTable();
          this.notifyAction('Example ' + firstname + " " + lastname + ' saved with success!', backup);
        }
      });
  }

  private notifyAction(message: string, backup: Example) {
    this.snackBar.open(message, 'Undo')
      .onAction().subscribe(() => {
      this.exampleService.saveExample(backup)
        .subscribe(() => this.updateDataTable());
    })
  }

  deleteExample({id, firstname, lastname, username}: Example) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {id, firstname, lastname, username};
    const backup = new Example(id, username, firstname, lastname);

    this.dialog.open(DeleteExampleDialogComponent, dialogConfig).afterClosed()
      .subscribe(val => {
        if (val) {
          this.notifyAction('Example ' + firstname + " " + lastname + ' saved with success!', backup);
          this.updateDataTable();
        }
      });
  }

  private updateDataTable() {
    this.paginator._changePageSize(this.paginator.pageSize);
  }
}
