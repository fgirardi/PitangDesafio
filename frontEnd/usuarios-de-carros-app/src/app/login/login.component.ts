import { Component } from '@angular/core';
import { Router } from '@angular/router';


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

  constructor(private router : Router) {

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

}
