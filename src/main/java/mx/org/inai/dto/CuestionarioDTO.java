package mx.org.inai.dto;

import mx.org.inai.model.Cuestionario;

public class CuestionarioDTO {

    private Integer idCuestionario;
	private String folio;
	private Integer pregunta;
	private Integer subpregunta;
	private String respuesta;
	private String rutaArchivo;
	private String activo;
	
	
	public CuestionarioDTO() {
		
	}
	
	public CuestionarioDTO(Cuestionario cue){
		this.idCuestionario = cue.getIdCuestionario();
		this.folio = cue.getFolio();
		this.pregunta = cue.getPregunta();
		this.subpregunta = cue.getSubPregunta();
		this.respuesta = cue.getRespuesta();
		this.rutaArchivo = cue.getRutaArchivo();
		this.activo = cue.getActivo();
	}

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

	public Integer getSubpregunta() {
		return subpregunta;
	}

	public void setSubpregunta(Integer subpregunta) {
		this.subpregunta = subpregunta;
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
