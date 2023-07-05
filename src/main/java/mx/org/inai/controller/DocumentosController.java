package mx.org.inai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mx.org.inai.dto.DocumentosDTO;
import mx.org.inai.service.IDocumentosService;

@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RestController
@RequestMapping("/documentos")
public class DocumentosController {
	
	@Autowired
	IDocumentosService iDocumentosService;
	
	@PostMapping(value = "/aviso")   
    public byte[]  aviso() {      
		return iDocumentosService.aviso();                                                                                                                                                  
    }
	
	
	@PostMapping(value = "/avison")   
    public DocumentosDTO  avison() {      
		return iDocumentosService.avison();                                                                                                                                                  
    }
	
	
}
