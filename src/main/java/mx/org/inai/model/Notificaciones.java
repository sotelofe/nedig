package mx.org.inai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NOTIFICACIONES")
public class Notificaciones {

	@Id
	@GeneratedValue(generator="notificaciones_id_notificacion_seq")
	@Column(name="id_notificacion")
    private Integer idNotificacion;
	
	@Column(name="id_usuario")
	private Integer idUsuario;
	
	@Column(name="para")
	private  String para;
	
	@Column(name="folio")
	private String folio;
	
	@Column(name="asunto")
	private String asunto;
	
	@Column(name="mensaje")
	private String mensaje;
	
	@Column(name="respuesta")
	private String respuesta;
	
	@Column(name="id_estatus")
	private Integer idEstatus;
	
	@Column(name="id_usuario_para")
	private Integer idUsuarioPara;
	
	@Column(name="ruta_solicitud")
	private String rutaSolicitud;
	
	@Column(name="ruta_respuesta")
	private String rutaRespuesta;	
	
	@Column(name="activo")
	private String activo;
	
	@Column(name="fecha")
	private String fecha;
	
	@Column(name="hora")
	private String hora;
	
	

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

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}
}
