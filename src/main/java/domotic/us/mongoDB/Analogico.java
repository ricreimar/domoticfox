package domotic.us.mongoDB;

public class Analogico {
	
	private String identificadorPlaca;
	private Integer valor;
	private Boolean pulsador1;
	private Boolean pulsador2;
	
	public Analogico() {
		this(null,null,null,null);
	}
	
	public Analogico(String identificadorPlaca, Integer valor, Boolean pulsador1, Boolean pulsador2) {
		super();
		this.identificadorPlaca = (String) identificadorPlaca;
		this.valor = valor;
		this.pulsador1 = pulsador1;
		this.pulsador2 = pulsador2;
	}
	
	
	public String getIdentificadorPlaca() {
		return identificadorPlaca;
	}
	public void setIdentificador(String identificador) {
		this.identificadorPlaca = identificador;
	}
	public Integer getValor() {
		return valor;
	}
	public void setValor(Integer valor) {
		this.valor = valor;
	}
	public Boolean getPulsador1() {
		return pulsador1;
	}
	public void setPulsador1(Boolean pulsador1) {
		this.pulsador1 = pulsador1;
	}
	public Boolean getPulsador2() {
		return pulsador2;
	}
	public void setPulsador2(Boolean pulsador2) {
		this.pulsador2 = pulsador2;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identificadorPlaca == null) ? 0 : identificadorPlaca.hashCode());
		result = prime * result + ((pulsador1 == null) ? 0 : pulsador1.hashCode());
		result = prime * result + ((pulsador2 == null) ? 0 : pulsador2.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
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
		Analogico other = (Analogico) obj;
		if (identificadorPlaca == null) {
			if (other.identificadorPlaca != null)
				return false;
		} else if (!identificadorPlaca.equals(other.identificadorPlaca))
			return false;
		if (pulsador1 == null) {
			if (other.pulsador1 != null)
				return false;
		} else if (!pulsador1.equals(other.pulsador1))
			return false;
		if (pulsador2 == null) {
			if (other.pulsador2 != null)
				return false;
		} else if (!pulsador2.equals(other.pulsador2))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Analogico [identificadorPlaca=" + identificadorPlaca + ", valor=" + valor + ", pulsador1=" + pulsador1
				+ ", pulsador2=" + pulsador2 + "]";
	}
}
