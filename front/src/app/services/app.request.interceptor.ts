import { Injectable } from '@angular/core';
import { HttpInterceptor,HttpRequest,HttpHandler,HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import {Router} from '@angular/router';
import {tap} from 'rxjs/operators';
import {environment} from "../../environments/environment";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    let httpHeaders = new HttpHeaders();
    let jwtAuthorization = sessionStorage.getItem('Authorization');
    if (jwtAuthorization) {
      httpHeaders = httpHeaders.append('Authorization', jwtAuthorization);
    }
    httpHeaders = httpHeaders.append('X-Requested-With', 'XMLHttpRequest');
    const xhr = req.clone({
      headers: httpHeaders,
      url: (environment.backendUrl + req.url)
    });
    return next.handle(xhr).pipe(tap(() => { },
      (err: any) => {
        if (err instanceof HttpErrorResponse && !jwtAuthorization) {
          //this.router.navigate(['home']);
        }
      }));
  }
}
