package mx.org.inai.dto;

import java.io.Serializable;

public class FormaRecaban implements Serializable {

	private static final long serialVersionUID = 1L;

	private String personal;
	private String directa;
	private String indirecta;
	
	public String getPersonal() {
		return personal;
	}
	public void setPersonal(String personal) {
		this.personal = personal;
	}
	public String getDirecta() {
		return directa;
	}
	public void setDirecta(String directa) {
		this.directa = directa;
	}
	public String getIndirecta() {
		return indirecta;
	}
	public void setIndirecta(String indirecta) {
		this.indirecta = indirecta;
	}
}
