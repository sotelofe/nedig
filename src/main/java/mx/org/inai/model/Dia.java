package mx.org.inai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DIA")
public class Dia {

	@Id
	@GeneratedValue(generator="dia_iddia_seq")
	@Column(name="iddia")
    private Integer idDia;
	
	@Column(name="fecha")
	private String fecha;
	
	@Column(name="activo")
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
