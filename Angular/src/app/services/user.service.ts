import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import { User } from '../models/user';
import {GLOBAL} from './global';

@Injectable()
export class UserService {
  public url: string;
  public identity;

  constructor(public _http: HttpClient) {
    this.url = GLOBAL.urlAPIREST;

   }

   register(user: User): Observable<any> {
      let params = JSON.stringify(user);
      let headers = new HttpHeaders().set('Content-Type', 'application/json');

      return this._http.post(this.url +'/api/usuario/register', params, {headers: headers});
   }

   signup(user: User): Observable<any> {
      let params = JSON.stringify(user);
      let headers = new HttpHeaders().set('Content-Type', 'application/json');
      let username = user.username;
      let password = user.password;
      let cadena = '' + username + ':' + password;
      
      let auth = btoa(cadena);

      return this._http.post(this.url + '/api/usuario/login', params, {headers: headers});
   }

   getIdentity(){
     // Para parsear un jsonString a un objeto de JavaScript
     let identity = JSON.parse(localStorage.getItem('identity'));
     if (identity != 'undefined'){
      this.identity = identity;
     }else{
       this.identity = null;
     }
     return this.identity;
   }
}
