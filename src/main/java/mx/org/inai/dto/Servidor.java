package mx.org.inai.dto;

import java.io.Serializable;

public class Servidor implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nombre;
    private String cargo;
	
    public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
}
