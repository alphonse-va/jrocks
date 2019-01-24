import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {Example} from "../model/example";
import {ApiService} from "./api.service";
import {map} from "rxjs/operators";
import {HttpHeaders} from "@angular/common/http";
import {Subject} from "rxjs";


@Injectable()
export class ExampleService {

  private changeSubject = new Subject<Example>();

  header = new HttpHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  });

  constructor(
    private apiService: ApiService) {
  }

  findExamples(filter, sortOrder = 'asc', sortField = 'id',
               pageNumber = 0, pageSize = 3): Observable<ExampleRestResult> {
    if (filter) {
      filter = filter ? filter + '%' : '%';
      return this.apiService.get("/api/example/search/filter", {
        "username": filter,
        "firstname": filter,
        "lastname": filter,
        "size": pageSize.toString(),
        "page": pageNumber.toString(),
        "sort": sortField + "," + sortOrder
      }).pipe(
        map(res => new ExampleRestResult(res['_embedded']['examples'], res['page']['totalElements']))
      );
    } else {
      return this.apiService.get("/api/example", {
        "size": pageSize.toString(),
        "page": pageNumber.toString(),
        "sort": sortField + "," + sortOrder
      }).pipe(
        map(res => new ExampleRestResult(res['_embedded']['examples'], res['page']['totalElements']))
      );
    }
  }

  findById(id): Observable<Example> {
    return this.apiService.get("/api/example/" + id);
  }

  saveNewExample(example: Example) {
    return this.apiService.post("/api/example", example, this.header);
  }

  afterSave() {
    return this.changeSubject.asObservable();
  }

  saveExample(example: Example) {
    return this.apiService.put("/api/example/" + example.id, example, this.header)
      .map(saved => {
        this.changeSubject.next(saved);
        return saved;
      }).first();
  }

  deleteExample(id: number) {
    return this.apiService.delete("/api/example/" + id);
  }
}

export class ExampleRestResult {
  constructor(
    private examples: Example[],
    private count: number
  ) {
  }
}
