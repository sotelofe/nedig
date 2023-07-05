package mx.org.inai.dto;

import java.io.Serializable;

public class DiaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idDia;
	private String fecha;
	private String activo;
	public Integer getIdDia() {
		return idDia;
	}
	public void setIdDia(Integer idDia) {
		this.idDia = idDia;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getActivo() {
		return activo;
	}
	public void setActivo(String activo) {
		this.activo = activo;
	}	
}
