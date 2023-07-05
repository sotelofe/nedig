package mx.org.inai.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import mx.org.inai.dto.RequestSecuencia;
import mx.org.inai.dto.ResponseArchivo;

public interface IDescargaService {
	public ResponseEntity<InputStreamResource> descargarCuestionarioF1(@RequestParam String folio) ;
	public ResponseEntity<InputStreamResource> descargarArchivoPorRuta(@RequestParam String ruta, Integer idFlujo, String carpeta, String pregunta);
	public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF1(@RequestParam String folio);
	public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF1RiaSolicitada(String pfolio) ;
	public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF1RiaEnviada(String pfolio) ;
	public ResponseEntity<InputStreamResource> descargarCuestionarioF2(String folio);
	public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF2(String pfolio);
	public ResponseEntity<InputStreamResource> descargaDocumentoRiesgosF2(String pfolio);
	public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF1RiaSolicitada2(String pfolio);
	public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF1RiaEnviada2(String pfolio);
	public ResponseEntity<InputStreamResource> descargarFormatos(String nombre);
	public ResponseArchivo descargaArchivoRuta(RequestSecuencia request);
	
}
