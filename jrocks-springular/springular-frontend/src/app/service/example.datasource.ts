import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {Observable} from "rxjs/Observable";
import {Example} from "../model/example";
import {ExampleService} from "./example.service";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {catchError, finalize} from "rxjs/operators";
import {of} from "rxjs/observable/of";

export class ExampleDataSource implements DataSource<Example> {

  private examplesSubject = new BehaviorSubject<Example[]>([]);

  private loadingSubject = new BehaviorSubject<boolean>(false);

  private numberOfElementsSubject = new BehaviorSubject<number>(0);

  private _loading$ = this.loadingSubject.asObservable();

  private _numberOfElements$ = this.numberOfElementsSubject.asObservable();

  constructor(private exampleService: ExampleService) {
  }

  loadExamples(
    filter: string,
    sortDirection: string,
    sortField: string,
    pageIndex: number,
    pageSize: number) {

    this.loadingSubject.next(true);

    this.exampleService.findExamples(filter,
      sortDirection, sortField, pageIndex, pageSize)
      .pipe(catchError(() => of([])),
        finalize(() => this.loadingSubject.next(false))
      )
      .subscribe(result => {
        this.examplesSubject.next(result['examples']);
        this.numberOfElementsSubject.next(result['count']);
      });

  }

  connect(collectionViewer: CollectionViewer): Observable<Example[]> {
    console.log("Connecting data source");
    return this.examplesSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.examplesSubject.complete();
    this.loadingSubject.complete();
    this.numberOfElementsSubject.complete();
  }

  get loading$(): Observable<boolean> {
    return this._loading$;
  }

  get numberOfElements$(): Observable<number> {
    return this._numberOfElements$;
  }
}

