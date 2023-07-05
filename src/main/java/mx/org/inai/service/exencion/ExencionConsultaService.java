package mx.org.inai.service.exencion;

import java.util.List;

import mx.org.inai.dto.exencion.ExencionResponseDTO;
import mx.org.inai.dto.exencion.ExencionCuestionarioDTO;
import mx.org.inai.dto.exencion.ExencionFlujoDTO;
import mx.org.inai.util.exencion.TipoFlujo;

/**
 * The Interface ExcencionConsultaService.
 *
 * @author A. Juarez
 */
public interface ExencionConsultaService {
    
    /**
     * Consultar cuestionario.
     *
     * @param folio the folio
     * @param pregunta the pregunta
     * @return the excencion response DTO
     */
    ExencionResponseDTO<List<ExencionCuestionarioDTO>> consultarCuestionario(final String folio, Integer pregunta);
    
    /**
     * Consultar cuestionario.
     *
     * @param folio the folio
     * @return the excencion response DTO
     */
    ExencionResponseDTO<List<ExencionCuestionarioDTO>> consultarCuestionario(final String folio);
    
    /**
     * Consultar flujo.
     *
     * @param tipoFlujo the tipo flujo
     * @param idUsuario the id usuario
     * @return the excencion response DTO
     */
    ExencionResponseDTO<List<ExencionFlujoDTO>> consultarFlujo(TipoFlujo tipoFlujo, Integer idUsuario);
    
    /**
     * Consultar flujo.
     *
     * @param idUsuario the id usuario
     * @return the excencion response DTO
     */
    ExencionResponseDTO<List<ExencionFlujoDTO>>consultarFlujo(Integer idUsuario);
    
}
