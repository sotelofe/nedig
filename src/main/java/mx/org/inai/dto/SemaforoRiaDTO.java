package mx.org.inai.dto;

import java.io.Serializable;

import mx.org.inai.model.SemaforoRia;

public class SemaforoRiaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer idSemaforo;
	private String folio;
	private String fechaInicioPeriodo;
	private String fechaFinPeriodo;
	private Integer diasPeriodo;
	private Integer idColor;
	private String activo;
	private Integer diasTranscurridos;
	private Integer suspendido;
	
	public SemaforoRiaDTO(SemaforoRia periodo) {
		this.idSemaforo = periodo.getIdSemaforo();
		this.folio = periodo.getFolio();
		this.fechaInicioPeriodo = periodo.getFechaInicioPeriodo();
		this.fechaFinPeriodo = periodo.getFechaFinPeriodo();
		this.diasPeriodo = periodo.getDiasPeriodo();
		this.idColor = periodo.getIdColor();
		this.activo = periodo.getActivo();		
	}

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
