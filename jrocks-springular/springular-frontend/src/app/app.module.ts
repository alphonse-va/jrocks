import {BrowserModule} from '@angular/platform-browser';
import {APP_INITIALIZER, NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {HttpClientModule} from '@angular/common/http';
// material
import {
  MAT_SNACK_BAR_DEFAULT_OPTIONS,
  MatAutocompleteModule,
  MatButtonModule,
  MatCardModule,
  MatCheckboxModule,
  MatDialogModule,
  MatGridListModule,
  MatIconModule,
  MatIconRegistry,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatPaginatorModule,
  MatProgressSpinnerModule,
  MatSnackBarModule,
  MatSortModule,
  MatTableModule,
  MatToolbarModule,
  MatTooltipModule
} from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FlexLayoutModule} from '@angular/flex-layout';
import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {HomeComponent} from './home';
import {LoginComponent} from './login';
import {AdminGuard, GuestGuard, LoginGuard} from './guard';
import {NotFoundComponent} from './not-found';
import {AccountMenuComponent} from './component/header/account-menu/account-menu.component';
import {ApiCardComponent, FooterComponent, GithubComponent, HeaderComponent} from './component';

import {ApiService, AuthService, ConfigService, FooService, UserService} from './service';
import {ChangePasswordComponent} from './change-password';
import {ForbiddenComponent} from './forbidden';
import {AdminComponent} from './admin';
import {SignupComponent} from './signup';
import {EntityMenuComponent} from './component/header/entity-menu/entity-menu.component';
import {ExampleService} from "./service/example.service";
import {LayoutModule} from '@angular/cdk/layout';
import {ExampleListComponent} from "./entity/list-example/example-list.component";
import {EditExampleComponent} from "./entity/list-example/edit/edit-example.component";
import {ViewExampleComponent} from "./entity/list-example/view/view-example.component";
import {ExampleDatatableComponent} from "./entity/example-datatable/example-datatable.component";
import {NewExampleDialogComponent} from "./entity/example-datatable/new/new-example-dialog.component";
import {EditExampleDialogComponent} from "./entity/example-datatable/edit/edit-example-dialog.component";
import {DeleteExampleDialogComponent} from "./entity/example-datatable/delete/delete-example-dialog.component";

export function initUserFactory(userService: UserService) {
  return () => userService.initUser();
}

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    ApiCardComponent,
    HomeComponent,
    GithubComponent,
    LoginComponent,
    NotFoundComponent,
    AccountMenuComponent,
    ChangePasswordComponent,
    ForbiddenComponent,
    AdminComponent,
    SignupComponent,
    EntityMenuComponent,
    ExampleListComponent,
    EditExampleComponent,
    ViewExampleComponent,
    // @@springular.module.declarations@@
    DeleteExampleDialogComponent,
    EditExampleDialogComponent,
    NewExampleDialogComponent,
    ExampleDatatableComponent,
  ],
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    HttpClientModule,
    AppRoutingModule,
    MatMenuModule,
    MatTooltipModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatToolbarModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatDialogModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatCheckboxModule,
    MatAutocompleteModule,
    FlexLayoutModule,
    MatGridListModule,
    MatListModule,
    LayoutModule,
    // @@springular.module.imports@@
  ],
  providers: [
    LoginGuard,
    GuestGuard,
    AdminGuard,
    FooService,
    AuthService,
    ApiService,
    UserService,
    ExampleService,
    ConfigService,
    MatIconRegistry,
    {
      'provide': APP_INITIALIZER,
      'useFactory': initUserFactory,
      'deps': [UserService],
      'multi': true
    },
    {provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: {duration: 6000}}
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    // @@springular.module.entryComponents@@
    DeleteExampleDialogComponent,
    EditExampleDialogComponent,
    NewExampleDialogComponent,
  ]

})
export class AppModule {
}
