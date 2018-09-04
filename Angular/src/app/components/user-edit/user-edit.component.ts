import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params} from '@angular/router';
import { User } from '../../models/user';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css'],
  providers: [UserService]
})
export class UserEditComponent implements OnInit {
  public title: string;
  public user: User;
  public identity;
  public status: string;
  constructor(
    private _route: ActivatedRoute,
    private _router: Router,
    private _userService: UserService
  ) { 
    this.title = 'Actualizar';
    this.user = this._userService.getIdentity();
    this.identity = this.user;
  }

  ngOnInit() {
    console.log(this.user);
    console.log('Se ha cargado el user-edit.component');
  }

  onSubmit(){
    console.log(this.user);
  }
}
