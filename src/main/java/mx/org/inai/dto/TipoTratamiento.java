package mx.org.inai.dto;

import java.io.Serializable;

public class TipoTratamiento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String obtencion;
	private String aprovechamiento;
	private String explotacion;
	private String almacenamiento;
	private String conservacion;
	private String supresion;
	private String otra;
	
	public String getObtencion() {
		return obtencion;
	}
	public void setObtencion(String obtencion) {
		this.obtencion = obtencion;
	}
	public String getAprovechamiento() {
		return aprovechamiento;
	}
	public void setAprovechamiento(String aprovechamiento) {
		this.aprovechamiento = aprovechamiento;
	}
	public String getExplotacion() {
		return explotacion;
	}
	public void setExplotacion(String explotacion) {
		this.explotacion = explotacion;
	}
	public String getAlmacenamiento() {
		return almacenamiento;
	}
	public void setAlmacenamiento(String almacenamiento) {
		this.almacenamiento = almacenamiento;
	}
	public String getConservacion() {
		return conservacion;
	}
	public void setConservacion(String conservacion) {
		this.conservacion = conservacion;
	}
	public String getSupresion() {
		return supresion;
	}
	public void setSupresion(String supresion) {
		this.supresion = supresion;
	}
	public String getOtra() {
		return otra;
	}
	public void setOtra(String otra) {
		this.otra = otra;
	}
}
