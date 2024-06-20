import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from './environment/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  apiURL : string = environment.apiURLBase + "/api/usuarios"

  constructor(private http : HttpClient) { }

  salvar() {}

}
