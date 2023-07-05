package mx.org.inai.dto;

public class UsuarioTokenDTO {
	
    private Integer idUsuarioToken;
	private String usuarioToken;
	private String sToken;
	private String fecha;
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
