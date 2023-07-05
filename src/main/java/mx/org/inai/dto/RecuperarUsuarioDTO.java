package mx.org.inai.dto;

import java.io.Serializable;

public class RecuperarUsuarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String emailInstitucional;

	public String getEmailInstitucional() {
		return emailInstitucional;
	}

	public void setEmailInstitucional(String emailInstitucional) {
		this.emailInstitucional = emailInstitucional;
	}
}
