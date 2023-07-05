package mx.org.inai.service.exencion;

import java.io.IOException;
import mx.org.inai.dto.exencion.ExencionRequestDTO;

/**
 * The Interface ExcencionGeneraCuestionario.
 *
 * @author A. Juarez
 */
public interface ExencionGeneraCuestionario {
    
    /**
     * Generar.
     *
     * @param form the form
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void generar(ExencionRequestDTO form) throws IOException;
}
