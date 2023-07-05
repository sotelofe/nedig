package mx.org.inai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CUESTIONARIO")
public class Cuestionario {

	@Id
	@GeneratedValue(generator="cuestionario_idcuestionario_seq")
	@Column(name="idcuestionario")
    private Integer idCuestionario;
	
	@Column(name="folio")
	private String folio;
	
	@Column(name="pregunta")
	private Integer pregunta;
	
	@Column(name="subpregunta")
	private Integer subPregunta;
	
	@Column(name="respuesta")
	private String respuesta;
	
	@Column(name="rutaarchivo")
	private String rutaArchivo;
	
	@Column(name="activo")
	private String activo;

	public Integer getIdCuestionario() {
		return idCuestionario;
	}

	public void setIdCuestionario(Integer idCuestionario) {
		this.idCuestionario = idCuestionario;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public Integer getPregunta() {
		return pregunta;
	}

	public void setPregunta(Integer pregunta) {
		this.pregunta = pregunta;
	}	

	public Integer getSubPregunta() {
		return subPregunta;
	}

	public void setSubPregunta(Integer subPregunta) {
		this.subPregunta = subPregunta;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
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
