package mx.org.inai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mx.org.inai.model.Usuario;

@Repository
@ComponentScan(basePackages = "mx.org.inai.repository")
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	@Query("SELECT u FROM Usuario u WHERE u.idUsuario = :idUsuario AND u.activo='A'")
    Optional<Usuario> findUsuario(Integer idUsuario);
	
	Usuario findByIdUsuarioAndActivo(Integer idUsuario, String activo);
	
	Usuario findByUsuarioAndEmailInstitucional(String usuario, String email);
	Usuario findByEmailInstitucionalAndActivo(String email, String activo);
	Usuario findByUsuario(String idUsuario);
	Usuario findByEmailInstitucional(String email);	
	List<Usuario> findByActivoOrActivo(String acu, String actd);
	List<Usuario> findByIdPerfil(Integer idPerfil);
	Usuario findByIdUsuario(Integer idUsuario);
}
