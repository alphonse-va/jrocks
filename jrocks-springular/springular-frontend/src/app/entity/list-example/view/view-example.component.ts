import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ExampleService} from "../../../service/example.service";
import {Example} from "../../../model/example";

@Component({
  selector: 'app-view-example',
  templateUrl: './view-example.component.html',
  styleUrls: ['./view-example.component.scss']
})
export class ViewExampleComponent implements OnInit {

  model: Example = new Example();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: ExampleService) {
  }

  ngOnInit() {
    this.route.params.subscribe(p => this.getById(p && p['id']));
  }

  private getById(id: string): void {
    if (!id) {
      this.model = new Example();
      return;
    }

    this.service.findById(id)
      .subscribe(example => {
        this.model = example;
      }, (err) => {
        console.log(err);
        this.toList();
      });
  }



  edit() {
    this.router.navigate(["/list-examples/edit/" + this.model.id]);
  }

  toList() {
    this.router.navigate(['../'], {relativeTo: this.route});
  }
}
