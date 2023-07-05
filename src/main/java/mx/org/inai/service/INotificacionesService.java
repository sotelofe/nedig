package mx.org.inai.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import mx.org.inai.dto.NotificacionesDTO;
import mx.org.inai.dto.ResponseNotificaciones;

public interface INotificacionesService {
	public ResponseNotificaciones alta(NotificacionesDTO notifiaciones);
	public ResponseNotificaciones recuperarNotificaciones(NotificacionesDTO notifiaciones);	
	public ResponseNotificaciones bajaNotificaciones(NotificacionesDTO notificaciones);
	public ResponseEntity<InputStreamResource> descargaNotificaciones(String ruta);
	public ResponseNotificaciones respondeNotificaciones(NotificacionesDTO notificaciones);
	public ResponseNotificaciones getAdministradores();
	public ResponseNotificaciones getUsuarios();
	public ResponseNotificaciones getUsuarioPorFolio(NotificacionesDTO noti);     
}
