import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './components/login/login.component';
import { LandingPageComponent } from './components/langing-page/landing-page.component';
import {AppRoutingModule} from "./app-routing.module";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatRadioModule} from "@angular/material/radio";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatDividerModule} from "@angular/material/divider";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatGridListModule} from "@angular/material/grid-list";
import {MatStepperModule} from "@angular/material/stepper";
import {MatTabsModule} from "@angular/material/tabs";
import {MatTreeModule} from "@angular/material/tree";
import {MatButtonModule} from "@angular/material/button";
import {MatButtonToggleModule} from "@angular/material/button-toggle";
import {MatBadgeModule} from "@angular/material/badge";
import {MatChipsModule} from "@angular/material/chips";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatBottomSheetModule} from "@angular/material/bottom-sheet";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatSortModule} from "@angular/material/sort";
import {MatTableModule} from "@angular/material/table";
import {MatCardModule} from "@angular/material/card";
import {FormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import {MatIconModule} from "@angular/material/icon";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {JwtInterceptor} from "./services/app.request.interceptor";
import { DashboardComponent } from './components/dashboard/dashboard.component';
import {AuthActivateRouteGuard} from "./services/auth.routeguard";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LandingPageComponent,
    DashboardComponent
  ],
    imports: [
        AppRoutingModule,
        BrowserModule,
        BrowserAnimationsModule,
        MatAutocompleteModule,
        MatCheckboxModule,
        MatDatepickerModule,
        MatFormFieldModule,
        MatRadioModule,
        MatSelectModule,
        MatSlideToggleModule,
        MatSidenavModule,
        MatToolbarModule,
        MatDividerModule,
        MatExpansionModule,
        MatGridListModule,
        MatStepperModule,
        MatTabsModule,
        MatTreeModule,
        MatButtonModule,
        MatButtonToggleModule,
        MatBadgeModule,
        MatChipsModule,
        MatProgressSpinnerModule,
        MatProgressBarModule,
        MatBottomSheetModule,
        MatSnackBarModule,
        MatPaginatorModule,
        MatSortModule,
        MatTableModule,
        MatCardModule,
        MatInputModule,
        FormsModule,
        MatIconModule,
        HttpClientModule
    ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    },
    AuthActivateRouteGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
