package mx.org.inai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mx.org.inai.dto.ResponseListaParticular;
import mx.org.inai.service.IListaParticularService;

@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RestController
@RequestMapping("/tratamientos") 
public class TratamientosController {
	
	@Autowired
	IListaParticularService service;
	
	
	
	@PostMapping(value = "/getLista", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseListaParticular getLista() {      
		return service.getListaParticular();                                                                                                                                                  
    }
	
	
}
