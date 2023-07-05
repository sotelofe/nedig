package mx.org.inai.service;

import java.util.List;

import mx.org.inai.dto.ManualesDTO;
import mx.org.inai.dto.ResponseArchivo;

public interface IManualesService {

	public List<ManualesDTO>  obtenerLista();

	public byte[] getDocumento(String nombreDocumento);

	public ResponseArchivo descargaDocumento(String nombreDocumento);
}
