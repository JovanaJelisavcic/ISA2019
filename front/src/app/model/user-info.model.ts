export class UserInfoModel {
  email : string;
  name : string;
  surname : string;
  adress : string;
  city : string;
  state : string;
  phoneNum : string;
  password : string;

  constructor() {
    this.email = '';
    this.name  = '';
    this.surname = '';
    this.adress = '';
    this.city = '';
    this.state = '';
    this.phoneNum = '';
    this.password = '';
  }
}
