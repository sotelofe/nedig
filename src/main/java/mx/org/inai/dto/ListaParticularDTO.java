package mx.org.inai.dto;

import java.io.Serializable;

import mx.org.inai.model.TrataMientoParticulares;

public class ListaParticularDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean valCheck;
	private String valTitulo;
	private String valText;
	private String valHelp;
	
	public ListaParticularDTO getObjeto(TrataMientoParticulares entity) {
		ListaParticularDTO objeto = new ListaParticularDTO();
		objeto.setValCheck(false);
		objeto.setValTitulo(entity.getTitulo());
		objeto.setValText("");
		objeto.setValHelp(entity.getAyuda());
		
		return objeto;
	}

	public Boolean getValCheck() {
		return valCheck;
	}

	public void setValCheck(Boolean valCheck) {
		this.valCheck = valCheck;
	}

	public String getValTitulo() {
		return valTitulo;
	}

	public void setValTitulo(String valTitulo) {
		this.valTitulo = valTitulo;
	}

	public String getValText() {
		return valText;
	}

	public void setValText(String valText) {
		this.valText = valText;
	}

	public String getValHelp() {
		return valHelp;
	}

	public void setValHelp(String valHelp) {
		this.valHelp = valHelp;
	}
}
