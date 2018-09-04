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
	selector: 'app-configuracion',
	templateUrl: './configuracion.component.html',
	styleUrls: ['./configuracion.component.css'],
	providers: [EstadoESP8266Service, EventBusService, PlacaService] // Lo inyectamos dentro de nuestro componente
})
export class ConfiguracionComponent implements OnInit, DoCheck {
	public titulo: string;
	public estadoESP8266: EstadoESP8266;
	public identity;
	public bandera;
	public user;
	public esp;
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
	public imagenTemperatura: string;
	public imagenLuminosidad: string;
	public imagenHumedad: string;
	public mostrar: boolean;
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
		this.mostrar = false;
		this.bandera = false;
		this.rojo = '#DB3236';
		this.verde = '#34A853';
		this.urlWS = GLOBAL.urlWebSocket;
		this.vista_configuracion = false;
		this.imagenTemperatura = 'fas fa-thermometer-three-quarters fa-5x';
		this.imagenLuminosidad = 'far fa-sun fa-5x';
		this.imagenHumedad = 'fas fa-tint fa-5x';
		this.placa = new Placa('', '', '');
		this.datoSensor = new DatoSensor('', '', '', 0, 0, (new Date).getDay(), (new Date).getMonth(), (new Date).getFullYear());
	}
	ngOnInit(){
		this.identity = this._userService.getIdentity();
		this.user = this.identity;
		this.esp = this._estadoESP8266Service.getIdentity();
		console.log(this.identity.permiso);
		this.getEstadoESP8266eIniciarManejadores();
		if (this.identity == null){
		  this._router.navigate(['/login']);
		}
		console.log(this.identity.permiso);
		this.bandera = true;
	  }
	
	  // Cada vez que se haga algun cambio en la app a nivel de los componentes, se
	  // deben actualizar los datos del storage. Necesito implementar onInit y Docheck
	  ngDoCheck(){
		this.identity = this._userService.getIdentity();
		this.esp = this._estadoESP8266Service.getIdentity();
		localStorage.setItem('identity', JSON.stringify(this.identity));
	
	  }

	getEstadoESP8266eIniciarManejadores() {
		this._estadoESP8266Service.getEstado(this.identity.permiso).subscribe(
			result => {
				this.estadoESP8266 = result;
				console.log(result);
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
				this.registrarManejadores();
			},
			error => {
				let errorMessage = <any>error;
				console.log('errorrrrr');
			}
		);
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
			
			},
			error => {
				let errorMessage = <any>error;
				//console.log(errorMessage);
			}
		);
	}

	registrarManejadores() {
		this._eventBusService.connect(this.urlWS);
		let identificador = this.estadoESP8266.identificador;

		this._eventBusService.registerHandler(identificador + '/cambioConfiguracion', function (error, message) {	
			this.getEstadoESP8266();
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
	enviarAutomatico(n: number) {
		let identity = this.identity;
		this._estadoESP8266Service.enviarAutomatico(n, identity).subscribe(
			response => {
				console.log(response);
			}, error => {
				console.log(error);
			}
		);
	}

	enviarManual(n: number) {
		let identity = this.identity;
		this._estadoESP8266Service.enviarManual(n, identity).subscribe(
			response => {
				console.log(response);
			}, error => {
				console.log(error);
			}
		);
	}
	enviarActivado(n: number) {
		let identity = this.identity;
		this._estadoESP8266Service.enviarActivado(n, identity).subscribe(
			response => {
				console.log(response);
			}, error => {
				console.log(error);
			}
		);
	}
	enviarDesactivado(n: number) {
		let identity = this.identity;
		this._estadoESP8266Service.enviarDesactivado(n, identity).subscribe(
			response => {
				console.log(response);
			}, error => {
				console.log(error);
			}
		);
	}
	enviarMayorUmbral(n: number) {
		let identity = this.identity;
		this._estadoESP8266Service.enviarMayorUmbral(n, identity).subscribe(
			response => {
				console.log(response);
			}, error => {
				console.log(error);
			}
		);
	}
	enviarMenorUmbral(n: number) {
		let identity = this.identity;
		this._estadoESP8266Service.enviarMenorUmbral(n, identity).subscribe(
			response => {
				console.log(response);
			}, error => {
				console.log(error);
			}
		);
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

	enviarTexto(n: number, value: string) {
		let identity = this.identity;
		this._estadoESP8266Service.enviarTexto(n, value, identity).subscribe(
			response => {
				console.log(response);
			}, error => {
				console.log(error);
			}
		);
	}
	enviarUmbral(n: number, valor: number) {
		let identity = this.identity;
		this._estadoESP8266Service.enviarUmbral(n, valor, identity).subscribe(
			response => {
				console.log(response);
			}, error => {
				console.log(error);
			}
		);
	}
	enviarPulsador1(texto: string) {
		let identity = this.identity;
		this._estadoESP8266Service.enviarPulsador1(texto, identity).subscribe(
			response => {
				console.log(response);
			}, error => {
				console.log(error);
			}
		);
	}
	enviarPulsador2(texto:string) {
		let identity = this.identity;
		this._estadoESP8266Service.enviarPulsador2(texto, identity).subscribe(
			response => {
				console.log(response);
			}, error => {
				console.log(error);
			}
		);
	}
	
	eliminarDispositivo(){
		this._estadoESP8266Service.eliminarDispositivo(this.identity, this.estadoESP8266._id).subscribe(
			response => {
				this.identity = this._userService.getIdentity();
				console.log(response);
				localStorage.setItem('identity', JSON.stringify(this.identity));
				this._router.navigate(['/configuracion']);
			}, error => {
				console.log(error);
			}
		);
	}

	onSubmit(form) {
		let usuario = this.identity;
		this._placaService.register(this.placa, usuario).subscribe(
		  response => {
			  if (response) {
				if (!response.username && (response.lenght <= 0)) {
					this.status = 'error';
				  }
				  console.log(response);
				  this.status = 'success';
				  this.identity = response;
				  localStorage.setItem('identity', JSON.stringify(this.identity));
				  window.alert('Enhorabuena, ahora tienes acceso al dispositivo');
			  }
		  },
		  error => {
			this.status = 'error';
			window.alert('no valido');
		  }
		);
	  }
}
