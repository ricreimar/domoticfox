import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import { Placa } from '../models/placa';
import { User } from '../models/user';
import {GLOBAL} from './global';

@Injectable()
export class PlacaService {
  public url: string;
  public identity;

  constructor(public _http: HttpClient) {
    this.url = GLOBAL.urlAPIREST;
    
   }

   register(placa: Placa, user: any): Observable<any> {
    let params = JSON.stringify({
      passwordPlaca : placa.passwordPlaca,
      tipoAnalogico: placa.tipoAnalogico,
      password: user.password,
      username: user.username
    });
    
    let headers = new HttpHeaders().set('Content-Type', 'application/json');
    
    console.log(params);
    return this._http.post(this.url + '/api/esp/esp8266/' + placa.identificadorSerieId, params, {headers: headers});
 }
}
