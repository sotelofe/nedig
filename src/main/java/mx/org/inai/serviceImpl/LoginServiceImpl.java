package mx.org.inai.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.org.inai.dto.Constantes;
import mx.org.inai.dto.MenuDTO;
import mx.org.inai.dto.ResponseLogin;
import mx.org.inai.dto.UsuarioDTO;
import mx.org.inai.model.Menu;
import mx.org.inai.model.Perfil;
import mx.org.inai.model.Usuario;
import mx.org.inai.repository.MenuRepository;
import mx.org.inai.repository.PerfilRepository;
import mx.org.inai.repository.UsuarioRepository;
import mx.org.inai.service.ILoginService;

@Service
public class LoginServiceImpl implements ILoginService{
	
	@Autowired
	private UsuarioRepository usuarioRepositorio;
	@Autowired
	private MenuRepository menuRepositorio;
	@Autowired
	private PerfilRepository perfilRepositorio;
	
	
	public ResponseLogin validarUsuario(UsuarioDTO usuario) {
		ResponseLogin response = new ResponseLogin();
		
		try {
			
			Usuario usuarios = usuarioRepositorio.findAll().stream().filter(u -> u.getUsuario().equals(usuario.getUsuario()) && u.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
			if(usuarios!=null) {
				response.setUsuario(convertirModeloDTO(usuarios));
				response.setListaMenus(obtenerMenusPermitidos(usuarios.getIdPerfil()));
				response.setDescPerfil((descPerfil(usuarios.getIdPerfil())));
				response.setEstatus("ok");
				response.setMensaje("Login correcto");
				
			}else {
				response.setUsuario(new UsuarioDTO());
				response.setEstatus("fail");
				response.setMensaje("Login Incorrecto");
			}
			
		} catch (Exception e) {
			response.setUsuario(new UsuarioDTO());
			response.setEstatus("error");
			response.setMensaje(response.getMensaje() + e.getMessage());
		}
		
		return response;
	}
	
	private UsuarioDTO convertirModeloDTO(Usuario usuarios) {
		UsuarioDTO udto = new UsuarioDTO();
		udto.setIdUsuario(usuarios.getIdUsuario());
		udto.setIdPerfil(usuarios.getIdPerfil());
		udto.setSujetoObligado(usuarios.getSujetoObligado());
		udto.setNombreServidorPublico(usuarios.getNombreServidorPublico());
		udto.setCargoServidorPublico(usuarios.getCargoServidorPublico());
		udto.setUsuario(usuarios.getUsuario());
		udto.setEmailInstitucional(usuarios.getEmailInstitucional());
		udto.setTelefono(usuarios.getTelefono());
		udto.setExtensionTelefono(usuarios.getExtensionTelefono());
		udto.setCelular(usuarios.getCelular());
		
		return udto;
	}
	
	private List<MenuDTO> obtenerMenusPermitidos(Integer idPerfil)throws Exception{
		
		List<MenuDTO> listaMenus = new ArrayList<MenuDTO>();
		List<Menu> listModelMenus = menuRepositorio.findAll().stream()
				.filter(per -> per.getIdPerfil() == idPerfil && per.getEstatus().equals(Constantes.ACTIVO))
				.collect(Collectors.toList());
		
		listModelMenus.forEach((m)->{
			MenuDTO mto = new MenuDTO();
			mto.setMenu(m.getMenu());
			mto.setMenuNav(m.getMenuNav());
			mto.setDescMenu(m.getDescMenu());
			
			listaMenus.add(mto);
		});
		
		return listaMenus;
	}
	
	private String descPerfil(Integer idPerfil)throws Exception {
		String descPerfil = "";
		Perfil perfil = perfilRepositorio.findAll().stream().filter(p -> p.getIdPerfil() == idPerfil && p.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
		descPerfil = perfil.getDescPerfil();
		return descPerfil;
	}
}
