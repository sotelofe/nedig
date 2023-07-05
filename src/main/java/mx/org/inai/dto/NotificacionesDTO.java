package mx.org.inai.dto;

import mx.org.inai.model.Notificaciones;

public class NotificacionesDTO {

	private Integer idNotificacion;
	private Integer idUsuario;
	private Integer idPerfil;
	private String folio;
	private String asunto;
	private String mensaje;
	private String respuesta;
	private Integer idEstatus;
	private Integer idUsuarioPara;
	private String rutaSolicitud;
	private String rutaRespuesta;
	private String activo;
	private String fecha;
	private String hora;
	private String para;
	private UploadF upload;
	private Integer ipara;
	private String de;
	private String tipoMensaje;
	
	public NotificacionesDTO() {
		
	}
	
	public NotificacionesDTO(Notificaciones en) {
		this.idNotificacion = en.getIdNotificacion();
		this.idUsuario = en.getIdUsuario();
		this.folio = en.getFolio();
		this.asunto = en.getAsunto();
		this.mensaje = en.getMensaje();
		this.respuesta = en.getRespuesta();
		this.idEstatus = en.getIdEstatus();
		this.idUsuarioPara = en.getIdUsuarioPara();
		this.rutaSolicitud = en.getRutaSolicitud();
		this.rutaRespuesta = en.getRutaRespuesta();
		this.activo = en.getActivo();
		this.fecha = en.getFecha();
		this.hora = en.getHora();
		this.para = en.getPara();
		
	}
	
	
	public Notificaciones getEntidad(NotificacionesDTO noti) {
		Notificaciones entity = new Notificaciones();		
		entity.setIdUsuario(noti.getIdUsuario());
		entity.setFolio(noti.getFolio());
		entity.setAsunto(noti.getAsunto());
		entity.setMensaje(noti.getMensaje());
		entity.setRespuesta(noti.getRespuesta());
		entity.setIdEstatus(noti.getIdEstatus());
		entity.setIdUsuarioPara(noti.getIdUsuarioPara());
		entity.setRutaSolicitud(noti.getRutaSolicitud());
		entity.setRutaRespuesta(noti.getRutaRespuesta());
		entity.setActivo(noti.getActivo());
		entity.setFecha(noti.getFecha());
		entity.setHora(noti.getHora());
		entity.setPara(noti.getPara());		
		return entity;
	}

	public Integer getIdNotificacion() {
		return idNotificacion;
	}

	public void setIdNotificacion(Integer idNotificacion) {
		this.idNotificacion = idNotificacion;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public Integer getIdEstatus() {
		return idEstatus;
	}

	public void setIdEstatus(Integer idEstatus) {
		this.idEstatus = idEstatus;
	}

	public Integer getIdUsuarioPara() {
		return idUsuarioPara;
	}

	public void setIdUsuarioPara(Integer idUsuarioPara) {
		this.idUsuarioPara = idUsuarioPara;
	}

	public String getRutaSolicitud() {
		return rutaSolicitud;
	}

	public void setRutaSolicitud(String rutaSolicitud) {
		this.rutaSolicitud = rutaSolicitud;
	}

	public String getRutaRespuesta() {
		return rutaRespuesta;
	}

	public void setRutaRespuesta(String rutaRespuesta) {
		this.rutaRespuesta = rutaRespuesta;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public UploadF getUpload() {
		return upload;
	}

	public void setUpload(UploadF upload) {
		this.upload = upload;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public Integer getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	public Integer getIpara() {
		return ipara;
	}

	public void setIpara(Integer ipara) {
		this.ipara = ipara;
	}

	public String getDe() {
		return de;
	}

	public void setDe(String de) {
		this.de = de;
	}

	public String getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(String tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}
}
