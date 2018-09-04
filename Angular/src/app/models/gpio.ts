export class Gpio{
	constructor(
		public numero: number,
		public nombre: string,
		public activado: string,
		public estado: string,
		public automatico: string,
		public umbral: number,
		public sentido: string
	) {}
}
