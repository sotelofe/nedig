package mx.org.inai.dto;

import java.io.Serializable;

public class DocumentosDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nombre;
	private byte[] data;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
	
	
	
	
}
