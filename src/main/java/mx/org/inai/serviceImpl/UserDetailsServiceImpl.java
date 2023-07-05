package mx.org.inai.serviceImpl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import mx.org.inai.dao.UserOauthDAO;
import mx.org.inai.dto.Constantes;
import mx.org.inai.model.Usuario;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserOauthDAO userDAO;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Usuario userInfo = userDAO.getUserInfoByUsuario(userName);
		GrantedAuthority authority = new SimpleGrantedAuthority(Constantes.ROL_DESC);
		return new User(userInfo.getUsuario(), userInfo.getContrasena(), Arrays.asList(authority));
	}
}
