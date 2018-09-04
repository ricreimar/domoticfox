import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { User } from '../../models/user';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  providers: [UserService]
})
export class LoginComponent {
  public title: string;
  public user: User;
  public status: string;
  public identity; // objeto del usuario identificado
  constructor(
    private _route: ActivatedRoute,
    private _router: Router,
    private _userService: UserService
  ) {
    this.title = 'Login';
    this.user = new User('', '', '', '', '', '', '', '', '');
  }

  onSubmit(form) {
    // se logea al usuario
    this._userService.signup(this.user).subscribe(
      response => {
        //this.identity = response.username;
        if (!response.username && (response.lenght <= 0)) {
          this.status = 'error';
        }
        console.log(response);
        this.status = 'success';
        this.identity = response;
        // Persistir datos del usuario. Usaremos el localStorage para guardar la información
        // y poder acceder a ella en cualquier componente
        localStorage.setItem('identity', JSON.stringify(this.identity));
        // Conseguir el resto de datos del usuario
        form.reset();
        this._router.navigate(['/']);
      },
      error => {
        let errorMessage = <any>error;
        if (errorMessage != null) {
          this.status = 'error';
        }
      }
    );
  }
  
}
