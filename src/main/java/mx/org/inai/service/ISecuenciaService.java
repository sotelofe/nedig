package mx.org.inai.service;

import mx.org.inai.dto.RequestSecuencia;
import mx.org.inai.dto.ResponseSecuencia;

public interface ISecuenciaService {
	public ResponseSecuencia getSecuencia(RequestSecuencia folio);

	public ResponseSecuencia getComentario(RequestSecuencia folio);

	public ResponseSecuencia altaComentario(RequestSecuencia folio);
}
