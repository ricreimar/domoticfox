import { Component, OnInit, DoCheck } from '@angular/core';
import { EstadoESP8266Service } from '../../services/estadoesp8266.service';
import { DatoSensor } from '../../models/datoSensor';
import { UserService } from '../../services/user.service';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { EventBusService } from '../../services/event-bus.service';

@Component({
  selector: 'app-linea',
  templateUrl: './linea.component.html',
  providers: [EstadoESP8266Service, UserService, EventBusService]
})
export class LineaComponent implements OnInit, DoCheck {
  // Propiedades grafico
  public lineChartData: Array<any>;
  public lineChartData1: Array<any>;
  public lineChartData2: Array<any>;
  public lineChartLabels: Array<any>;
  public lineChartLabels1: Array<any>;
  public lineChartLabels2: Array<any>;
  public lineChartOptions: any;
  public lineChartColors: Array<any>;
  public lineChartLegend: boolean;
  public lineChartType: string;
  public datosMeses: any;
  public numeroDatosPorMes: number[];
  public sumatorioDatosPorMes: number[];

  // Propiedades relacionadas servicios
  public identity;
  public mediaValoresPorMeses: number[];
  public sensores: any;

  public componenteCargado;

  // Propiedades actualizacion grafico
  public bandera: boolean;
  
  constructor(
		private _route: ActivatedRoute,
		private _router: Router,
    private _estadoESP8266Service: EstadoESP8266Service,
    private _userService: UserService
  ){
    this.bandera = false;
    this.componenteCargado = false;
    this.datosMeses = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    this.numeroDatosPorMes = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    this.sumatorioDatosPorMes = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];

    
    this.mediaValoresPorMeses  = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    this.lineChartData1 = [ {data: this.mediaValoresPorMeses, label: 'Anual' } ];
    this.lineChartLabels1 = ['Enero', 'Febero', 
    'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 
    'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
    this.lineChartLabels2 = ['1', '2', 
    '3', '4', '5', '6', '7', 
    '8', '9', '10', '11', '12'];
    this.lineChartOptions = { responsive: true };
    this.lineChartColors = [
      { // grey
        backgroundColor: 'rgba(148,159,177,0.2)',
        borderColor: 'rgba(148,159,177,1)',
        pointBackgroundColor: 'rgba(148,159,177,1)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgba(148,159,177,0.8)'
      },
      { // dark grey
        backgroundColor: 'rgba(77,83,96,0.2)',
        borderColor: 'rgba(77,83,96,1)',
        pointBackgroundColor: 'rgba(77,83,96,1)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgba(77,83,96,1)'
      },
      { // grey
        backgroundColor: 'rgba(148,159,177,0.2)',
        borderColor: 'rgba(148,159,177,1)',
        pointBackgroundColor: 'rgba(148,159,177,1)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgba(148,159,177,0.8)'
      }
    ];
    this.lineChartLegend = true;
    this.lineChartType = 'line';

  }

  ngOnInit(){
    this.identity = this._userService.getIdentity();
    this.getDatosSensores();
  }

  ngDoCheck(){
		this.identity = this._userService.getIdentity();
    }

  public getDatosSensores(){
    let identity = this.identity;
    this._estadoESP8266Service.getSensores(identity).subscribe(
      response => {
        if (!response) {
          console.log(response);
        }
        this.sensores = response;
        for (let i = 0, len = this.sensores.length; i < len; ++i){

          let mes = Number(this.sensores[i].month);
          let valor = Number(this.sensores[i].valor);
          this.numeroDatosPorMes[mes - 1] = this.numeroDatosPorMes[mes - 1] + 1;
          this.sumatorioDatosPorMes[mes - 1] = this.sumatorioDatosPorMes[mes - 1] + valor;
          this.mediaValoresPorMeses = [0,0,0,0,0,0,0,0,0,0,0,0];

        }
        for (let i = 0; i < 12; i++){
          if (this.numeroDatosPorMes[i] > 0){
            this.mediaValoresPorMeses[i] = this.sumatorioDatosPorMes[i] / this.numeroDatosPorMes[i];
          }
          
        }
        //this.mediaValoresPorMeses  = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
        this.lineChartData1 = [ {data: this.mediaValoresPorMeses, label: 'Anual' } ];
        this.datosMeses = this.mediaValoresPorMeses;
        this.lineChartData1 = [ {data: this.datosMeses, label: 'Anual' } ];
        
        if (!this.componenteCargado){
          this.actualizar();
        }
        this.componenteCargado = true;
      },
      error => {
        console.log('error');
        let errorMessage = <any>error;
      }
    );

  }

  private actualizar() {
    this.getDatosSensores();
    let _lineChartData: Array<any> = new Array(this.lineChartData1.length);
    console.log(this.lineChartData1.length);
    for (let i = 0; i < this.lineChartData1.length; i++) {
      _lineChartData[i] = {data: new Array(this.lineChartData1[i].data.length), label: this.lineChartData1[i].label};
      console.log(this.lineChartData1[0].data.length);
      for (let j = 0; j < this.lineChartData1[i].data.length; j++) {
        _lineChartData[i].data[j] = this.datosMeses[j];
      }
    }
    this.lineChartData1 = _lineChartData;
    this.bandera = true;
    }

    public borrarDatos(){
      this._estadoESP8266Service.borrarDatosSensores(this.identity).subscribe(
        response => {
          console.log(response);
        },
        error => {
          console.log('error');
        }
      );
    }

  // events
  public chartClicked(e: any): void {
    console.log(e);
  }
 
  public chartHovered(e: any): void {
    console.log(e);
  }
}
