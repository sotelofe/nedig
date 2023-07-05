package mx.org.inai.service;

import mx.org.inai.dto.UsuarioDTO;
import mx.org.inai.model.Usuario;

public interface IoauthService {
	public Usuario getUserInfoByUsuario(UsuarioDTO usuario);
}
