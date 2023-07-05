package mx.org.inai.dto;

import java.util.List;

public class ResponseUsuario extends Respuesta {
	
	private List<UsuarioDTO> usuario;
	
	public ResponseUsuario() {
		estatus = Constantes.ESTATUS;
		mensaje = Constantes.MENSAJE;
	}

	public List<UsuarioDTO> getUsuario() {
		return usuario;
	}

	public void setUsuario(List<UsuarioDTO> usuario) {
		this.usuario = usuario;
	}
	
}
