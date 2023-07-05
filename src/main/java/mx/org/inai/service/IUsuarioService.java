package mx.org.inai.service;

import mx.org.inai.dto.RecuperarUsuarioDTO;
import mx.org.inai.dto.ResponseLogin;
import mx.org.inai.dto.ResponseUsuario;
import mx.org.inai.dto.TokenRecupera;
import mx.org.inai.dto.UsuarioAltaDTO;
import mx.org.inai.dto.UsuarioDTO;

public interface IUsuarioService {
	public ResponseUsuario altaUsuario(UsuarioAltaDTO usuario);
	public ResponseUsuario recuperarUsuario(RecuperarUsuarioDTO usuario);
	public ResponseLogin validaUsuarioToken(TokenRecupera token);
	public ResponseUsuario actualizarPass(UsuarioAltaDTO usuario);
	public ResponseUsuario recuperarUsuarios();
	public ResponseUsuario activarUsuario(RecuperarUsuarioDTO usuario);
	public ResponseUsuario desactivarUsuario(RecuperarUsuarioDTO usuario);
	public ResponseUsuario actualizarUsuario(UsuarioDTO usuario);
	public ResponseUsuario eliminarUsuario(RecuperarUsuarioDTO usuario);
	public ResponseUsuario recuperarUsuarioByEmail(RecuperarUsuarioDTO usuario);	
}
