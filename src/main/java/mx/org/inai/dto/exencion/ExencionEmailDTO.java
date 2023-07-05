package  mx.org.inai.dto.exencion;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class EmailDTO.
 *
 * @author A. Juarez
 */
@Data
@NoArgsConstructor
public class ExencionEmailDTO {

    /** The de. */
    private String de;
    
    /** The nombre de. */
    private String nombreDe;
    
    /** The destinatarios. */
    private String destinatarios;
    
    /** The asunto. */
    private String asunto;
    
    /** The body. */
    private String body;
    
    /** The servidor. */
    private String servidor;

	public String getDe() {
		return de;
	}

	public void setDe(String de) {
		this.de = de;
	}

	public String getNombreDe() {
		return nombreDe;
	}

	public void setNombreDe(String nombreDe) {
		this.nombreDe = nombreDe;
	}

	public String getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(String destinatarios) {
		this.destinatarios = destinatarios;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getServidor() {
		return servidor;
	}

	public void setServidor(String servidor) {
		this.servidor = servidor;
	}

    
}
