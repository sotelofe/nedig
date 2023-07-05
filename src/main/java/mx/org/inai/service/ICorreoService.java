package mx.org.inai.service;

public interface ICorreoService {
	public void enviaCorreoNotificacionAltaUsuario();
	public void  configurarCorreo();
	public void enviarCorreo(String de, String nombreDe, String para, String asunto, String body);
}
