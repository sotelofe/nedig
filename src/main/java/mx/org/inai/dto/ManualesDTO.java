package mx.org.inai.dto;

import java.io.Serializable;

public class ManualesDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long idManual;
	private Long idSubManual;
	private String valor;
	private String clave;
	private Integer grupo;
	private Integer orden;
	private Integer tipo;
	private String descripcion;
	private String activo;
	
	public Long getIdManual() {
		return idManual;
	}
	public void setIdManual(Long idManual) {
		this.idManual = idManual;
	}
	public Long getIdSubManual() {
		return idSubManual;
	}
	public void setIdSubManual(Long idSubManual) {
		this.idSubManual = idSubManual;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public Integer getGrupo() {
		return grupo;
	}
	public void setGrupo(Integer grupo) {
		this.grupo = grupo;
	}
	public Integer getOrden() {
		return orden;
	}
	public void setOrden(Integer orden) {
		this.orden = orden;
	}
	public Integer getTipo() {
		return tipo;
	}
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getActivo() {
		return activo;
	}
	public void setActivo(String activo) {
		this.activo = activo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
