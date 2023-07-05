package mx.org.inai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USUARIO")
public class Usuario {

	@Id
	@GeneratedValue(generator="usuario_idusuario_seq")
	@Column(name="idusuario")
    private Integer idUsuario;
	
	@Column(name="idperfil")
	private Integer idPerfil;
	
	@Column(name="sujetoobligado")
	private String sujetoObligado;
	
	@Column(name="nombreservidorpublico")
	private String nombreServidorPublico;
	
	@Column(name="cargoservidorpublico")
	private String cargoServidorPublico;
	
	@Column(name="usuario")
	private String usuario;
	
	@Column(name="contrasena")
	private String contrasena;
	
	@Column(name="emailinstitucional")
	private String emailInstitucional;
	
	@Column(name="telefono")
	private String telefono;
	
	@Column(name="extensiontelefono")
	private String extensionTelefono;
	
	@Column(name="celular")
	private String celular;
	
	@Column(name="activo")
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
