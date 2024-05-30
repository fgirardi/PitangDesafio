import { Injectable } from '@angular/core';
import {PersonDTO} from './clientes';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClientesService {

  constructor(private httpClient: HttpClient) { }

  getCliente() : PersonDTO {
 
    let cliente : PersonDTO = new PersonDTO();
    return cliente;
    
  }

  salvar (personDTO : PersonDTO) : Observable<PersonDTO> {
     return this.httpClient.post<PersonDTO> ('http://localhost:8082/usuario-carros/api/person', personDTO);
  }

  atualizar (personDTO : PersonDTO) : Observable<any> {
    return this.httpClient.put<any> (`http://localhost:8082/usuario-carros/api/person/${personDTO.id}`, personDTO);
 }

  
  getClientes(): Observable<PersonDTO[]> {
    return this.httpClient.get<PersonDTO[]>('http://localhost:8082/usuario-carros/api/person');
  }

  getClientesById(id : number): Observable<PersonDTO> {
    return this.httpClient.get<PersonDTO>(`http://localhost:8082/usuario-carros/api/person/${id}`);
  }

  getClientesByFirstName(firstName : string): Observable<PersonDTO[]> {
    
    const httpParams = new HttpParams();
    httpParams.set("firstName", firstName);
    return this.httpClient.get<PersonDTO[]>("http://localhost:8082/usuario-carros/api/person?" + httpParams.toString());
  }
  
  deletar (personDTO : PersonDTO) : Observable<any> {
    return this.httpClient.delete<any> (`http://localhost:8082/usuario-carros/api/person/${personDTO.id}`);
 }
}
