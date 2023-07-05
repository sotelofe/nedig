package mx.org.inai.dto;

import java.io.Serializable;

public class UsuarioAltaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sujetoObligado;
	private String nombreServidorPublico;
	private String cargoServidorPublico;
	private String usuario;
	private String contrasena;
	private String repetirContrasena;
	private String emailInstitucional;
	private String telefono;
	private String extensionTelefono;
	private String celular;
	
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
	public String getRepetirContrasena() {
		return repetirContrasena;
	}
	public void setRepetirContrasena(String repetirContrasena) {
		this.repetirContrasena = repetirContrasena;
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
}
