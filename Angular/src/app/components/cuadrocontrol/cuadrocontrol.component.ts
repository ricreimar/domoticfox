import { UserService } from '../../services/user.service';
import { PlacaService } from '../../services/placa.service';
import { Component, OnInit, DoCheck } from '@angular/core';
import {Observable} from 'rxjs/Observable';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { EventBusService } from '../../services/event-bus.service';
import { EstadoESP8266 } from '../../models/estadoESP8266';
import { EstadoESP8266Service } from '../../services/estadoesp8266.service';
import { Gpio } from '../../models/gpio';
import { GLOBAL } from '../../services/global';
import {Placa } from '../../models/placa';
import { DatoSensor } from '../../models/datoSensor';
import { User } from '../../models/user';
import $ from 'jquery';

@Component({
	selector: 'app-cuadrocontrol',
	templateUrl: './cuadrocontrol.html',
	styleUrls: ['./cuadrocontrol.css'],
	providers: [EstadoESP8266Service, EventBusService, PlacaService] // Lo inyectamos dentro de nuestro componente
})
export class CuadrocontrolComponent implements OnInit, DoCheck {
	public titulo: string;
	public estadoESP8266: EstadoESP8266;
	public identity;
	public gpios;
	public gpio1;
	public gpio2;
	public gpio3;
	public gpio4;
	public gpio5;
	public gpio6;
	public gpio7;
	public urlWS;
	public placa: Placa;
	public rojo: string;
	public verde: string;
	public datoSensor: DatoSensor;
	public claseSensor: string;
	public status;
	public componenteCargado;
	//public useRed: string;
	public imagenTemperatura: string;
	public imagenLuminosidad: string;
	public imagenHumedad: string;
	public mostrar: boolean;
	//public configuracion: boolean;
	public vista_configuracion: boolean;
	public timerWS;
	
	constructor(
		private _route: ActivatedRoute,
		private _router: Router,
		private _estadoESP8266Service: EstadoESP8266Service,
		private _eventBusService: EventBusService,
		private _userService: UserService,
		private _placaService: PlacaService
	) {
		this.titulo = 'Cuadro de control';
		//this.gpios = new Array();
		this.mostrar = false;
		//this.useRed = 'color: red';
		this.rojo = '#DB3236';
		this.verde = '#34A853';
		this.gpios = {};
		this.urlWS = GLOBAL.urlWebSocket;
		this.vista_configuracion = false;
		this.componenteCargado = false;
		this.imagenTemperatura = 'fas fa-thermometer-three-quarters fa-5x';
		this.imagenLuminosidad = 'far fa-sun fa-5x';
		this.imagenHumedad = 'fas fa-tint fa-5x';
		this.placa = new Placa('', '', '');
		this.datoSensor = new DatoSensor('', '', '', 0, 0, (new Date).getDay(), (new Date).getMonth(), (new Date).getFullYear());
	}
	ngOnInit(){
		this.identity = this._userService.getIdentity();
		this.getEstadoESP8266();
		this.funcionTimeOutWS();
		if (this.identity == null){
		  this._router.navigate(['/login']);
		}

	  }
	
	  // Cada vez que se haga algun cambio en la app a nivel de los componentes, se
	  // deben actualizar los datos del storage. Necesito implementar onInit y Docheck
	  ngDoCheck(){
		this.identity = this._userService.getIdentity();
	  }

	funcionTimeOutWS(){
		this.timerWS = setTimeout(
			() => this.mostrar = true, 10000);
	}

	getEstadoESP8266() {
		this._estadoESP8266Service.getEstado(this.identity.permiso).subscribe(
			result => {
				this.estadoESP8266 = result;
				this.gpio1 = (this.estadoESP8266.gpio1);
				this.gpio2 = (this.estadoESP8266.gpio2);
				this.gpio3 = (this.estadoESP8266.gpio3);
				this.gpio4 = (this.estadoESP8266.gpio4);
				this.gpio5 = (this.estadoESP8266.gpio5);
				this.gpio6 = (this.estadoESP8266.gpio6);
				this.gpio7 = (this.estadoESP8266.gpio7);
				this.gpio1.colorEstado = (this.estadoESP8266.gpio1.estado ? this.verde : this.rojo);
				this.gpio2.colorEstado = (this.estadoESP8266.gpio2.estado ? this.verde : this.rojo);
				this.gpio3.colorEstado = (this.estadoESP8266.gpio3.estado ? this.verde : this.rojo);
				this.gpio4.colorEstado = (this.estadoESP8266.gpio4.estado ? this.verde : this.rojo);
				this.gpio5.colorEstado = (this.estadoESP8266.gpio5.estado ? this.verde : this.rojo);
				this.gpio6.colorEstado = (this.estadoESP8266.gpio6.estado ? this.verde : this.rojo);
				this.gpio7.colorEstado = (this.estadoESP8266.gpio7.estado ? this.verde : this.rojo);

				if (this.estadoESP8266.tipoAnalogico == 'Temperatura'){
					this.claseSensor = this.imagenTemperatura;
				}else if (this.estadoESP8266.tipoAnalogico == 'Luminosidad'){
					this.claseSensor = this.imagenLuminosidad;
				}else if (this.estadoESP8266.tipoAnalogico == 'Humedad'){
					this.claseSensor = this.imagenHumedad;
				}
				
				if (!this.estadoESP8266) {
					console.log('Error en el servidor');
				}
				if ( !this.componenteCargado ){
					this.registrarManejadores();
				}
				this.componenteCargado = true;
				
			},
			error => {
				let errorMessage = <any>error;
				console.log(errorMessage);
			}
		);

	}

	registrarManejadores() {

		// Cada vez que haya un cambio, manejador
		this._eventBusService.connect(this.urlWS);
		let identificador = this.estadoESP8266.identificador;
console.log(identificador+'/valor');
		this._eventBusService.registerHandler(identificador + '/valor', function (error, message) {
			this.mostrar = false;
			clearTimeout(this.timerWS);
			this.funcionTimeOutWS();
			console.log(message.body);
			this.datoSensor.valor = message.body;
			this.contadorWS = 0;
		}.bind(this));

		this._eventBusService.registerHandler(identificador + '/pulsador1', function (error, message) {
			if(message.body == '1'){
				window.alert(this.estadoESP8266.pulsador1);
			}
		}.bind(this));
		this._eventBusService.registerHandler(identificador + '/pulsador2', function (error, message) {
			if(message.body == '1'){
				window.alert(this.estadoESP8266.pulsador2);
			}
		}.bind(this));
		this._eventBusService.registerHandler(identificador + '/cambioConfiguracion', function (error, message) {
			this.getEstadoESP8266();
		}.bind(this));

		// GPIO 1
		this._eventBusService.registerHandler(identificador + '/1/estado', function (error, message) {
			this.gpio1.colorEstado = (message.body == 'true' ? this.verde : this.rojo);
			this.gpio1.estado = (message.body == 'true');
		}.bind(this));
		this._eventBusService.registerHandler(identificador + '/2/estado', function (error, message) {
			this.gpio2.colorEstado = (message.body == 'true' ? this.verde : this.rojo);
			this.gpio2.estado = (message.body == 'true');
		}.bind(this));
		this._eventBusService.registerHandler(identificador + '/3/estado', function (error, message) {
			this.gpio3.colorEstado = (message.body == 'true' ? this.verde : this.rojo);
			this.gpio3.estado = (message.body == 'true');
		}.bind(this));
		this._eventBusService.registerHandler(identificador + '/4/estado', function (error, message) {
			this.gpio4.colorEstado = (message.body == 'true' ? this.verde : this.rojo);
			this.gpio4.estado = (message.body == 'true');
		}.bind(this));
		this._eventBusService.registerHandler(identificador + '/5/estado', function (error, message) {
			this.gpio5.colorEstado = (message.body == 'true' ? this.verde : this.rojo);
			this.gpio5.estado = (message.body == 'true');
		}.bind(this));
		this._eventBusService.registerHandler(identificador + '/6/estado', function (error, message) {
			this.gpio6.colorEstado = (message.body == 'true' ? this.verde : this.rojo);
			this.gpio6.estado = (message.body == 'true');
		}.bind(this));
		this._eventBusService.registerHandler(identificador + '/7/estado', function (error, message) {
			this.gpio7.colorEstado = (message.body == 'true' ? this.verde : this.rojo);
			this.gpio7.estado = (message.body == 'true');
		}.bind(this));
	
		
	}

	cambiarVistaConf(entrada: boolean) {
		this.vista_configuracion = entrada;
	}
	
	enviarEncenderOApagar(n: number) {
		let identity = this.identity;
		this._estadoESP8266Service.enviarEncenderOApagar(n, identity).subscribe(
			response => {
				console.log(response);
			}, error => {
				console.log(error);
			}
		);
	}

	onSubmit(form) {
		let usuario = this.identity;
		this._placaService.register(this.placa, usuario).subscribe(
		  response => {
			console.log(response.permiso);
			  if (response && response.permiso) {
				console.log(response);
				this.status = 'success';
				this.identity = response;

				// Persistir datos del usuario. Usaremos el localStorage para guardar la información
				// y poder acceder a ella en cualquier componente

				localStorage.setItem('identity', JSON.stringify(this.identity));
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
