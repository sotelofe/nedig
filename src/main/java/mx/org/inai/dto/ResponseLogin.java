package mx.org.inai.dto;

import java.util.List;

public class ResponseLogin extends Respuesta {

	private UsuarioDTO usuario;
	private String descPerfil;
	private List<MenuDTO> listaMenus;
	
	public ResponseLogin() {
		estatus = Constantes.ESTATUS;
		mensaje = Constantes.MENSAJE;
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}

	public String getDescPerfil() {
		return descPerfil;
	}

	public void setDescPerfil(String descPerfil) {
		this.descPerfil = descPerfil;
	}

	public List<MenuDTO> getListaMenus() {
		return listaMenus;
	}

	public void setListaMenus(List<MenuDTO> listaMenus) {
		this.listaMenus = listaMenus;
	}
	
	
	
}
