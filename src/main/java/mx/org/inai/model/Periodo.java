package mx.org.inai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PERIODO")
public class Periodo {

	@Id
	@GeneratedValue(generator="periodo_idperiodo_seq")
	@Column(name="idperiodo")
    private Integer idPeriodo;
	
	@Column(name="folio")
	private String folio;
	
	@Column(name="fechainicioperiodo")
	private String fechaInicioPeriodo;
	
	@Column(name="fechafinperiodo")
	private String fechaFinPeriodo;
	
	@Column(name="diasperiodo")
	private Integer diasPeriodo;
	
	@Column(name="idcolor")
	private Integer idColor;
	
	@Column(name="activo")
	private String activo;
	
	@Column(name="dias_transcurridos")
	private Integer diasTranscurridos;

	@Column(name="suspendido")
	private Integer suspendido;
	
	public Integer getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(Integer idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getFechaInicioPeriodo() {
		return fechaInicioPeriodo;
	}

	public void setFechaInicioPeriodo(String fechaInicioPeriodo) {
		this.fechaInicioPeriodo = fechaInicioPeriodo;
	}

	public String getFechaFinPeriodo() {
		return fechaFinPeriodo;
	}

	public void setFechaFinPeriodo(String fechaFinPeriodo) {
		this.fechaFinPeriodo = fechaFinPeriodo;
	}

	public Integer getDiasPeriodo() {
		return diasPeriodo;
	}

	public void setDiasPeriodo(Integer diasPeriodo) {
		this.diasPeriodo = diasPeriodo;
	}

	public Integer getIdColor() {
		return idColor;
	}

	public void setIdColor(Integer idColor) {
		this.idColor = idColor;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public Integer getDiasTranscurridos() {
		return diasTranscurridos;
	}

	public void setDiasTranscurridos(Integer diasTranscurridos) {
		this.diasTranscurridos = diasTranscurridos;
	}

	public Integer getSuspendido() {
		return suspendido;
	}

	public void setSuspendido(Integer suspendido) {
		this.suspendido = suspendido;
	}
}
