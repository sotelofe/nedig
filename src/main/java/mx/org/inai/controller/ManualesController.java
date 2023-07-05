package mx.org.inai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.org.inai.dto.ManualesDTO;
import mx.org.inai.dto.ResponseArchivo;
import mx.org.inai.service.IManualesService;

@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RestController
@RequestMapping("/manuales")
public class ManualesController {
	
	@Autowired
	IManualesService service;
	
	@GetMapping(value = "/obtenerLista", produces = MediaType.APPLICATION_JSON_VALUE)   
    public List<ManualesDTO>  obtenerLista() {      
		return service.obtenerLista();                                                                                                                                                  
    }
	
	@GetMapping(value = "/getDocumento")   
    public byte[]  getDocumento(@RequestParam(value="nombredocumento", required=true) String nombreDocumento) {      
		return service.getDocumento(nombreDocumento);                                                                                                                                                  
    }
	
	@GetMapping(value = "/descargaDocumento", produces = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseArchivo  descargaDocumento(@RequestParam(value="nombredocumento", required=true) String nombreDocumento) {      
		return service.descargaDocumento(nombreDocumento);                                                                                                                                                  
    }
	
	
}
