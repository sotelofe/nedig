package mx.org.inai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USUARIOTOKEN")
public class UsuarioToken {

	@Id
	@GeneratedValue(generator="usuariotoken_idusuariotoken_seq")
	@Column(name="idusuariotoken")
    private Integer idUsuarioToken;
	
	@Column(name="usuariotoken")
	private String usuarioToken;
	
	@Column(name="stoken")
	private String sToken;
	
	@Column(name="fecha")
	private String fecha;
	
	@Column(name="activo")
	private String activo;

	public Integer getIdUsuarioToken() {
		return idUsuarioToken;
	}

	public void setIdUsuarioToken(Integer idUsuarioToken) {
		this.idUsuarioToken = idUsuarioToken;
	}

	public String getUsuarioToken() {
		return usuarioToken;
	}

	public void setUsuarioToken(String usuarioToken) {
		this.usuarioToken = usuarioToken;
	}

	public String getsToken() {
		return sToken;
	}

	public void setsToken(String sToken) {
		this.sToken = sToken;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}
}
