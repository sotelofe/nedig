package mx.org.inai.dto;

import java.io.Serializable;

public class SubeDocumento implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String folio;
	private String email;
	private String asunto;
	private UploadF upload;
	public String getFolio() {
		return folio;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public UploadF getUpload() {
		return upload;
	}
	public void setUpload(UploadF upload) {
		this.upload = upload;
	}
}
