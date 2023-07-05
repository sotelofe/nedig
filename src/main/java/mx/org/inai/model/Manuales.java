package mx.org.inai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Manuales")
public class Manuales {
	
	@Id
	@GeneratedValue(generator="manuales_idmanual_seq")
	@Column(name="idmanual")	
	private Long idManual;
	@Column(name="idsubmanual")
	private Long idSubManual;
	@Column(name="valor")
	private String valor;
	@Column(name="clave")
	private String clave;
	@Column(name="grupo")
	private Integer grupo;
	@Column(name="orden")
	private Integer orden;
	@Column(name="tipo")
	private Integer tipo;
	@Column(name="descripcion")
	private String descripcion;
	@Column(name="activo")
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
}
