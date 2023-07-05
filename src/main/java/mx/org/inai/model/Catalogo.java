package mx.org.inai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CATALOGO")
public class Catalogo {

	@Id
	@GeneratedValue(generator="catalogo_idcatalogo_seq")
	@Column(name="idcatalogo")
    private Integer idCatalogo;
	
	@Column(name="idsubcatalogo")
	private Integer idSubCatalogo;
	
	@Column(name="valor")
	private String valor;
	
	@Column(name="clave")
	private String clave;
	
	@Column(name="descripcion")
	private String descripcion;
	
	@Column(name="activo")
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
