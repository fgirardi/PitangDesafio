import { Component, OnInit } from '@angular/core';
import { PersonDTO } from '../clientes';
import { ClientesService } from '../clientes.service';
import { Router, ActivatedRoute } from '@angular/router';


@Component({
  selector: 'clientes-form',
  templateUrl: './clientes-form.component.html',
  styleUrl: './clientes-form.component.css'
})
export class ClientesFormComponent implements OnInit {
    
    personDTO       : PersonDTO;
    requisicaoComSucesso : boolean = false;
    erros : String[];
    id : number;
  
    constructor(private clientesService : ClientesService,
                private router          : Router,
                private activatedRoute  : ActivatedRoute
    ) {
      this.personDTO = new PersonDTO();
      this.erros     = [];
      this.id        = 0;
    }

    ngOnInit(): void {
      
      this.id = this.activatedRoute.snapshot.params['id'];
      console.log(this.id);
      
      if (this.id) {
        this.clientesService
          .getClientesById(this.id)
          .subscribe(
            response => this.personDTO = response,
            errorResponse => this.personDTO = new PersonDTO()
          )
      }
    
    }
    
    voltarParaListagem() {
      this.router.navigate(['/clientes/lista'])
    }
    
    onSubmit(){
      if (this.id) {

        this.clientesService.atualizar(this.personDTO)
            .subscribe(response => {
              this.requisicaoComSucesso = true;
              this.personDTO = response;
              this.erros = [];
              console.log(response);
            }, errorResponse => {
              console.log(errorResponse);
              this.requisicaoComSucesso = false;
              this.erros = errorResponse.error.messages;
            });

      } else {
            this.clientesService.salvar(this.personDTO)
            .subscribe(response => {
              this.requisicaoComSucesso = true;
              this.personDTO = response;
              this.erros = [];
              console.log(response);
            }, errorResponse => {
              console.log(errorResponse);
              this.requisicaoComSucesso = false;
              this.erros = errorResponse.error.messages;
            });
      }
    }
}
