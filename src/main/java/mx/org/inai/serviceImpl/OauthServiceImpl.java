package mx.org.inai.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.org.inai.dao.UserOauthDAO;
import mx.org.inai.dto.UsuarioDTO;
import mx.org.inai.model.Usuario;
import mx.org.inai.service.IoauthService;

@Service
public class OauthServiceImpl implements IoauthService {
	
	@Autowired
	UserOauthDAO dao;

	@Override
	public Usuario getUserInfoByUsuario(UsuarioDTO usuario) {
		Usuario eusuario = dao.getUserInfoByUsuario(usuario.getUsuario()); 
		return eusuario;
	}

}
