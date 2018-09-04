package domotic.us.mongoDB;

import domotic.us.modelos.TipoAnalogico;

public class ESP8266 {

	private String identificadorSerie;
	private String password;
	private boolean registrado;
	private TipoAnalogico tipoAnalogico;
	
	public ESP8266(String identificadorSerie, boolean registrado, TipoAnalogico tipoSensor) {
		super();
		this.identificadorSerie = identificadorSerie;
		this.registrado = registrado;
		this.tipoAnalogico = tipoSensor;
	}	
	public String getIdentificadorSerie() {
		return identificadorSerie;
	}
	public void setIdentificadorSerie(String identificadorSerie) {
		this.identificadorSerie = identificadorSerie;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isRegistrado() {
		return registrado;
	}
	public void setRegistrado(boolean registrado) {
		this.registrado = registrado;
	}
	public TipoAnalogico getTipoSensor() {
		return tipoAnalogico;
	}
	public void setTipoSensor(TipoAnalogico tipoAnalogico) {
		this.tipoAnalogico = tipoAnalogico;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identificadorSerie == null) ? 0 : identificadorSerie.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + (registrado ? 1231 : 1237);
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
		ESP8266 other = (ESP8266) obj;
		if (identificadorSerie == null) {
			if (other.identificadorSerie != null)
				return false;
		} else if (!identificadorSerie.equals(other.identificadorSerie))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (registrado != other.registrado)
			return false;
		if (tipoAnalogico != other.tipoAnalogico)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ESP8266 [identificadorSerie=" + identificadorSerie + ", password=" + password + ", registrado="
				+ registrado + ", tipoAnalogico=" + tipoAnalogico + "]";
	}
	
	
}
