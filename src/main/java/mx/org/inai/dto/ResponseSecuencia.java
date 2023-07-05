package mx.org.inai.dto;

import java.util.List;

public class ResponseSecuencia extends Respuesta{

	private List<SecuenciaFlujoDTO> listaSecuencia;

	public List<SecuenciaFlujoDTO> getListaSecuencia() {
		return listaSecuencia;
	}

	public void setListaSecuencia(List<SecuenciaFlujoDTO> listaSecuencia) {
		this.listaSecuencia = listaSecuencia;
	}
}
