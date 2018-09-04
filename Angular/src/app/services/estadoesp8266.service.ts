import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
// import * as $ from 'jquery';

import { EventBusService } from './event-bus.service';
import { Gpio} from '../models/gpio';
import { EstadoESP8266 } from '../models/estadoESP8266';
import { GLOBAL } from './global';

@Injectable()
export class EstadoESP8266Service {
    public urlApiRest: string;
    public urlWS: string;
    public gpios: Gpio[];
    public estadoESP8266: EstadoESP8266;
    public esp;
    constructor(
      public _http: HttpClient,
      private _eventBusService: EventBusService
    ) {
        this.urlApiRest = GLOBAL.urlAPIREST;
        this.urlWS = GLOBAL.urlWebSocket;
    }

    pruebaEvent() {
        this._eventBusService.connect(this.urlWS);
    }

    getEstado(dispositivo: string): Observable<any> {
        console.log(this.urlApiRest + '/api/esp/esp8266/' + dispositivo);
        return this._http.get(this.urlApiRest + '/api/esp/esp8266/' + dispositivo);   
    }

    getIdentity(){
        // Para parsear un jsonString a un objeto de JavaScript
        let esp = JSON.parse(localStorage.getItem('esp'));
        if (esp != 'undefined'){
         this.esp = esp;
        }else{
          this.esp = null;
        }
        return this.esp;
      }

    enviarAutomatico(n: number, identity: any): Observable<any> {
        let identificadorSerieId = identity.permiso;
        let username = identity.username;
        let password = identity.password;
        let params = JSON.stringify({
            username: identity.username,
            password: identity.password,
        });
        let headers = new HttpHeaders().set('Content-Type', 'application/json');
        return this._http.post(this.urlApiRest + '/api/esp/accion/automatico/' + 
                            identificadorSerieId + '/' + n + '/true', params, {headers: headers});
            
    }

    enviarManual(n: number, identity: any): Observable<any> {
        let username = identity.username;
        let password = identity.password;
        let identificadorSerieId = identity.permiso;
        let params = JSON.stringify({
            username: identity.username,
            password: identity.password,
        });
        let headers = new HttpHeaders().set('Content-Type', 'application/json');
        return this._http.post(this.urlApiRest + '/api/esp/accion/automatico/' + 
                            identificadorSerieId + '/' + n + '/false', params, {headers: headers});
            
    }

    enviarActivado(n: number, identity: any): Observable<any> {
        let username = identity.username;
        let password = 'user1';
        let identificadorSerieId = identity.permiso;
        let params = JSON.stringify({
            username: identity.username,
            password: identity.password,
        });
        let headers = new HttpHeaders().set('Content-Type', 'application/json');
        return this._http.post(this.urlApiRest + '/api/esp/accion/activado/' + 
        identificadorSerieId + '/' + n + '/true', params, {headers: headers});        
    }

    enviarDesactivado(n: number, identity: any): Observable<any> {
        let identificadorSerieId = identity.permiso;
        let username = identity.username;
        let password = identity.password;
        let params = JSON.stringify({
            username: identity.username,
            password: identity.password,
        });
        let headers = new HttpHeaders().set('Content-Type', 'application/json');

        return this._http.post(this.urlApiRest + '/api/esp/accion/activado/' + 
        identificadorSerieId + '/' + n + '/false', params, {headers: headers});     
    }

    enviarMayorUmbral(n: number, identity: any): Observable<any> {
        let identificadorSerieId = identity.permiso;
        let username = identity.username;
        let password = identity.password;
        let params = JSON.stringify({
            username: identity.username,
            password: identity.password,
        });
        let headers = new HttpHeaders().set('Content-Type', 'application/json');
        return this._http.post(this.urlApiRest + '/api/esp/accion/sentido/' + 
                                identificadorSerieId + '/' + n + '/true', params, {headers: headers});
            
    }
    enviarMenorUmbral(n: number, identity: any): Observable<any> {
        let identificadorSerieId = identity.permiso;
        let username = identity.username;
        let password = identity.password;
        let params = JSON.stringify({
            username: identity.username,
            password: identity.password,
        });
        let headers = new HttpHeaders().set('Content-Type', 'application/json');
        return this._http.post(this.urlApiRest + '/api/esp/accion/sentido/' + 
                                identificadorSerieId + '/' + n + '/false', params, {headers: headers});
            
    }
    enviarEncenderOApagar(n: number, identity: any): Observable<any> {
        let identificadorSerieId = identity.permiso;
        let username = identity.username;
        let password = identity.password;
        let params = JSON.stringify({
            username: identity.username,
            password: identity.password,
        });
        let headers = new HttpHeaders().set('Content-Type', 'application/json');
        return this._http.post(this.urlApiRest + '/api/esp/accion/estado/' + identificadorSerieId + '/' + n, params, {headers: headers});
        
         }

    enviarTexto(n: number, value: string, identity: any ): Observable<any> {

        let identificadorSerieId = identity.permiso;
        let username = identity.username;
        let password = identity.password;
        let params = JSON.stringify({
            username: identity.username,
            password: identity.password,
        });
        let headers = new HttpHeaders().set('Content-Type', 'application/json');
        return this._http.post(this.urlApiRest + '/api/esp/accion/nombre/' 
                    + identificadorSerieId + '/' + n + '/' + value, params, {headers: headers});
    }

    enviarPulsador1(value: string, identity: any ): Observable<any> {

        let identificadorSerieId = identity.permiso;
        let username = identity.username;
        let password = identity.password;
        let params = JSON.stringify({
            username: identity.username,
            password: identity.password,
        });
        let headers = new HttpHeaders().set('Content-Type', 'application/json');
        return this._http.post(this.urlApiRest + '/api/esp/accion/pulsador1/' 
                    + identificadorSerieId + '/' + value, params, {headers: headers});
    }

    enviarPulsador2(value: string, identity: any ): Observable<any> {

        let identificadorSerieId = identity.permiso;
        let username = identity.username;
        let password = identity.password;
        let params = JSON.stringify({
            username: identity.username,
            password: identity.password,
        });
        let headers = new HttpHeaders().set('Content-Type', 'application/json');
        return this._http.post(this.urlApiRest + '/api/esp/accion/pulsador2/' 
                    + identificadorSerieId +  '/' + value, params, {headers: headers});
    }
    enviarUmbral(n: number, value: number, identity: any): Observable<any> {

        let identificadorSerieId = identity.permiso;
        let username = identity.username;
        let password = identity.password;
        let params = JSON.stringify({
            username: identity.username,
            password: identity.password,
        });
        let headers = new HttpHeaders().set('Content-Type', 'application/json');
        return this._http.post(this.urlApiRest + '/api/esp/accion/umbral/' + 
                            identificadorSerieId + '/' + n + '/' + value, params, {headers: headers});
    }


    eliminarDispositivo(identity: any, estadoESP_id: any): Observable<any>{
        let identificadorSerieId = identity.permiso;
        let identificadorBD_id = estadoESP_id;
        let username = identity.username;
        let password = identity.password;
        let params = JSON.stringify({
            username: identity.username,
            password: identity.password,
            identificadorBD_id: identificadorSerieId
        });
        let headers = new HttpHeaders().set('Content-Type', 'application/json');
        return this._http.post(this.urlApiRest + '/api/esp/esp8266/eliminar/' + identificadorSerieId
        , params, {headers: headers});
    }

    getSensores(identity: any): Observable<any>{
        let identificadorSerieId = identity.permiso;
        let username = identity.username;
        let password = identity.password;
        let params = JSON.stringify({
            username: identity.username,
            password: identity.password,
        });
        let headers = new HttpHeaders().set('Content-Type', 'application/json');

       return this._http.post(this.urlApiRest + '/api/datosSensores/' + 
                            identificadorSerieId, params, {headers: headers});
    }

    borrarDatosSensores(identity: any): Observable<any>{
        let identificadorSerieId = identity.permiso;
        let username = identity.username;
        let password = identity.password;
        let params = JSON.stringify({
            username: identity.username,
            password: identity.password,
        });
        let headers = new HttpHeaders().set('Content-Type', 'application/json');

       return this._http.post(this.urlApiRest + '/api/datosSensores/borrar/' + 
                            identificadorSerieId, params, {headers: headers});
    }

}
