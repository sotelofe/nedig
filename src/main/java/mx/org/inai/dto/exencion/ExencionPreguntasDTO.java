package mx.org.inai.dto.exencion;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class PreguntasExencionDTO.
 *
 * @author A. Juarez
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ExencionPreguntasDTO extends ExencionAbstractBaseDTO {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2598981453115471217L;

	/** The pregunta. */
	private Integer pregunta;

	/** The subpregunta. */
	private Integer subpregunta;

	/** The respuesta. */
	private String respuesta;

	/** The archivo. */
	private ExencionArchivoDTO archivo;

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

	public ExencionArchivoDTO getArchivo() {
		return archivo;
	}

	public void setArchivo(ExencionArchivoDTO archivo) {
		this.archivo = archivo;
	}
	
	
}
