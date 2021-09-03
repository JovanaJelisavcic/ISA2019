import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {SessionUtils} from "./model/sessionUtils";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'farmacia-front';

  constructor(private router: Router) {
  }

  getEmail() {
    return SessionUtils.getUsername();
  }

  loginLogOut() {
    if (SessionUtils.getUsername()) {
      this.router.navigate(['/home']);
      sessionStorage.clear();
    } else {
      this.router.navigate(['/register']);
    }
  }
}
