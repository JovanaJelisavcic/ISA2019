import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {LandingPageComponent} from "./components/langing-page/landing-page.component";
import {LoginComponent} from "./components/login/login.component";
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {AuthActivateRouteGuard} from "./services/auth.routeguard"; // CLI imports router

const routes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'home', component: LandingPageComponent},
  {path: 'register', component: LoginComponent},
  {path: 'dashboard', component: DashboardComponent, canActivate: [AuthActivateRouteGuard]}
]; // sets up routes constant where you define your routes

// configures NgModule imports and exports
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
