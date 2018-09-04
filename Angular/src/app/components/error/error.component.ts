import { Component, OnInit } from '@angular/core';

@Component({
	selector: 'error',
	templateUrl: './error.html'
})
export class ErrorComponent implements OnInit {
	public titulo: string;

	constructor() {
		this.titulo = 'Error!! Página no encontrada';
	}

	ngOnInit(){
		console.log('Componente error.component.ts cargado');
	}
}