package mx.org.inai.service.exencion;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * The Interface ExcencionDescargaService.
 *
 * @author A. Juarez
 */
public interface ExencionDescargaService {

	/**
	 * Descargar.
	 *
	 * @param id       the id
	 * @param folio    the folio
	 * @param pregunta the pregunta
	 * @param response the response
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void descargar(Integer id, String folio, Integer pregunta, HttpServletResponse response) throws IOException;
}
