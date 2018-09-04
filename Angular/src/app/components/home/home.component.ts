import { Component, OnInit, DoCheck } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { UserService} from '../../services/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  providers: [UserService]
})
export class HomeComponent implements OnInit, DoCheck {
  public title: string;
  public identity;

  constructor(
    private _route: ActivatedRoute,
    private _router: Router,
    private _userService: UserService
  ) {
    this.title = 'Bienvenido a DomoticFox';
   }

   ngOnInit(){
    this.identity = this._userService.getIdentity();
    if(this.identity == null){
      this._router.navigate(['/login']);
    }
  }

  // Cada vez que se haga algun cambio en la app a nivel de los componentes, se
  // deben actualizar los datos del storage. Necesito implementar onInit y Docheck
  ngDoCheck(){
    this.identity = this._userService.getIdentity();
  }
}
