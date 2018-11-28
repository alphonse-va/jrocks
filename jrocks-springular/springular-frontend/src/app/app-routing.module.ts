import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './home';
import {LoginComponent} from './login';
import {AdminComponent} from './admin';
import {AdminGuard, GuestGuard, LoginGuard} from './guard';
import {NotFoundComponent} from './not-found';
import {ChangePasswordComponent} from './change-password';
import {ForbiddenComponent} from './forbidden';
import {SignupComponent} from './signup';
import {ExampleComponent} from "./entity/datatable-example/example.component";
import {ExampleListComponent} from "./entity/list-example/example-list.component";
import {EditExampleComponent} from "./entity/list-example/edit/edit-example.component";
import {ViewExampleComponent} from "./entity/list-example/view/view-example.component";

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    pathMatch: 'full'
  },
  {
    path: 'signup',
    component: SignupComponent,
    canActivate: [GuestGuard],
    pathMatch: 'full'
  },
  {
    path: 'example',
    component: ExampleComponent,
    canActivate: [LoginGuard],
    pathMatch: 'full'
  },
  {
    path: 'list-examples',
    component: ExampleListComponent,
    canActivate: [LoginGuard],
    children: [
      {
        path: ':id',
        component: ViewExampleComponent,
      },
      {
        path: 'edit/:id',
        component: EditExampleComponent,
      },
    ]
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [GuestGuard]
  },
  {
    path: 'change-password',
    component: ChangePasswordComponent,
    canActivate: [LoginGuard]
  },
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [AdminGuard]
  },
  {
    path: '404',
    component: NotFoundComponent
  },
  {
    path: '403',
    component: ForbiddenComponent
  },
  {
    path: '**',
    redirectTo: '/404'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: []
})
export class AppRoutingModule {
}
