package mx.org.inai.service.exencion.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.org.inai.dto.Constantes;
import mx.org.inai.dto.exencion.ExencionEmailDTO;
import mx.org.inai.dto.exencion.ExencionRequestDTO;
import mx.org.inai.excepcion.exencion.FlujoException;
import mx.org.inai.model.Catalogo;
import mx.org.inai.model.Flujo;
import mx.org.inai.model.Usuario;
import mx.org.inai.repository.CatalogoRepository;
import mx.org.inai.repository.FlujoRepository;
import mx.org.inai.repository.UsuarioRepository;
import mx.org.inai.service.ICorreoService;
import mx.org.inai.service.exencion.ExencionCorreoService;
import mx.org.inai.util.exencion.TipoEnvio;


/**
 * The Class ExcencionCorreoServiceImpl.
 *
 * @author A. Juarez
 */
@Service
public class ExencionCorreoServiceImpl implements ExencionCorreoService {

	/** The Constant LOGGER. */
	//private static final Logger LOGGER = LoggerFactory.getLogger(ExencionCorreoServiceImpl.class);

	/** The usuario repository. */
	@Autowired
	private UsuarioRepository usuarioRepository;

	/** The correo service. */
	@Autowired
	private ICorreoService correoService;

	/** The catalogo repository. */
	@Autowired
	private CatalogoRepository catalogoRepository;

	/** The Constant KEY_EMAIL. */
	private static final String KEY_EMAIL = "altaUsuarioCorreo";
	
	@Autowired
	private FlujoRepository flujoRepositorio;
	
	@Autowired
	private UsuarioRepository usuarioRepositorio;
	

	/**
	 * Enviar.
	 *
	 * @param form the form
	 */
	@Override
	public void enviar(ExencionRequestDTO form) {
		Optional<Usuario> usuario = usuarioRepository.findUsuario(form.getIdUsuario());
		if (!usuario.isPresent()) {
			throw new FlujoException("El usuario no se encontro");
		}
		TipoEnvio tipoEnvio = form.getTipoEnvio();
		ExencionEmailDTO email = obtenerEmail(usuario.get(), form.getFolio(), tipoEnvio);
		//LOGGER.info("Email: {}", email);
		correoService.enviaCorreoNotificacionAltaUsuario();
		correoService.configurarCorreo();
		correoService.enviarCorreo(email.getDe(), email.getNombreDe(), email.getDestinatarios(), email.getAsunto(),
				email.getBody());
	}

	
	/**
	 * Obtener asunto.
	 *
	 * @param catalogos the catalogos
	 * @param tipoEnvio the tipo envio
	 * @return the string
	 */
	private String obtenerAsunto(List<Catalogo> catalogos, TipoEnvio tipoEnvio) {
		for (Catalogo catalogo : catalogos) {
			if (tipoEnvio == TipoEnvio.REGISTRAR && "asunto-registrar-flujo3".equalsIgnoreCase(catalogo.getClave())) {
				return catalogo.getValor();
			}

			if (tipoEnvio == TipoEnvio.ACEPTAR && "asunto-aceptacion-flujo3".equalsIgnoreCase(catalogo.getClave())) {
				return catalogo.getValor();
			}
		}
		return "";
	}

	/**
	 * Obtener email.
	 *
	 * @param usuario   the usuario
	 * @param folio     the folio
	 * @param tipoEnvio the tipo envio
	 * @return the email DTO
	 */
	private ExencionEmailDTO obtenerEmail(Usuario usuario, String folio, TipoEnvio tipoEnvio) {
		ExencionEmailDTO email = new ExencionEmailDTO();
		List<Catalogo> catalogos = catalogoRepository.findBySubCatalogo(KEY_EMAIL);
		email.setAsunto(obtenerAsunto(catalogos, tipoEnvio));
		for (Catalogo catalogo : catalogos) {

			if ("de".equalsIgnoreCase(catalogo.getClave())) {
				email.setDe(catalogo.getValor());
			}

			if ("nombreDe".equalsIgnoreCase(catalogo.getClave())) {
				email.setNombreDe(catalogo.getValor());
			}

			if ("destinatarios".equalsIgnoreCase(catalogo.getClave())) {
				email.setDestinatarios(catalogo.getValor());
			}

			if (tipoEnvio == TipoEnvio.REGISTRAR && "texto-flujo3".equalsIgnoreCase(catalogo.getClave())) {
				email.setBody(catalogo.getValor());

			}

			if (tipoEnvio == TipoEnvio.ACEPTAR && "textoaceptar-flujo3".equalsIgnoreCase(catalogo.getClave())) {
				email.setBody(catalogo.getValor());
				
				Flujo flujo = flujoRepositorio.findAll().stream().filter(f -> f.getFolio().equals(folio) && f.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
				Usuario usuarios = usuarioRepositorio.findAll().stream().filter(u -> u.getIdUsuario() == flujo.getIdUsuario() && u.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
				email.setDestinatarios(usuarios.getEmailInstitucional());
				usuario.setUsuario(usuarios.getUsuario());
			}

			if ("servidor".equalsIgnoreCase(catalogo.getClave())) {
				email.setServidor(catalogo.getValor());
			}
		}
		email.setBody(email.getBody().replace("<user>", usuario.getUsuario()));
		email.setBody(email.getBody().replace("<folio>", folio));
		email.setBody(email.getBody().replace("<servidor>", email.getServidor()));
		return email;
	}
}
