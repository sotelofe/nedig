package mx.org.inai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.org.inai.dto.NotificacionesDTO;
import mx.org.inai.dto.ResponseNotificaciones;
import mx.org.inai.service.INotificacionesService;

@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RestController
@RequestMapping("/notificaciones") 
public class NotificacionesController {
	
	@Autowired
	INotificacionesService service;
	
	@PostMapping(value = "/alta", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseNotificaciones alta(@RequestBody NotificacionesDTO notificaciones) {      
		return service.alta(notificaciones);                                                                                                                                                  
    }
	
	@PostMapping(value = "/recuperarNotificaciones", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseNotificaciones recuperarNotificaciones(@RequestBody NotificacionesDTO notificaciones) {      
		return service.recuperarNotificaciones(notificaciones);                                                                                                                                                  
    }
	
	@PostMapping(value = "/bajaNotificaciones", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseNotificaciones bajaNotificaciones(@RequestBody NotificacionesDTO notificaciones) {      
		return service.bajaNotificaciones(notificaciones);                                                                                                                                                  
    }
	
	 @GetMapping(value= "/descargaNotificaciones")
	 public ResponseEntity<InputStreamResource> descargaNotificaciones(@RequestParam String ruta) {
		 return service.descargaNotificaciones(ruta);
	 }
	 
	 @PostMapping(value = "/respondeNotificaciones", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	    public ResponseNotificaciones respondeNotificaciones(@RequestBody NotificacionesDTO notificaciones) {      
			return service.respondeNotificaciones(notificaciones);                                                                                                                                                  
	    }
	 
	 @GetMapping(value= "/getAdministradores")
	 public ResponseNotificaciones getAdministradores() {
		 return service.getAdministradores();
	 }
	
	 @GetMapping(value= "/getUsuarios")
	 public ResponseNotificaciones getUsuarios() {
		 return service.getUsuarios();
	 }
	 
	 @PostMapping(value= "/getUsuarioPorFolio", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseNotificaciones getUsuarioPorFolio(@RequestBody NotificacionesDTO notificaciones) {
		 return service.getUsuarioPorFolio(notificaciones);
	 }
	
}
