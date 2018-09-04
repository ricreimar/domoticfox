package domotic.us.modelos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Gpio {
	private int numero;
	private String nombre;
	private boolean activado;
	private boolean estado;
	private boolean automatico;
	private double umbral;
	private boolean sentido;
	public Gpio(int n) {
		this(n,"Nombre por defecto GPIO"+Integer.toString(n),true, false,false,0.0,false);
	}
	
	@JsonCreator
	public Gpio(
			@JsonProperty("numero")int numero,
			@JsonProperty("nombre")String nombre,
			@JsonProperty("activado")boolean activado, 
			@JsonProperty("estado")boolean estado, 
			@JsonProperty("automatico")boolean automatico, 
			@JsonProperty("umbral")double umbral, 
			@JsonProperty("sentido")boolean sentido){
		super();
		this.numero = numero;
		this.nombre = nombre;
		this.estado = estado;
		this.automatico = automatico;
		this.umbral = umbral;
		this.sentido = sentido;
	}
	public int getNumero() {
		return numero;
	}
	
	public String getNombre() {
		return nombre;
	}
	public boolean getActivado() {
		return activado;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setNumero(int n) {
		this.numero = n;
	}
	public boolean isActivado() {
		return activado;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public boolean isAutomatico() {
		return automatico;
	}
	public void setAutomatico(boolean automatico) {
		this.automatico = automatico;
	}
	public double getUmbral() {
		return umbral;
	}
	public void setUmbral(float umbral) {
		this.umbral = umbral;
	}
	public boolean isSentido() {
		return sentido;
	}
	public void setSentido(boolean sentido) {
		this.sentido = sentido;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (automatico ? 1231 : 1237);
		result = prime * result + (estado ? 1231 : 1237);
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + numero;
		result = prime * result + (sentido ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(umbral);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Gpio other = (Gpio) obj;
		if (automatico != other.automatico)
			return false;
		if (estado != other.estado)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (numero != other.numero)
			return false;
		if (sentido != other.sentido)
			return false;
		if (Double.doubleToLongBits(umbral) != Double.doubleToLongBits(other.umbral))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Gpio [numero=" + numero + ", nombre=" + nombre + ", estado=" + estado + ", automatico=" + automatico
				+ ", umbral=" + umbral + ", sentido=" + sentido + "]";
	}



	
}
