package mx.org.inai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FLUJO")
public class Flujo {

	@Id
	@GeneratedValue(generator="flujo_idflujo_seq")
	@Column(name="idflujo")
    private Integer idFlujo;
	
	@Column(name="idusuario")
	private Integer idUsuario;
	
	@Column(name="flujo")
	private Integer flujo;
	
	@Column(name="folio")
	private String folio;
	
	@Column(name="etapa")
	private String etapa;
	
	@Column(name="fecha")
	private String fecha;
	
	@Column(name="activo")
	private String activo;
	
	@Column(name="permite_acuerdo")
	private Integer permiteAcuerdo;

	public Integer getIdFlujo() {
		return idFlujo;
	}

	public void setIdFlujo(Integer idFlujo) {
		this.idFlujo = idFlujo;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Integer getFlujo() {
		return flujo;
	}

	public void setFlujo(Integer flujo) {
		this.flujo = flujo;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getEtapa() {
		return etapa;
	}

	public void setEtapa(String etapa) {
		this.etapa = etapa;
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

	public Integer getPermiteAcuerdo() {
		return permiteAcuerdo;
	}

	public void setPermiteAcuerdo(Integer permiteAcuerdo) {
		this.permiteAcuerdo = permiteAcuerdo;
	}	
}
