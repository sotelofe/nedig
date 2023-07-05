package mx.org.inai.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mx.org.inai.dao.UserOauthDAO;
import mx.org.inai.model.Usuario;
import mx.org.inai.repository.UserDetailsRepository;

@Repository
public class UserOauthDAOImpl implements UserOauthDAO {
	
	@Autowired
	private UserDetailsRepository userDatailsRepository;

	public Usuario getUserInfoByUsuario(String userName) {
		String enabled = "A";
		return userDatailsRepository.findByUsuarioAndActivo(userName, enabled);
	}

	
}
