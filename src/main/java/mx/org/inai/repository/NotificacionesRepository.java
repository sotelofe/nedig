package mx.org.inai.repository;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.org.inai.model.Notificaciones;

@Repository
@ComponentScan(basePackages = "mx.org.inai.repository")
public interface NotificacionesRepository extends JpaRepository<Notificaciones, Integer>{
	
	List<Notificaciones> findByActivo(String activo);
	List<Notificaciones> findByIdUsuarioOrIdUsuarioParaAndActivo(Integer idUsuario, Integer sidUsuario, String activo);
	Notificaciones findByFolioAndIdNotificacion(String folio, Integer idNotificacion);
}
