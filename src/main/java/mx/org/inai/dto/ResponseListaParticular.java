package mx.org.inai.dto;

import java.util.List;

public class ResponseListaParticular extends Respuesta {

	private List<ListaParticularDTO> listaParticular;

	public ResponseListaParticular() {
	}

	public List<ListaParticularDTO> getListaParticular() {
		return listaParticular;
	}

	public void setListaParticular(List<ListaParticularDTO> listaParticular) {
		this.listaParticular = listaParticular;
	}
}
