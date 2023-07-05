package mx.org.inai.service.exencion.impl;

import mx.org.inai.dto.Constantes;
import mx.org.inai.dto.exencion.ExencionResponseDTO;
import mx.org.inai.dto.exencion.ExencionRequestDTO;
import mx.org.inai.excepcion.exencion.FlujoException;
import mx.org.inai.model.Flujo;
import mx.org.inai.repository.FlujoRepository;
import mx.org.inai.service.exencion.ExencionAbstract;
import mx.org.inai.service.exencion.FolioGeneratorService;
import mx.org.inai.service.exencion.Strategy;
import mx.org.inai.util.exencion.TipoEnvio;
import mx.org.inai.util.exencion.TipoFlujo;
import mx.org.inai.util.exencion.TipoExencion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * The Class ExencionRegistrarRespuesta.
 *
 * @author A. Juarez
 */
@Strategy(tipoExencion = TipoExencion.REGISTRAR_RESPUESTA)
public class ExencionRegistrarRespuestaImpl extends ExencionAbstract<String> {

	/** The folio service. */
	@Autowired
	private FolioGeneratorService folioService;
	
	@Autowired	
	private FlujoRepository flujoRepository;

	/**
	 * Registrar.
	 *
	 * @param peticion the peticion
	 * @param folio    the folio
	 * @return the excencion response DTO
	 */
	@Override
	public ExencionResponseDTO<String> registrar(ExencionRequestDTO peticion, String folio) {
		ExencionResponseDTO<String> respuesta = new ExencionResponseDTO<>();
		try {
			String etapa = folioService.obtenerEtapa(KEY_ETAPAH3);
			peticion.setEtapa(etapa);
			peticion.setTipoFlujo(TipoFlujo.EXENCION);
			peticion.setTipoEnvio(TipoEnvio.RESPUESTA);
			peticion.setFolio(folio);
			validarPeticion(peticion);
			guardarFlujo(peticion);
			guardarCuestionario(peticion);
			guardarPeriodo(peticion);			
			peticion.setEtapa(Constantes.ACUERDO);
			guardarSecuenciaActualizacion(peticion,true);			
			peticion.setEtapa(etapa);
			guardarSecuenciaRuta(peticion,"");
			Flujo flujo = flujoRepository.findByFolio(folio);
			flujo.setPermiteAcuerdo(0);
			flujoRepository.save(flujo);
			respuesta.setCode(0);
			respuesta.setMensaje(Constantes.OK);
			respuesta.setPayload(peticion.getFolio());
			return respuesta;
		} catch (ResponseStatusException | FlujoException ex) {
			logError(ex.getMessage());
			respuesta.setCode(-1);
			respuesta.setMensaje(ex.getMessage());
			return respuesta;
		} catch (Exception ex) {
			logError(ex);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error", ex);
		}
	}
}
