package mx.org.inai.dto.exencion;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class ExcencionResponseDTO.
 *
 * @author A. Juarez
 * @param <P> the generic type
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ExencionResponseDTO<P> extends ExencionAbstractBaseDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The code.
     */
    private int code;

    /**
     * The mensaje.
     */
    private String mensaje;

    /**
     * The payload.
     */
    private transient P payload;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public P getPayload() {
		return payload;
	}

	public void setPayload(P payload) {
		this.payload = payload;
	}
    
    

}
