import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {Example} from "../model/example";
import {ApiService} from "./api.service";
import {map} from "rxjs/operators";
import {HttpHeaders} from "@angular/common/http";
import {logger} from "codelyzer/util/logger";


@Injectable()
export class ExampleService {

  constructor(
    private apiService: ApiService) {
  }

  findExamples(filter = '', sortOrder = 'asc',
               sortField = 'username', pageNumber = 0, pageSize = 3): Observable<ExampleRestResult> {
    if (filter != '') {
      filter = filter + '%';
      return this.apiService.get("/api/example/search/filter", {
        "firstname": filter,
        "lastname": filter,
        "username": filter,
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

  saveExample(example: Example) {
    const header = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    });
    this.apiService.put("/api/example/" + example.id, example, header)
      .subscribe(saved => {
        example = saved;
        
      
      });
  }

  deleteExample(id: number) {
    this.apiService.delete("/api/example/" + id)
      .subscribe(saved => {
        console.log("Example with id " + id  + " deleted with success");
      });
  }
}

export class ExampleRestResult {
  constructor(
    private examples: Example[],
    private count: number
  ) {
  }
}
