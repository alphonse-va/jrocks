import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {Example} from "../model/example";
import {ApiService} from "./api.service";
import {map} from "rxjs/operators";
import {HttpHeaders} from "@angular/common/http";


@Injectable()
export class ExampleService {

  header = new HttpHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  });

  constructor(
    private apiService: ApiService) {
  }

  findExamples(usernameFilter, firstnameFilter, lastnameFilter,
               sortOrder = 'asc', sortField = 'username',
               pageNumber = 0, pageSize = 3): Observable<ExampleRestResult> {
    if (usernameFilter || firstnameFilter || lastnameFilter) {

      usernameFilter = usernameFilter ? usernameFilter + '%' : null;
      firstnameFilter = firstnameFilter + '%';
      lastnameFilter = lastnameFilter + '%' ;

      return this.apiService.get("/api/example/search/filter", {
        "firstname": firstnameFilter,
        "lastname": lastnameFilter,
        "username": usernameFilter,
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

  saveNewExample(example: Example) {
    this.apiService.post("/api/example", example, this.header)
      .subscribe(saved => {
        example = saved;
      });
  }


  saveExample(example: Example) {
    this.apiService.put("/api/example/" + example.id, example, this.header)
      .subscribe(saved => {
        example = saved;
      });
  }

  deleteExample(id: number) {
    this.apiService.delete("/api/example/" + id)
      .subscribe(saved => {
        console.log("Example with id " + id + " deleted with success");
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
