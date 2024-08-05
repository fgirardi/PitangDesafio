import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from './environment/environment';
import { AuthenticationDTO } from './login/authenticationdto';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  apiURL : string = environment.apiURLBase + "/api/usuarios"

  constructor(private http : HttpClient) { }

  salvar(authenticationDTO : AuthenticationDTO) : Observable<any> {
      return this.http.post<any>(this.apiURL, authenticationDTO);
  }

}
