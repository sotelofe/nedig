package mx.org.inai.dto;

import java.util.List;

public class TratamientoParticular {
	private Boolean checkParticular;
	private List<ListaParticularDTO> lista;

	public Boolean getCheckParticular() {
		return checkParticular;
	}

	public void setCheckParticular(Boolean checkParticular) {
		this.checkParticular = checkParticular;
	}

	public List<ListaParticularDTO> getLista() {
		return lista;
	}

	public void setLista(List<ListaParticularDTO> lista) {
		this.lista = lista;
	}

}
