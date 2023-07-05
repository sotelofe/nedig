package mx.org.inai.dto.exencion;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The Class ExencionCuestionarioDTO.
 *
 * @author A. Juarez
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExencionCuestionarioDTO extends ExencionAbstractBaseDTO {

    
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id cuestionario. */
	private Integer idCuestionario;
    
    /** The folio. */
    private String folio;
    
    /** The pregunta. */
    private Integer pregunta;
    
    /** The subpregunta. */
    private Integer subpregunta;
    
    /** The respuesta. */
    private String respuesta;
    
    /** The ruta archivo. */
    private String rutaArchivo;
    
    /** The activo. */
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
