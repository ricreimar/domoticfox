import { Component, OnInit, DoCheck } from '@angular/core';
import { Router, ActivatedRoute, Params} from '@angular/router';
import { UserService} from '../../services/user.service';

@Component({
  selector: 'app-mi-perfil',
  templateUrl: './mi-perfil.component.html'
})
export class MiPerfilComponent implements OnInit, DoCheck {
  public name: string;
  public email: string;
  public permiso: string;
  public rol: string;
  public surname: string;
  public identity;
  constructor(
    private _route: ActivatedRoute,
    private _router: Router,
    private _userService: UserService
  ) {
    this.identity = localStorage.getItem('identity');

    console.log(this.identity);
   }

   ngOnInit(){
    this.identity = this._userService.getIdentity();
  }

  // Cada vez que se haga algun cambio en la app a nivel de los componentes, se
  // deben actualizar los datos del storage. Necesito onIniit y Docheck
  ngDoCheck(){
    this.identity = this._userService.getIdentity();
  }

}
