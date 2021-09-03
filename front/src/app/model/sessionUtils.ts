export class SessionUtils {
  static getUsername() {
    return sessionStorage.getItem('UserName');
  }

  static getRoles() {
    return sessionStorage.getItem('UserRoles');
  }
}
