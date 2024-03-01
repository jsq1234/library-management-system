import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserInfo } from '../interfaces/user-info';
import { Observable } from 'rxjs';
import { SignupRequest } from '../interfaces/signup-request';
import { LoginRequest } from '../interfaces/login-request';
import { Router } from '@angular/router';
import { EmailLoginReq } from '../interfaces/email-login-req';
import { PhonenoLoginReq } from '../interfaces/phoneno-login-req';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private authenticatedUser: UserInfo | null = null;
  private baseUrl = 'http://localhost:8081/api/auth';
  constructor(private http: HttpClient, private router: Router) {}

  public loginByEmail(requestBody: EmailLoginReq): Observable<UserInfo> {
    return this.http.post<UserInfo>(`${this.baseUrl}/login`, requestBody);
  }
  public loginByPhoneNo(requestBody: PhonenoLoginReq): Observable<UserInfo> {
    return this.http.post<UserInfo>(`${this.baseUrl}/login`, requestBody);
  }

  public signUp(requestBody: SignupRequest): Observable<UserInfo> {
    return this.http.post<UserInfo>(`${this.baseUrl}/signup`, requestBody);
  }

  public logout() {
    this.authenticatedUser = null;
    localStorage.removeItem('user');
    this.router.navigate(['login']);
  }

  get user(): UserInfo | null {
    return this.authenticatedUser;
  }

  set user(userInfo: UserInfo) {
    this.authenticatedUser = userInfo;
  }
}
