package mx.org.inai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "secuencia_flujo")
public class SecuenciaFlujo {

	@Id
	@GeneratedValue(generator="secuencia_flujo_idsecuencia_seq")
	@Column(name="idsecuencia")
    private Integer idSecuencia;
	
	@Column(name="folio")
	private String folio;
	
	@Column(name="etapa")
	private String etapa;
	
	@Column(name="fecha")
	private String fecha;
	
	@Column(name="rutaarchivo")
	private String rutaArchivo;
	
	@Column(name="activo")
	private String activo;

	public Integer getIdSecuencia() {
		return idSecuencia;
	}

	public void setIdSecuencia(Integer idSecuencia) {
		this.idSecuencia = idSecuencia;
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

	public String getRutaArchivo() {
		return rutaArchivo;
	}

	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}
}
