package mx.org.inai.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.org.inai.model.Usuario;

@Repository
@Transactional
public interface UserDetailsRepository extends JpaRepository<Usuario, String> {
	public Usuario findByUsuarioAndActivo(String usuario, String activo);	
}
