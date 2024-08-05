import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationDTO } from './authenticationdto';
import  { AuthService} from '../auth.service';
import { response } from 'express';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  
  login : string;
  password : string;
  loginError: boolean;
  novoUsuario : boolean;
  mensagemSucesso : string;

  constructor(private router      : Router,
              private authService : AuthService) {

  }

  onSubmit() {
    this.router.navigate(['/home']);
  }

  preparaCadastro(event : MouseEvent)
  { event.preventDefault();
    this.novoUsuario = true;
  }
  
  cancelaCadastro() {
    this.novoUsuario = false;
  }

  cadastrar() {
    const  authenticationDTO : AuthenticationDTO = new AuthenticationDTO();
    authenticationDTO.login    = this.login;
    authenticationDTO.password = this.password;
    this.authService
      .salvar(authenticationDTO)
      .subscribe(response => {
        this.mensagemSucesso = "Usuario criado com sucesso";
        this.loginError = false;
      }, error => {
        this.loginError = true;
        this.mensagemSucesso = "";
      })
  }

}
