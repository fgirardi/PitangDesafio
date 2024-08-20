import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from './environment/environment';
import { UserDTO } from './login/userDTO';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  apiURL : string = environment.apiURLBase + "/users"
  token : string = environment.apiURLBase + "/oauth/token"

  constructor(private http : HttpClient) { }

  salvar(userDTO : UserDTO) : Observable<any> {
      return this.http.post<any>(this.apiURL, userDTO);
  }

}
