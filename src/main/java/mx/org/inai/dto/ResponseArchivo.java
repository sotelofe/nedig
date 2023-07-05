package mx.org.inai.dto;

import java.io.Serializable;

public class ResponseArchivo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String base64;
	private String estatus;
	public String getBase64() {
		return base64;
	}
	public void setBase64(String base64) {
		this.base64 = base64;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	
	
}
