package mx.org.inai.service;

import mx.org.inai.dto.ResponseLogin;
import mx.org.inai.dto.UsuarioDTO;

public interface ILoginService {
	public ResponseLogin validarUsuario(UsuarioDTO usuario);
}
