package domotic.us.modelos;

import com.fasterxml.jackson.annotation.*;

public class EstadoESP8266 {
	
	private String identificador;
	private TipoAnalogico tipoAnalogico;
	private String pulsador1;
	private String pulsador2;
	private String _id;
	private Gpio gpio1;
	private Gpio gpio2;
	private Gpio gpio3;
	private Gpio gpio4;
	private Gpio gpio5;
	private Gpio gpio6;
	private Gpio gpio7;
	
	@JsonCreator
	public EstadoESP8266(@JsonProperty("identificador")String identificador,
			@JsonProperty("tipoAnalogico")TipoAnalogico tipoAnalogico,
			@JsonProperty("pulsador1")String pulsador1,
			@JsonProperty("pulsador2")String pulsador2) {
		this.identificador = identificador;
		this.tipoAnalogico = tipoAnalogico;
		this.pulsador1 = pulsador1;
		this.pulsador2 = pulsador2;
		this._id = new String();
		this.gpio1 = new Gpio(1);
		this.gpio2 = new Gpio(2);
		this.gpio3 = new Gpio(3);
		this.gpio4 = new Gpio(4);
		this.gpio5 = new Gpio(5);
		this.gpio6 = new Gpio(6);
		this.gpio7 = new Gpio(7);
	}
	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public TipoAnalogico getTipoAnalogico() {
		return tipoAnalogico;
	}

	public void setTipoAnalogico(TipoAnalogico tipoAnalogico) {
		this.tipoAnalogico = tipoAnalogico;
	}

	public Gpio getGpio(Integer n) {
		Gpio res;
		switch(n){
		case 1: res = gpio1; break;
		case 2: res = gpio2; break;
		case 3: res = gpio3; break;
		case 4: res = gpio4; break;
		case 5: res = gpio5; break;
		case 6: res = gpio6; break;
		case 7: res = gpio7; break;
		default: res = gpio1;
		}
		return res;
	}
	
	public void setGpio(Integer n, Boolean b) {
		switch(n){
		case 1: this.gpio1.setEstado(b); break;
		case 2: this.gpio2.setEstado(b); break;
		case 3: this.gpio3.setEstado(b); break;
		case 4: this.gpio4.setEstado(b); break;
		case 5: this.gpio5.setEstado(b); break;
		case 6: this.gpio6.setEstado(b); break;
		case 7: this.gpio7.setEstado(b); break;
		default: break;
		}
	}

	public Gpio getGpio1() {
		return gpio1;
	}

	public void setGpio1(Gpio gpio1) {
		this.gpio1 = gpio1;
	}

	public Gpio getGpio2() {
		return gpio2;
	}

	public void setGpio2(Gpio gpio2) {
		this.gpio2 = gpio2;
	}

	public Gpio getGpio3() {
		return gpio3;
	}

	public void setGpio3(Gpio gpio3) {
		this.gpio3 = gpio3;
	}

	public Gpio getGpio4() {
		return gpio4;
	}

	public void setGpio4(Gpio gpio4) {
		this.gpio4 = gpio4;
	}

	public Gpio getGpio5() {
		return gpio5;
	}

	public void setGpio5(Gpio gpio5) {
		this.gpio5 = gpio5;
	}

	public Gpio getGpio6() {
		return gpio6;
	}

	public void setGpio6(Gpio gpio6) {
		this.gpio6 = gpio6;
	}
	public Gpio getGpio7() {
		return gpio7;
	}

	public void setGpio7(Gpio gpio7) {
		this.gpio7 = gpio7;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gpio1 == null) ? 0 : gpio1.hashCode());
		result = prime * result + ((gpio2 == null) ? 0 : gpio2.hashCode());
		result = prime * result + ((gpio3 == null) ? 0 : gpio3.hashCode());
		result = prime * result + ((gpio4 == null) ? 0 : gpio4.hashCode());
		result = prime * result + ((gpio5 == null) ? 0 : gpio5.hashCode());
		result = prime * result + ((gpio6 == null) ? 0 : gpio6.hashCode());
		result = prime * result + ((gpio7 == null) ? 0 : gpio7.hashCode());
		result = prime * result + ((identificador == null) ? 0 : identificador.hashCode());
		result = prime * result + ((tipoAnalogico == null) ? 0 : tipoAnalogico.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EstadoESP8266 other = (EstadoESP8266) obj;
		if (gpio1 == null) {
			if (other.gpio1 != null)
				return false;
		} else if (!gpio1.equals(other.gpio1))
			return false;
		if (gpio2 == null) {
			if (other.gpio2 != null)
				return false;
		} else if (!gpio2.equals(other.gpio2))
			return false;
		if (gpio3 == null) {
			if (other.gpio3 != null)
				return false;
		} else if (!gpio3.equals(other.gpio3))
			return false;
		if (gpio4 == null) {
			if (other.gpio4 != null)
				return false;
		} else if (!gpio4.equals(other.gpio4))
			return false;
		if (gpio5 == null) {
			if (other.gpio5 != null)
				return false;
		} else if (!gpio5.equals(other.gpio5))
			return false;
		if (gpio6 == null) {
			if (other.gpio6 != null)
				return false;
		} else if (!gpio6.equals(other.gpio6))
			return false;
		if (identificador == null) {
			if (other.identificador != null)
				return false;
		} else if (!identificador.equals(other.identificador))
			return false;
		if (tipoAnalogico != other.tipoAnalogico)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EstadoESP8266 [identificador=" + identificador + ", tipoAnalogico=" + tipoAnalogico + ", gpio1=" + gpio1 + ", gpio2=" + gpio2 + ", gpio3=" + gpio3 + ", gpio4=" + gpio4 + ", gpio5=" + gpio5
				+ ", gpio6=" + gpio6 + ", gpio7="+ gpio7 + "]";
	}
	
	
	
	
}
