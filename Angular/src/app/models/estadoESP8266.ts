import { Gpio } from './gpio';

export class EstadoESP8266 {
	constructor(
public _id: string,
public identificador: string,
public tipoAnalogico: string,
public pulsador1: string,
public pulsador2: string,
public gpio1: Gpio,
public gpio2: Gpio,
public gpio3: Gpio,
public gpio4: Gpio,
public gpio5: Gpio,
public gpio6: Gpio,
public gpio7: Gpio,
public gpios: Gpio[]
) {}
}
