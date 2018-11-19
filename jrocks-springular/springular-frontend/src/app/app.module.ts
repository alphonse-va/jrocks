import {BrowserModule} from '@angular/platform-browser';
import {APP_INITIALIZER, NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {HttpClientModule} from '@angular/common/http';
// material
import {
  MAT_SNACK_BAR_DEFAULT_OPTIONS,
  MatButtonModule,
  MatCardModule,
  MatDialogModule,
  MatGridListModule,
  MatIconModule,
  MatIconRegistry,
  MatInputModule,
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
import {ChangePasswordComponent} from './change-password/change-password.component';
import {ForbiddenComponent} from './forbidden/forbidden.component';
import {AdminComponent} from './admin/admin.component';
import {SignupComponent} from './signup/signup.component';
import {EntityMenuComponent} from './component/header/entity-menu/entity-menu.component';
import {ExampleComponent} from './entity/example/example.component';
import {ExampleService} from "./service/example.service";
import {EditComponent} from './entity/example/edit/edit.component';
import {LayoutModule} from '@angular/cdk/layout';
import {DeleteComponent} from "./entity/example/delete/delete.component";
import {NewCommand} from "@angular/cli/commands/new-impl";
import {NewComponent} from "./entity/example/new/new.component";

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
    ExampleComponent,
    NewComponent,
    EditComponent,
    DeleteComponent
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
    FlexLayoutModule,
    MatGridListModule,
    LayoutModule
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
    {provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: {duration: 2500}}
  ],
  bootstrap: [AppComponent],
  entryComponents: [NewComponent, EditComponent, DeleteComponent]

})
export class AppModule {
}
