package mx.org.inai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mx.org.inai.dto.RequestSecuencia;
import mx.org.inai.dto.ResponseSecuencia;
import mx.org.inai.service.ISecuenciaService;

@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RestController
@RequestMapping("/historico") 
public class SecuenciaController {
	
	@Autowired
	ISecuenciaService secuenciaService;
	
	@PostMapping(value = "/getSecuencia", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseSecuencia getSecuencia(@RequestBody RequestSecuencia folio) {      
		return secuenciaService.getSecuencia(folio);                                                                                                                                                  
    }
	
	@PostMapping(value = "/getComentario", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseSecuencia getComentario(@RequestBody RequestSecuencia folio) {      
		return secuenciaService.getComentario(folio);                                                                                                                                                  
    }
	
	@PostMapping(value = "/altaComentario", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseSecuencia altaComentario(@RequestBody RequestSecuencia folio) {      
		return secuenciaService.altaComentario(folio);                                                                                                                                                  
    }
}
