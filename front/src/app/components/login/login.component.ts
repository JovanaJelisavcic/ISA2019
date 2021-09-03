import {Component, OnInit, ViewChild} from '@angular/core';
import {UserModel} from "../../model/user.model";
import {UserInfoModel} from "../../model/user-info.model";
import {HttpClient} from "@angular/common/http";
import {MatTabGroup} from "@angular/material/tabs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public loginUser: UserModel = new UserModel();

  public registerUser: UserInfoModel = new UserInfoModel();

  constructor(private http : HttpClient, private router : Router) { }

  // @ts-ignore
  @ViewChild('loginTabs')  loginTabs : MatTabGroup;

  ngOnInit(): void {
  }

  login() {
    this.http.post('register/signin', this.loginUser).subscribe(
      (response) => {
        console.log(response);
        // @ts-ignore
        sessionStorage.setItem('Authorization', 'Bearer ' + response.accessToken);
        // @ts-ignore
        sessionStorage.setItem('UserRoles', response.roles);
        // @ts-ignore
        sessionStorage.setItem('UserName', response.username);

        this.router.navigate(['/dashboard']);
      },
      (error) => {
        console.log(error);
      }
    );
  }

  register() {
    this.http.post('register/signup', this.registerUser).subscribe(
      (response) => {
        alert("Please verify your account on provided email address: " + this.registerUser.email);
        this.loginTabs.selectedIndex = 0;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  isRegisterDisabled() {
    return !this.registerUser.surname || !this.registerUser.email || !this.registerUser.name ||
      !this.registerUser.adress || !this.registerUser.city || !this.registerUser.state ||
      !this.registerUser.phoneNum || !this.registerUser.password;
  }

  onChangeTab() {
    this.loginUser = new UserModel();
    this.registerUser = new UserInfoModel();
  }
}
