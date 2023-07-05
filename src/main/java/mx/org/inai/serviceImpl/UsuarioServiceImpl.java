package mx.org.inai.serviceImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import mx.org.inai.dto.Constantes;
import mx.org.inai.dto.RecuperarUsuarioDTO;
import mx.org.inai.dto.ResponseLogin;
import mx.org.inai.dto.ResponseUsuario;
import mx.org.inai.dto.TokenRecupera;
import mx.org.inai.dto.UsuarioAltaDTO;
import mx.org.inai.dto.UsuarioDTO;
import mx.org.inai.model.Catalogo;
import mx.org.inai.model.Perfil;
import mx.org.inai.model.Usuario;
import mx.org.inai.model.UsuarioToken;
import mx.org.inai.repository.CatalogoRepository;
import mx.org.inai.repository.PerfilRepository;
import mx.org.inai.repository.UsuarioRepository;
import mx.org.inai.repository.UsuarioTokenRepository;
import mx.org.inai.service.ICatalogoService;
import mx.org.inai.service.ICorreoService;
import mx.org.inai.service.IEncryptService;
import mx.org.inai.service.IUsuarioService;
import mx.org.inai.util.usuario.SistemaOperativo;

@Service
public class UsuarioServiceImpl implements IUsuarioService{
	
	//private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UsuarioRepository usuarioRepositorio;
	
	@Autowired
	private PerfilRepository perfilRepositorio;
	
	@Autowired
	ICorreoService iCorreoService;
	
	@Autowired
	ICatalogoService iCatalogoService;
	
	@Autowired
	IEncryptService encryptService;
	
	@Autowired
	private CatalogoRepository catalogoRepositorio;
	
	@Autowired
	private UsuarioTokenRepository tokenRepositorio;
	
	@Autowired
	SistemaOperativo so;
	
	
	public ResponseUsuario altaUsuario(UsuarioAltaDTO usuario) {
		ResponseUsuario response = new ResponseUsuario();
		Usuario usuarios = null;
        try {
			
        	//usuarios = usuarioRepositorio.findAll().stream().filter(u -> u.getUsuario().equals(usuario.getUsuario()) || u.getEmailInstitucional().equals(usuario.getEmailInstitucional()) ).findAny().orElse(null);
        	usuarios = usuarioRepositorio.findByUsuarioAndEmailInstitucional(usuario.getUsuario(), usuario.getEmailInstitucional());
        	if(usuarios==null) {
	         	Usuario usuarioModel = convertirDtoModelUsuario(usuario);
	        	usuarioRepositorio.save(usuarioModel);
				
				if(usuarioModel.getIdUsuario() !=null) {
					response.setEstatus(Constantes.OK);
					response.setMensaje(Constantes.LOGIN_CORRECTO);
					enviaCorreoNotificacionAltaUsuario(usuario);
				}else {
					response.setEstatus(Constantes.FAIL);
					response.setMensaje(Constantes.NO_ALTA);
				}
            }else {
				response.setEstatus(Constantes.EXISTE);
				response.setMensaje(Constantes.USUARIO_EXISTE);
			}
			
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
			//logger.info(e.getMessage());
		}
		
		
		return response;
	}
	
	private Usuario convertirDtoModelUsuario(UsuarioAltaDTO usuario)throws Exception {
		//Perfil perfil = perfilRepositorio.findAll().stream().filter(p -> p.getDescPerfil().equals(Constantes.SUJETO_OBLIGADO) && p.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
		Perfil perfil = perfilRepositorio.findByDescPerfilAndActivo(Constantes.SUJETO_OBLIGADO, Constantes.ACTIVO);
		Usuario u = new Usuario();
		u.setIdPerfil(perfil.getIdPerfil());
		u.setSujetoObligado(usuario.getSujetoObligado());
		u.setNombreServidorPublico(usuario.getNombreServidorPublico());
		u.setCargoServidorPublico(usuario.getCargoServidorPublico());
		u.setUsuario(usuario.getUsuario());
		u.setContrasena(encriptarPass(usuario.getContrasena()));
		u.setEmailInstitucional(usuario.getEmailInstitucional());
		u.setTelefono(usuario.getTelefono());
		u.setExtensionTelefono(usuario.getExtensionTelefono());
		u.setCelular(usuario.getCelular());
		u.setActivo(Constantes.INACTIVO);

		return u;
	}
	
	
	private void enviaCorreoNotificacionAltaUsuario(UsuarioAltaDTO usuario)throws Exception {
		
		String de ="";
		String nombreDe ="";
		String destinatarios ="";
		String asunto ="";
		String body ="";
		String servidor ="";
		
		List<Catalogo> listCatalogo = new ArrayList<Catalogo>();
		listCatalogo = iCatalogoService.getListaDatosCatalogo(Constantes.ALTA_USUARIO_CORREO);
		
		
		for(int i = 0 ; i< listCatalogo.size(); i++) {
			if(listCatalogo.get(i).getClave().equals("de")) {
				de = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("nombreDe")) {
				nombreDe = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("destinatarios")) {
				destinatarios = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("asunto")) {
				asunto = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("texto")) {
				body = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("servidor")) {
				servidor = listCatalogo.get(i).getValor();
				continue;
			}
		}
		
		body = body.replace("<user>", usuario.getUsuario());
		body = body.replace("<email>", usuario.getEmailInstitucional());
		
		if(so.isWindows()) {
					
		}else if(so.isUnix()) {
			servidor = servidor + "eipdp";			
		}
		
		body = body.replace("<servidor>", servidor);
		
		//logger.debug(nombreDe);
		System.out.println(nombreDe);
		//logger.info("de, nombreDe, destinatarios, asunto, body :"+ de +","+ nombreDe+","+destinatarios+","+asunto+","+body);
		
		iCorreoService.enviaCorreoNotificacionAltaUsuario();
		iCorreoService.configurarCorreo();
		iCorreoService.enviarCorreo(de, nombreDe, destinatarios, asunto, body);
		
		
	}

	@Override
	public ResponseUsuario recuperarUsuario(RecuperarUsuarioDTO usuario) {
		ResponseUsuario response = new ResponseUsuario();
		Usuario usuarios = null;
        try {
        	//usuarios = usuarioRepositorio.findAll().stream().filter(u -> u.getEmailInstitucional().equals(usuario.getEmailInstitucional()) && u.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
        	usuarios = usuarioRepositorio.findByEmailInstitucionalAndActivo(usuario.getEmailInstitucional(), Constantes.ACTIVO);
        	if(usuarios!=null) {
	         	
        		
        		String token = encriptar(usuarios.getUsuario());
        		borrarListaTokens(usuarios.getUsuario());
        		guardarUsuarioToken(usuarios.getUsuario(), token);
        		enviaCorreoNotificacionRecuperaUsuario(usuarios);
        		response.setEstatus(Constantes.OK);
				response.setMensaje(Constantes.ENVIO_CORRECTO);
        		
        		
            }else {
				response.setEstatus(Constantes.NO_EXISTE);
				response.setMensaje(Constantes.USUARIO_NO_EXISTE);
			}
        }catch (Exception e) {
        	e.printStackTrace();
        	response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
			//logger.info(e.getMessage());
		}
        
		return response;
	}
	
	private void enviaCorreoNotificacionRecuperaUsuario(Usuario usuario)throws Exception {
		
		String de ="";
		String nombreDe ="";
		String destinatarios ="";
		String asunto ="";
		String body ="";
		String servidor ="";
		
		List<Catalogo> listCatalogo = new ArrayList<Catalogo>();
		listCatalogo = iCatalogoService.getListaDatosCatalogo(Constantes.ALTA_USUARIO_CORREO);
		
		
		for(int i = 0 ; i< listCatalogo.size(); i++) {
			if(listCatalogo.get(i).getClave().equals("de")) {
				de = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("nombreDe")) {
				nombreDe = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("destinatarios")) {
				destinatarios = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("asunto")) {
				asunto = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("textorecupera")) {
				body = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("servidor")) {
				servidor = listCatalogo.get(i).getValor();
				continue;
			}
		}
		
		body = body.replace("<user>", usuario.getUsuario());
		//body = body.replace("<email>", usuario.getEmailInstitucional());
		
		//UsuarioToken usuariosToken = tokenRepositorio.findAll().stream().filter(u -> u.getUsuarioToken().equals(usuario.getUsuario())).findAny().orElse(null);
		UsuarioToken usuariosToken = tokenRepositorio.findByUsuarioToken(usuario.getUsuario());
		if(so.isWindows()) {
			servidor = servidor + "?token="+ usuariosToken.getsToken();			
		}else if(so.isUnix()) {			
			servidor = servidor + "eipdp/#/?token="+ usuariosToken.getsToken();
		}
		
		
		body = body.replace("<servidor>", servidor);
		
	//	logger.debug(nombreDe);
		System.out.println(nombreDe);
		
		destinatarios = usuario.getEmailInstitucional();
		asunto = "Recuperación de Contraseña";
		iCorreoService.enviaCorreoNotificacionAltaUsuario();
		iCorreoService.configurarCorreo();
		//logger.info("de, nombreDe, destinatarios, asunto, body: "+ de+","+ nombreDe+","+destinatarios+","+asunto+","+body);
		iCorreoService.enviarCorreo(de, nombreDe, destinatarios, asunto, body);
		
		
	}
	
	
	private String encriptar(String usuario)throws Exception {
		
		
		String en = encryptService.base64encode(usuario, obtenerKey());
		
		if(en.indexOf("+")!=-1) {
			encriptar(usuario);
		}else if(en.indexOf("/")!=-1) {
			encriptar(usuario);
		}
		
        return en;
	}
	
	private String obtenerKey() {
		//Catalogo catalogo = catalogoRepositorio.findAll().stream().filter(c -> c.getClave().equals(Constantes.KEY) && c.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
		Catalogo catalogo = catalogoRepositorio.findByClaveAndActivo(Constantes.KEY, Constantes.ACTIVO);
		//List<Catalogo> listCatalogo = catalogoRepositorio.findAll().stream().filter(ca ->  ca.getIdSubCatalogo() == catalogo.getIdCatalogo()).collect(Collectors.toList());
		List<Catalogo> listCatalogo = catalogoRepositorio.findByIdSubCatalogoAndActivo(catalogo.getIdCatalogo(), Constantes.ACTIVO);
		String key="";
		for(int i = 0 ; i< listCatalogo.size(); i++) {
			if(listCatalogo.get(i).getClave().equals("key")) {
				key = listCatalogo.get(i).getValor();
				break;
			}
		}
		
		return key;
	}
	
	private void borrarListaTokens(String usuario) {
		//List<UsuarioToken> listaUsuariosToken = tokenRepositorio.findAll().stream().filter(u -> u.getUsuarioToken().equals(usuario)).collect(Collectors.toList());
		List<UsuarioToken> listaUsuariosToken = tokenRepositorio.findByUsuarioTokenLista(usuario);
		if(listaUsuariosToken!=null && listaUsuariosToken.size() > 0) {
			for (UsuarioToken usuarioToken : listaUsuariosToken) {
				tokenRepositorio.delete(usuarioToken);
			}
		}
	}
	
	private void guardarUsuarioToken(String usuario, String token) {
		UsuarioToken utok = new UsuarioToken();
		utok.setUsuarioToken(usuario);
		utok.setsToken(token);
		utok.setFecha("");
		utok.setActivo("A");
		tokenRepositorio.save(utok);
	}

	@Override
	public ResponseLogin validaUsuarioToken(TokenRecupera token) {
		ResponseLogin response = new ResponseLogin();
		try {
			//UsuarioToken usuarios = tokenRepositorio.findAll().stream().filter(u -> u.getsToken().equals(token.getToken()) && u.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
			UsuarioToken usuarios = tokenRepositorio.findBysToken(token.getToken(), Constantes.ACTIVO);
			if(usuarios!=null) {
				String desencriptar = encryptService.base64decode(token.getToken(), obtenerKey());
				if(usuarios.getUsuarioToken().equals(desencriptar)) {
					UsuarioDTO ud = new UsuarioDTO();
					//Usuario us = usuarioRepositorio.findAll().stream().filter(u -> u.getUsuario().equals(usuarios.getUsuarioToken())).findAny().orElse(null);
					Usuario us = usuarioRepositorio.findByUsuario(usuarios.getUsuarioToken());
					ud.setUsuario(us.getUsuario());
					response.setUsuario(ud);
					response.setEstatus(Constantes.OK);
				}else {
					response.setUsuario(new UsuarioDTO());
					response.setEstatus(Constantes.FAIL);
				}
				
			}else {
				response.setUsuario(new UsuarioDTO());
				response.setEstatus(Constantes.FAIL);
			}
		
		}catch (Exception e) {
			response.setUsuario(new UsuarioDTO());
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
			//logger.info(e.getMessage());
		}
		
		return response;
	}

	@Override
	public ResponseUsuario actualizarPass(UsuarioAltaDTO usuario) {
		ResponseUsuario response = new ResponseUsuario();
		Usuario usuarios = null;
        try {
        	//usuarios = usuarioRepositorio.findAll().stream().filter(u -> u.getUsuario().equals(usuario.getUsuario())).findAny().orElse(null);
        	usuarios = usuarioRepositorio.findByUsuario(usuario.getUsuario());
        	if(usuarios!=null) {
        		usuarios.setContrasena(encriptarPass(usuario.getContrasena()));
        		usuarioRepositorio.save(usuarios);
        		
        		//UsuarioToken tok = tokenRepositorio.findAll().stream().filter(u -> u.getUsuarioToken().equals(usuario.getUsuario())).findAny().orElse(null);
        		UsuarioToken tok = tokenRepositorio.findByUsuarioToken(usuario.getUsuario());
        		tok.setActivo("I");
        		tokenRepositorio.save(tok);
        		
        		response.setEstatus(Constantes.OK);
            }else {
				response.setEstatus(Constantes.FAIL);
			}
        }catch (Exception e) {
        	e.printStackTrace();
        	response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
		//	logger.info(e.getMessage());
		}
        
		return response;
	}

	@Override
	public ResponseUsuario recuperarUsuarios() {
		ResponseUsuario response = new ResponseUsuario();
		List<Usuario> listaUsuarios = new ArrayList<Usuario>();
		List<UsuarioDTO> listaUsuariosDTO = new ArrayList<UsuarioDTO>();
        try {
        	
        	listaUsuarios = usuarioRepositorio.findByActivoOrActivo(Constantes.ACTIVO,Constantes.INACTIVO);
        	
        	listaUsuarios = listaUsuarios.stream()
			.sorted(Comparator.comparing(Usuario::getUsuario))
			.collect(Collectors.toList());
        	
        	if(listaUsuarios!=null && listaUsuarios.size() > 0) {
	         	
        		for(Usuario us: listaUsuarios){
        			UsuarioDTO usdto = new UsuarioDTO();
        			usdto.setSujetoObligado(us.getSujetoObligado());
        			usdto.setNombreServidorPublico(us.getNombreServidorPublico());
        			usdto.setCargoServidorPublico(us.getCargoServidorPublico());
        			usdto.setUsuario(us.getUsuario());
        			usdto.setEmailInstitucional(us.getEmailInstitucional());
        			usdto.setTelefono(us.getTelefono());
        			usdto.setExtensionTelefono(us.getExtensionTelefono());
        			usdto.setCelular(us.getCelular());
        			usdto.setActivo(us.getActivo());
        			
        			listaUsuariosDTO.add(usdto);
        		}
        		
        		response.setUsuario(listaUsuariosDTO);
        		response.setEstatus(Constantes.OK);
				
            }else {
				response.setEstatus(Constantes.NO_EXISTE);
				response.setUsuario(listaUsuariosDTO);
			}
        }catch (Exception e) {
        	e.printStackTrace();
        	response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
			response.setUsuario(listaUsuariosDTO);
			//logger.info(e.getMessage());
		}
        
		return response;
	}

	@Override
	public ResponseUsuario activarUsuario(RecuperarUsuarioDTO usuario) {
		Usuario usuarios = null;
		ResponseUsuario response = new ResponseUsuario();
		
		 try {
	        	//usuarios = usuarioRepositorio.findAll().stream().filter(u -> u.getUsuario().equals(usuario.getEmailInstitucional())).findAny().orElse(null);
	        	usuarios = usuarioRepositorio.findByUsuario(usuario.getEmailInstitucional());
	        	if(usuarios!=null) {
	        		usuarios.setActivo(Constantes.ACTIVO);
	        		usuarioRepositorio.save(usuarios);

	        		enviaCorreoNotificacionActivaUsuario(usuarios);
	        		response.setEstatus(Constantes.OK);
	            }else {
					response.setEstatus(Constantes.FAIL);
				}
	        }catch (Exception e) {
	        	e.printStackTrace();
	        	response.setEstatus(Constantes.ERROR);
				response.setMensaje(response.getMensaje() + e.getMessage());
			//	logger.info(e.getMessage());
			}
		 
		return response;
	}
	
	@Override
	public ResponseUsuario desactivarUsuario(RecuperarUsuarioDTO usuario) {
		Usuario usuarios = null;
		ResponseUsuario response = new ResponseUsuario();
		
		 try {
	        	//usuarios = usuarioRepositorio.findAll().stream().filter(u -> u.getUsuario().equals(usuario.getEmailInstitucional())).findAny().orElse(null);
	        	usuarios = usuarioRepositorio.findByUsuario(usuario.getEmailInstitucional());
	        	if(usuarios!=null) {
	        		usuarios.setActivo(Constantes.INACTIVO);
	        		usuarioRepositorio.save(usuarios);
	        		response.setEstatus(Constantes.OK);
	            }else {
					response.setEstatus(Constantes.FAIL);
				}
	        }catch (Exception e) {
	        	e.printStackTrace();
	        	response.setEstatus(Constantes.ERROR);
				response.setMensaje(response.getMensaje() + e.getMessage());
			//	logger.info(e.getMessage());
			}
		 
		return response;
	}
	
	@Override
	public ResponseUsuario eliminarUsuario(RecuperarUsuarioDTO usuario) {
		Usuario usuarios = null;
		ResponseUsuario response = new ResponseUsuario();
		
		 try {	        	
	        	usuarios = usuarioRepositorio.findByUsuario(usuario.getEmailInstitucional());
	        	if(usuarios!=null) {
	        		usuarios.setActivo(Constantes.BAJA);
	        		usuarioRepositorio.save(usuarios);
	        		response.setEstatus(Constantes.OK);
	            }else {
					response.setEstatus(Constantes.FAIL);
				}
	        }catch (Exception e) {
	        	e.printStackTrace();
	        	response.setEstatus(Constantes.ERROR);
				response.setMensaje(response.getMensaje() + e.getMessage());
			//	logger.info(e.getMessage());
			}
		 
		return response;
	}
	
	@Override
	public ResponseUsuario actualizarUsuario(UsuarioDTO usuario) {
		Usuario usuarios = null;
		ResponseUsuario response = new ResponseUsuario();
		
		 try {	        		        	
	        	usuarios = usuarioRepositorio.findByIdUsuario(usuario.getIdUsuario());
	        	if(usuarios!=null) {	        		
	        		usuarios.setSujetoObligado(usuario.getSujetoObligado());
	        		usuarios.setNombreServidorPublico(usuario.getNombreServidorPublico());
	        		usuarios.setCargoServidorPublico(usuario.getCargoServidorPublico());	        		
	        		usuarios.setUsuario(usuario.getUsuario());
	        		usuarios.setEmailInstitucional(usuario.getEmailInstitucional());
	        		usuarios.setTelefono(usuario.getTelefono());
	        		usuarios.setExtensionTelefono(usuario.getExtensionTelefono());
	        		usuarios.setCelular(usuario.getCelular());
	        		usuarioRepositorio.save(usuarios);

	        		enviaCorreoNotificacionActivaUsuario(usuarios);
	        		response.setEstatus(Constantes.OK);
	            }else {
					response.setEstatus(Constantes.FAIL);
				}
	        }catch (Exception e) {
	        	e.printStackTrace();
	        	response.setEstatus(Constantes.ERROR);
				response.setMensaje(response.getMensaje() + e.getMessage());
			
			}
		 
		return response;
	}
	
	private void enviaCorreoNotificacionActivaUsuario(Usuario usuario)throws Exception {
		
		String de ="";
		String nombreDe ="";
		String destinatarios ="";
		String asunto ="";
		String body ="";
		String servidor ="";
		
		List<Catalogo> listCatalogo = new ArrayList<Catalogo>();
		listCatalogo = iCatalogoService.getListaDatosCatalogo(Constantes.ALTA_USUARIO_CORREO);
		
		
		for(int i = 0 ; i< listCatalogo.size(); i++) {
			if(listCatalogo.get(i).getClave().equals("de")) {
				de = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("nombreDe")) {
				nombreDe = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("destinatarios")) {
				destinatarios = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("asunto")) {
				asunto = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("textoactiva")) {
				body = listCatalogo.get(i).getValor();
				continue;
			}
			
			if(listCatalogo.get(i).getClave().equals("servidor")) {
				servidor = listCatalogo.get(i).getValor();
				continue;
			}
		}
		
		destinatarios = usuario.getEmailInstitucional();
		
		body = body.replace("<user>", usuario.getUsuario());
		//body = body.replace("<email>", usuario.getEmailInstitucional());
		
		
		
		if(so.isWindows()) {
						
		}else if(so.isUnix()) {			
			servidor = servidor + "eipdp";
		}
		
		body = body.replace("<servidor>", servidor);
		
	//	logger.debug(nombreDe);
		System.out.println(nombreDe);
		
		iCorreoService.enviaCorreoNotificacionAltaUsuario();
		iCorreoService.configurarCorreo();
	//	logger.info("de, nombreDe, destinatarios, asunto, body: "+ de+","+ nombreDe+","+destinatarios+","+asunto+","+body);
		iCorreoService.enviarCorreo(de, nombreDe, destinatarios, asunto, body);
		
		
	}
	
	private String encriptarPass(String pass) {
		BCryptPasswordEncoder encoder4 = new BCryptPasswordEncoder(4);
		return encoder4.encode(pass);
	}
	
	@Override
	public ResponseUsuario recuperarUsuarioByEmail(RecuperarUsuarioDTO usuario) {
		ResponseUsuario response = new ResponseUsuario();
		List<Usuario> listaUsuarios = new ArrayList<Usuario>();
		List<UsuarioDTO> listaUsuariosDTO = new ArrayList<UsuarioDTO>();
		
        try {
        	Integer idUsuario = Integer.parseInt(usuario.getEmailInstitucional());
        	Usuario user = usuarioRepositorio.findByIdUsuario(idUsuario);
        	
        	if(user!=null) {        		        	
        		listaUsuarios.add(user);
        	}
        	
        	
        	if(listaUsuarios!=null && listaUsuarios.size() > 0) {
	         	
        		for(Usuario us: listaUsuarios){
        			UsuarioDTO usdto = new UsuarioDTO();
        			usdto.setSujetoObligado(us.getSujetoObligado());
        			usdto.setNombreServidorPublico(us.getNombreServidorPublico());
        			usdto.setCargoServidorPublico(us.getCargoServidorPublico());
        			usdto.setUsuario(us.getUsuario());
        			usdto.setEmailInstitucional(us.getEmailInstitucional());
        			usdto.setTelefono(us.getTelefono());
        			usdto.setExtensionTelefono(us.getExtensionTelefono());
        			usdto.setCelular(us.getCelular());
        			usdto.setActivo(us.getActivo());
        			
        			listaUsuariosDTO.add(usdto);
        		}
        		
        		response.setUsuario(listaUsuariosDTO);
        		response.setEstatus(Constantes.OK);
				response.setMensaje(Constantes.EXISTE);
            }else {
				response.setEstatus(Constantes.NO_EXISTE);
				response.setUsuario(listaUsuariosDTO);
			}
        }catch (Exception e) {
        	e.printStackTrace();
        	response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
			response.setUsuario(listaUsuariosDTO);
			
		}
        
		return response;
	}
}

	
	

