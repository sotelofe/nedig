package mx.org.inai.dto;

import java.io.Serializable;

public class CatalogoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	
    private Integer idCatalogo;
	private Integer idSubCatalogo;
	private String valor;
	private String clave;
	private String descripcion;
	private String activo;
	

	public Integer getIdCatalogo() {
		return idCatalogo;
	}

	public void setIdCatalogo(Integer idCatalogo) {
		this.idCatalogo = idCatalogo;
	}

	public Integer getIdSubCatalogo() {
		return idSubCatalogo;
	}

	public void setIdSubCatalogo(Integer idSubCatalogo) {
		this.idSubCatalogo = idSubCatalogo;
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
