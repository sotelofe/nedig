package mx.org.inai.service.exencion;

import mx.org.inai.dto.exencion.ExencionRequestDTO;

/**
 * The Interface ExcencionCorreoService.
 *
 * @author A. Juarez
 */
public interface ExencionCorreoService {

	/**
	 * Enviar.
	 *
	 * @param form the form
	 */
	void enviar(ExencionRequestDTO form);
}
