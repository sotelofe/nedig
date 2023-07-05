package mx.org.inai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tratamiento_particulares")
public class TrataMientoParticulares {

	@Id
	@GeneratedValue(generator = "tratamiento_particulares_idparticular_seq")
	@Column(name = "idparticular")
	private Integer idParticular;

	@Column(name = "titulo")
	private String titulo;

	@Column(name = "ayuda")
	private String ayuda;

	@Column(name = "orden")
	private Integer orden;

	@Column(name = "activo")
	private String activo;

	public Integer getIdParticular() {
		return idParticular;
	}

	public void setIdParticular(Integer idParticular) {
		this.idParticular = idParticular;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAyuda() {
		return ayuda;
	}

	public void setAyuda(String ayuda) {
		this.ayuda = ayuda;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}
}
