package mx.org.inai.dao;

import mx.org.inai.model.Usuario;

public interface UserOauthDAO {	
	public Usuario getUserInfoByUsuario(String userName);
}
