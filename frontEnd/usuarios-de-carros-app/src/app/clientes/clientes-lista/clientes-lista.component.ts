import { Component } from '@angular/core';
import { PersonDTO } from '../clientes';
import { ClientesService } from '../clientes.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-clientes-lista',
  templateUrl: './clientes-lista.component.html',
  styleUrl: './clientes-lista.component.css'
})
export class ClientesListaComponent {

  personsDTO : PersonDTO[];
  personSelecionado : PersonDTO; 
  mensagemSucesso : string;
  mensagemErro : string;
  firstNameSearch : string;

  constructor(private clientesService : ClientesService, 
              private router          : Router) {
    this.personsDTO = [];
    this.personSelecionado = new PersonDTO();
    this.mensagemSucesso = ""; 
    this.mensagemErro = "";
    this.firstNameSearch = "";
  }

  ngOnInit(): void {
    
    this.clientesService
    .getClientes()
    .subscribe(resposta => this.personsDTO = resposta);
  
  }

  novoCadastro() {
    this.router.navigate(['/clientes/form'])
  }

  prepararDelecao(personDTO : PersonDTO) {
    this.personSelecionado =  personDTO; 
  }

  deletarCliente() {
    this.clientesService
    .deletar(this.personSelecionado)
    .subscribe(resposta => {this.mensagemSucesso = resposta; this.ngOnInit()},
              erro => this.mensagemErro = erro);
  }

  consultarPorNome(){
    this.clientesService.getClientesByFirstName(this.firstNameSearch)
    .subscribe(response => this.personsDTO = response);
    console.log(this.firstNameSearch);

  }

}
