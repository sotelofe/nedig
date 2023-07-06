package mx.org.inai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.org.inai.dto.RequestSecuencia;
import mx.org.inai.dto.ResponseArchivo;
import mx.org.inai.service.IDescargaService;


@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RestController
@RequestMapping("/fichero") 
public class DescargasController {
	
	@Autowired
	IDescargaService iDescargaService;
	
	 @GetMapping(value= "/descargarCuestionarioF1")
	 public ResponseEntity<InputStreamResource> descargarCuestionarioF1(@RequestParam String folio) {
		 return iDescargaService.descargarCuestionarioF1(folio);
	 }
	 
	 @GetMapping(value= "/descargarArchivoPorRuta")
	 public ResponseEntity<InputStreamResource> descargarArchivoPorRuta(@RequestParam String ruta, @RequestParam Integer idFlujo, @RequestParam String carpeta, @RequestParam String pregunta)  {
		 return iDescargaService.descargarArchivoPorRuta(ruta, idFlujo, carpeta, pregunta);
	 }
	 
	 @GetMapping(value= "/descargaDocumentoPorNombreF1")
	 public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF1(@RequestParam String folio)  {
		 return iDescargaService.descargaDocumentoPorNombreF1(folio);
	 }
	 
	 @GetMapping(value= "/descargaDocumentoPorNombreF1RiaSolicitada")
	 public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF1RiaSolicitada(@RequestParam String folio)  {
		 return iDescargaService.descargaDocumentoPorNombreF1RiaSolicitada(folio);
	 }
	 
	 @GetMapping(value= "/descargaDocumentoPorNombreF1RiaEnviada")
	 public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF1RiaEnviada(@RequestParam String folio)  {
		 return iDescargaService.descargaDocumentoPorNombreF1RiaEnviada(folio);
	 }
	 
	 @GetMapping(value= "/descargarCuestionarioF2")
	 public ResponseEntity<InputStreamResource> descargarCuestionarioF2(@RequestParam String folio)  {
		 return iDescargaService.descargarCuestionarioF2(folio);
	 }
	 
	 @GetMapping(value= "/descargaDocumentoPorNombreF2")
	 public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF2(@RequestParam String folio) {
		 return iDescargaService.descargaDocumentoPorNombreF2(folio);
	 }
	 
	 @GetMapping(value= "/descargaDocumentoRiesgosF2")
	 public ResponseEntity<InputStreamResource> descargaDocumentoRiesgosF2(@RequestParam String folio){
		 return iDescargaService.descargaDocumentoRiesgosF2(folio);
	 }
	 
	 @GetMapping(value= "/descargaDocumentoPorNombreF1RiaSolicitada2")
	 public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF1RiaSolicitada2(@RequestParam String folio)  {
		 return iDescargaService.descargaDocumentoPorNombreF1RiaSolicitada2(folio);
	 }
	 
	 @GetMapping(value= "/descargaDocumentoPorNombreF1RiaEnviada2")
	 public ResponseEntity<InputStreamResource> descargaDocumentoPorNombreF1RiaEnviada2(@RequestParam String folio)  {
		 return iDescargaService.descargaDocumentoPorNombreF1RiaEnviada2(folio);
	 }
	 
	 @GetMapping(value= "/descargarFormatos")
	 public ResponseEntity<InputStreamResource> descargarFormatos(@RequestParam String nombre)  {
		 return iDescargaService.descargarFormatos(nombre);
	 }
	 
	 @PostMapping(value= "/descargaArchivoRuta")
	 public ResponseArchivo descargaArchivoRuta(@RequestBody RequestSecuencia request)  {	
		 return iDescargaService.descargaArchivoRuta(request);
	 }
}
