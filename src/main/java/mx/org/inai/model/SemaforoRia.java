package mx.org.inai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "semaforo_ria")
public class SemaforoRia {

	@Id
	@GeneratedValue(generator="semaforo_ria_idsemaforo_seq")
	@Column(name="idsemaforo")
    private Integer idSemaforo;
	
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

	public Integer getIdSemaforo() {
		return idSemaforo;
	}

	public void setIdSemaforo(Integer idSemaforo) {
		this.idSemaforo = idSemaforo;
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
}
