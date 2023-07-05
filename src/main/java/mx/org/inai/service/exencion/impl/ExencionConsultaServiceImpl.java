package mx.org.inai.service.exencion.impl;

import java.util.List;
import java.util.Optional;
import mx.org.inai.dto.Constantes;
import mx.org.inai.dto.exencion.ExencionResponseDTO;
import mx.org.inai.dto.exencion.ExencionCuestionarioDTO;
import mx.org.inai.dto.exencion.ExencionFlujoDTO;
import mx.org.inai.excepcion.exencion.FlujoException;
import mx.org.inai.model.Cuestionario;
import mx.org.inai.model.Flujo;
import mx.org.inai.model.SemaforoRia;
import mx.org.inai.model.Usuario;
import mx.org.inai.repository.CuestionarioRepository;
import mx.org.inai.repository.FlujoRepository;
import mx.org.inai.repository.SemaforoRiaRepository;
import mx.org.inai.repository.UsuarioRepository;
import mx.org.inai.service.exencion.ExencionConsultaService;
import mx.org.inai.transform.exencion.ConsultaCuestionarioTransform;
import mx.org.inai.transform.exencion.ConsultaFlujoTransform;
import mx.org.inai.util.exencion.TipoFlujo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * The Class ExcencionConsultaServiceImpl.
 *
 * @author A. Juarez
 */
@Component
public class ExencionConsultaServiceImpl implements ExencionConsultaService {

	/** The cuestionaro repository. */
	@Autowired
	private CuestionarioRepository cuestionaroRepository;

	/** The usuario repository. */
	@Autowired
	private UsuarioRepository usuarioRepository;

	/** The flujo repository. */
	@Autowired
	private FlujoRepository flujoRepository;

	/** The cuestionario transform. */
	@Autowired
	private ConsultaCuestionarioTransform cuestionarioTransform;

	/** The flujo transform. */
	@Autowired
	private ConsultaFlujoTransform flujoTransform;

	/** The Constant ADMINISTRADOR. */
	private static final int ADMINISTRADOR = 1;

	/** The Constant DGNC. */
	private static final int DGNC = 3;
	
	@Autowired
	private SemaforoRiaRepository semaforoRepository;

	/**
	 * Consultar cuestionario.
	 *
	 * @param folio    the folio
	 * @param pregunta the pregunta
	 * @return the excencion response DTO
	 */
	@Override
	public ExencionResponseDTO<List<ExencionCuestionarioDTO>> consultarCuestionario(final String folio,
			Integer pregunta) {
		ExencionResponseDTO<List<ExencionCuestionarioDTO>> respuesta = new ExencionResponseDTO<>();
		List<Cuestionario> cuestionarios = cuestionaroRepository.findByFolioAndPregunta(folio, pregunta);
		if (cuestionarios.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No existe el folio");
		}
		respuesta.setCode(0);
		respuesta.setMensaje(Constantes.OK);
		respuesta.setPayload(cuestionarioTransform.transform(cuestionarios));
		return respuesta;
	}

	/**
	 * Consultar cuestionario.
	 *
	 * @param folio the folio
	 * @return the excencion response DTO
	 */
	@Override
	public ExencionResponseDTO<List<ExencionCuestionarioDTO>> consultarCuestionario(final String folio) {
		ExencionResponseDTO<List<ExencionCuestionarioDTO>> respuesta = new ExencionResponseDTO<>();
		List<Cuestionario> cuestionarios = cuestionaroRepository.findByFolio(folio);
		if (cuestionarios.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No existe el folio");
		}
		respuesta.setCode(0);
		respuesta.setMensaje(Constantes.OK);
		respuesta.setPayload(cuestionarioTransform.transform(cuestionarios));
		return respuesta;
	}

	/**
	 * Consultar flujo.
	 *
	 * @param idUsuario the id usuario
	 * @return the excencion response DTO
	 */
	@Override
	public ExencionResponseDTO<List<ExencionFlujoDTO>> consultarFlujo(Integer idUsuario) {
		return consultarFlujo(TipoFlujo.EXENCION, idUsuario);
	}

	/**
	 * Consultar flujo.
	 *
	 * @param tipoFlujo the tipo flujo
	 * @param idUsuario the id usuario
	 * @return the excencion response DTO
	 */
	@Override
	public ExencionResponseDTO<List<ExencionFlujoDTO>> consultarFlujo(TipoFlujo tipoFlujo, Integer idUsuario) {
		ExencionResponseDTO<List<ExencionFlujoDTO>> respuesta = new ExencionResponseDTO<>();
		Optional<Usuario> usuario = usuarioRepository.findUsuario(idUsuario);
		List<Flujo> flujos = obtenerFlujos(tipoFlujo, usuario);
		respuesta.setCode(0);
		respuesta.setMensaje(Constantes.OK);
		respuesta.setPayload(flujoTransform.transform(flujos));
		respuesta.getPayload().forEach(f->{
			if(f.getFlujo()==1) {
				f.setTramite(Constantes.CONSULTA);
			}else if(f.getFlujo()==2) {
				f.setTramite(Constantes.PRESENTACION);
			}else if(f.getFlujo()==3) {
				f.setTramite(Constantes.INFORME);
			}
			
			if(f.getPermiteAcuerdo() == null) {
				f.setPermiteAcuerdo(getPermiteAcuerdo(f.getFolio()));
			}
			if(f.getEtapa().equals(Constantes.RIA)) {
				f.setIdColorRia(getIdcolorRia(f.getFolio()));
			}
			
			f.setUsuario(getNombreUsuario(f.getFolio(),f.getFlujo()));
		});
		return respuesta;
	}
	
	private Integer getIdcolorRia(String folio) {
		Integer idColor = 0;
		SemaforoRia sem = semaforoRepository.findByFolioAndActivo(folio, Constantes.ACTIVO);
		if(sem!=null) {
			idColor = sem.getIdColor();
		}
		
		return idColor;
	}
	
	private Integer getPermiteAcuerdo(String folio) {
		Integer idPermiso = 0;
		Flujo flu = flujoRepository.findByFolio(folio);
		if(flu!=null) {
			idPermiso = flu.getPermiteAcuerdo();
		}else {
			idPermiso=0;
		}
		
		return idPermiso;
	}

	private String getNombreUsuario(String folio, Integer idFlujo) {
		String nombre="Admin";
		try {
			Flujo f = flujoRepository.findByFolioAndFlujo(folio, idFlujo);
			if(f!=null) {
				Usuario u = usuarioRepository.findByIdUsuarioAndActivo(f.getIdUsuario(), Constantes.ACTIVO);
				nombre = u.getSujetoObligado();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nombre;
	}
	
	/**
	 * Obtener flujos.
	 *
	 * @param tipoFlujo the tipo flujo
	 * @param usuario   the usuario
	 * @return the list
	 */
	private List<Flujo> obtenerFlujos(TipoFlujo tipoFlujo, Optional<Usuario> usuario) {
		List<Flujo> flujos;
		if (!usuario.isPresent()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "El usuario no existe");
		}
		if (usuario.get().getIdPerfil() == ADMINISTRADOR || usuario.get().getIdPerfil() == DGNC) {
			flujos = flujoRepository.findByFlujo(obtenerTipoFlujo(tipoFlujo));
		} else {
			flujos = flujoRepository.findByFlujoAndUsuario(obtenerTipoFlujo(tipoFlujo), usuario.get().getIdUsuario());
		}
		return flujos;
	}

	/**
	 * Obtener tipo flujo.
	 *
	 * @param tipoFlujo the tipo flujo
	 * @return the int
	 */
	private int obtenerTipoFlujo(TipoFlujo tipoFlujo) {
		switch (tipoFlujo) {
		case EXENCION:
			return TipoFlujo.EXENCION.getValor();
		case CONSULTAS:
			return TipoFlujo.CONSULTAS.getValor();
		case EIPDP:
			return TipoFlujo.EIPDP.getValor();
		default:
			throw new FlujoException("El tipo de flujo no es valido");
		}
	}
}
