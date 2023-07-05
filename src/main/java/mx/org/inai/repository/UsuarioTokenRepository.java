package mx.org.inai.repository;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mx.org.inai.model.UsuarioToken;

@Repository
@ComponentScan(basePackages = "mx.org.inai.repository")
public interface UsuarioTokenRepository extends JpaRepository<UsuarioToken, Integer>{

	 UsuarioToken findByUsuarioToken(String usuarioToken);
	
	 @Query("SELECT u FROM UsuarioToken u WHERE u.usuarioToken = :usuarioToken ")
	 List<UsuarioToken> findByUsuarioTokenLista(String usuarioToken);
	 
	 @Query("SELECT u FROM UsuarioToken u WHERE u.sToken = :token and u.activo = :activo ")
	 UsuarioToken findBysToken(String token, String activo);
}
