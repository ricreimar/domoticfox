import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import {UserService} from '../../services/user.service';
import { User } from '../../models/user';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  providers: [UserService]
})
export class RegisterComponent implements OnInit {
  public title: string;
  public user: User;
  public cadena: String;
  public status;
  constructor(
    private _route: ActivatedRoute,
    private _router: Router,
    private _userService: UserService
  ) {
    this.title = 'Regístrate';
    this.user = new User('', '', '', '', '', '', '', '', '');
   }
      
  ngOnInit() {
  }
    
  onSubmit(form) {
    this._userService.register(this.user).subscribe(
      response => {
          if (response && response._id) {
              this.status = 'success';
              form.reset();
          }
      },
      error => {
        this.status = 'error';
      }
    );
  }

}
