package mx.org.inai.service.exencion.impl;

import mx.org.inai.dto.Constantes;
import mx.org.inai.dto.exencion.ExencionRequestDTO;
import mx.org.inai.excepcion.exencion.FlujoException;
import mx.org.inai.service.exencion.ExencionAbstract;
import mx.org.inai.service.exencion.FolioGeneratorService;
import mx.org.inai.service.exencion.Strategy;
import mx.org.inai.dto.exencion.ExencionResponseDTO;
import mx.org.inai.util.exencion.TipoEnvio;
import mx.org.inai.util.exencion.TipoFlujo;
import mx.org.inai.util.exencion.TipoExencion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * The Class ExencionRegistrarCuestionarioImpl.
 *
 * @author A. Juarez
 */
@Strategy(tipoExencion = TipoExencion.REGISTRAR_CUESTIONARIO)
public class ExencionRegistrarCuestionarioImpl extends ExencionAbstract<String> {

	/** The folio service. */
	@Autowired
	private FolioGeneratorService folioService;

	/**
	 * Registrar.
	 *
	 * @param peticion the peticion
	 * @param f        the f
	 * @return the excencion response DTO
	 */
	@Override
	public ExencionResponseDTO<String> registrar(ExencionRequestDTO peticion, String f) {
		ExencionResponseDTO<String> respuesta = new ExencionResponseDTO<>();
		try {
			peticion.setTipoFlujo(TipoFlujo.EXENCION);
			peticion.setTipoEnvio(TipoEnvio.REGISTRAR);
			Integer numFolio = folioService.obtenerFolio();
			String folio = folioService.generaFolio(numFolio, peticion.getTipoFlujo());
			String etapa = folioService.obtenerEtapa(KEY_ETAPAH1);
			peticion.setEtapa(etapa);
			peticion.setFolio(folio);
			peticion.setSiguienteFolio(numFolio);
			peticion.setNumDias(5);
		//	LOGGER.info("Peticion: {}", peticion);
			validarPeticion(peticion);
			crearDocumento(peticion);
			guardarFolio(peticion);
			guardarFlujo(peticion);
			guardarCuestionario(peticion);
			guardarPeriodo(peticion);
			enviarCorreo(peticion);
			guardarSecuencia(peticion);
			respuesta.setCode(0);
			respuesta.setMensaje(Constantes.OK);
			respuesta.setPayload(folio);
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
