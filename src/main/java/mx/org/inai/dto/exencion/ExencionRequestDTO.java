package mx.org.inai.dto.exencion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.org.inai.util.exencion.TipoEnvio;
import mx.org.inai.util.exencion.TipoFlujo;

/**
 * The Class ExencionRequestDTO.
 *
 * @author A. Juarez
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ExencionRequestDTO extends ExencionAbstractBaseDTO implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The folio. */
    @JsonIgnore
    private String folio;
    
    /** The etapa. */
    @JsonIgnore
    private String etapa;
    
    /** The siguiente folio. */
    @JsonIgnore
    private Integer siguienteFolio;
    
    /** The num dias. */
    @JsonIgnore
    private Integer numDias;
    
    /** The tipo flujo. */
    @JsonIgnore
    private TipoFlujo tipoFlujo;
    
    /** The tipo envio. */
    @JsonIgnore
    private TipoEnvio tipoEnvio;
    
    /** The preguntas. */
    private List<ExencionPreguntasDTO> preguntas;
    
    /** The id usuario. */
    private Integer idUsuario;

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

	public Integer getSiguienteFolio() {
		return siguienteFolio;
	}

	public void setSiguienteFolio(Integer siguienteFolio) {
		this.siguienteFolio = siguienteFolio;
	}

	public Integer getNumDias() {
		return numDias;
	}

	public void setNumDias(Integer numDias) {
		this.numDias = numDias;
	}

	public TipoFlujo getTipoFlujo() {
		return tipoFlujo;
	}

	public void setTipoFlujo(TipoFlujo tipoFlujo) {
		this.tipoFlujo = tipoFlujo;
	}

	public TipoEnvio getTipoEnvio() {
		return tipoEnvio;
	}

	public void setTipoEnvio(TipoEnvio tipoEnvio) {
		this.tipoEnvio = tipoEnvio;
	}

	public List<ExencionPreguntasDTO> getPreguntas() {
		return preguntas;
	}

	public void setPreguntas(List<ExencionPreguntasDTO> preguntas) {
		this.preguntas = preguntas;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
    
    
}
