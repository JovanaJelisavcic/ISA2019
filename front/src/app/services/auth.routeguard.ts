import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot,Router } from '@angular/router';
import {UserModel} from "../model/user.model";

@Injectable()
export class AuthActivateRouteGuard implements CanActivate {
  user = new UserModel();

  constructor(private router: Router){

  }

  canActivate(route:ActivatedRouteSnapshot, state:RouterStateSnapshot){
    // @ts-ignore
    this.user = atob(sessionStorage.getItem('Authorization').split('.')[1]);
    if(!this.user){
      this.router.navigate(['login']);
    }
    return !!this.user;
  }

}
