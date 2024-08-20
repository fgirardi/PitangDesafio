import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserDTO } from './userDTO';
import  { AuthService} from '../auth.service';
import { response } from 'express';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  
  email : string;
  password : string;
  loginError: boolean;
  novoUsuario : boolean;
  mensagem : string;
  errors : String[];

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
    const  userDTO : UserDTO = new UserDTO();
    userDTO.email    = this.email;
    userDTO.password = this.password;
    console.log(userDTO);
    this.authService
      .salvar(userDTO)
      .subscribe(response => {
        this.mensagem = "Usuario criado com sucesso";
        this.loginError = false;
        this.novoUsuario = false;
        this.email = '';
        this.password = '';
        this.errors = [];
      }, errorResponse => {
        this.loginError = true;
        this.errors = errorResponse.error.messages;
      })
  }

}
