package mx.org.inai.dto;

import java.io.Serializable;

public class UsuarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idUsuario;
	private Integer idPerfil;
	private String sujetoObligado;
	private String nombreServidorPublico;
	private String cargoServidorPublico;
	private String usuario;
	private String contrasena;
	private String emailInstitucional;
	private String telefono;
	private String extensionTelefono;
	private String celular;
	private String activo;
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Integer getIdPerfil() {
		return idPerfil;
	}
	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}
	public String getSujetoObligado() {
		return sujetoObligado;
	}
	public void setSujetoObligado(String sujetoObligado) {
		this.sujetoObligado = sujetoObligado;
	}
	public String getNombreServidorPublico() {
		return nombreServidorPublico;
	}
	public void setNombreServidorPublico(String nombreServidorPublico) {
		this.nombreServidorPublico = nombreServidorPublico;
	}
	public String getCargoServidorPublico() {
		return cargoServidorPublico;
	}
	public void setCargoServidorPublico(String cargoServidorPublico) {
		this.cargoServidorPublico = cargoServidorPublico;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getContrasena() {
		return contrasena;
	}
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	public String getEmailInstitucional() {
		return emailInstitucional;
	}
	public void setEmailInstitucional(String emailInstitucional) {
		this.emailInstitucional = emailInstitucional;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getExtensionTelefono() {
		return extensionTelefono;
	}
	public void setExtensionTelefono(String extensionTelefono) {
		this.extensionTelefono = extensionTelefono;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getActivo() {
		return activo;
	}
	public void setActivo(String activo) {
		this.activo = activo;
	}
}
