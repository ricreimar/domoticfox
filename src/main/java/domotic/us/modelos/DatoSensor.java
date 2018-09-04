package domotic.us.modelos;

public class DatoSensor {

	private String identificadorSerie;
	private TipoAnalogico tipoAnalogico;
	private String valor;
	private Integer pulsador1;
	private Integer pulsador2;
	private int year;
	private int month;
	private int day;
	
	public DatoSensor() {
		this("",null,"",0,0,0,0,0);
	}
	
	public DatoSensor(String identificadorSerie, TipoAnalogico tipoAnalogico, String valor, Integer pulsador1,
			Integer pulsador2, int year, int month, int day) {
		super();
		this.identificadorSerie = identificadorSerie;
		this.tipoAnalogico = tipoAnalogico;
		this.valor = valor;
		this.pulsador1 = pulsador1;
		this.pulsador2 = pulsador2;
		this.year = year;
		this.month = month;
		this.day = day;
	}
	
	
	public String getIdentificadorSerie() {
		return identificadorSerie;
	}
	public void setIdentificadorSerie(String identificadorSerie) {
		this.identificadorSerie = identificadorSerie;
	}
	public TipoAnalogico getTipoAnalogico() {
		return tipoAnalogico;
	}
	public void setTipoAnalogico(TipoAnalogico tipoAnalogico) {
		this.tipoAnalogico = tipoAnalogico;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public Integer getPulsador1() {
		return pulsador1;
	}
	public void setPulsador1(Integer pulsador1) {
		this.pulsador1 = pulsador1;
	}
	public Integer getPulsador2() {
		return pulsador2;
	}
	public void setPulsador2(Integer pulsador2) {
		this.pulsador2 = pulsador2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + day;
		result = prime * result + ((identificadorSerie == null) ? 0 : identificadorSerie.hashCode());
		result = prime * result + month;
		result = prime * result + ((pulsador1 == null) ? 0 : pulsador1.hashCode());
		result = prime * result + ((pulsador2 == null) ? 0 : pulsador2.hashCode());
		result = prime * result + ((tipoAnalogico == null) ? 0 : tipoAnalogico.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result + year;
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
		DatoSensor other = (DatoSensor) obj;
		if (day != other.day)
			return false;
		if (identificadorSerie == null) {
			if (other.identificadorSerie != null)
				return false;
		} else if (!identificadorSerie.equals(other.identificadorSerie))
			return false;
		if (month != other.month)
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
		if (tipoAnalogico != other.tipoAnalogico)
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		if (year != other.year)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "DatoSensor [identificadorSerie=" + identificadorSerie + ", tipoAnalogico=" + tipoAnalogico + ", valor="
				+ valor + ", pulsador1=" + pulsador1 + ", pulsador2=" + pulsador2 + ", year=" + year + ", month="
				+ month + ", day=" + day + "]";
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
	
	
}
