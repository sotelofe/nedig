package mx.org.inai.dto;

import java.util.List;

public class ResponseNotificaciones extends Respuesta {
	
	private List<NotificacionesDTO> notificaciones;
	
	public ResponseNotificaciones() {
		estatus = Constantes.ESTATUS;
		mensaje = Constantes.MENSAJE;
	}

	public List<NotificacionesDTO> getNotificaciones() {
		return notificaciones;
	}

	public void setNotificaciones(List<NotificacionesDTO> notificaciones) {
		this.notificaciones = notificaciones;
	}

}
