package mx.org.inai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mx.org.inai.dto.RecuperarUsuarioDTO;
import mx.org.inai.dto.ResponseLogin;
import mx.org.inai.dto.ResponseUsuario;
import mx.org.inai.dto.TokenRecupera;
import mx.org.inai.dto.UsuarioAltaDTO;
import mx.org.inai.dto.UsuarioDTO;
import mx.org.inai.service.IUsuarioService;

@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RestController
@RequestMapping("/usuario") 
public class UsuarioController {
	
	@Autowired
	IUsuarioService iUsuarioService;
	
	@PostMapping(value = "/alta", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseUsuario alta(@RequestBody UsuarioAltaDTO usuario) {      
		return iUsuarioService.altaUsuario(usuario);                                                                                                                                                  
    }
	
	@PostMapping(value = "/recuperarUsuario", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseUsuario recuperarUsuario(@RequestBody RecuperarUsuarioDTO usuario) {      
		return iUsuarioService.recuperarUsuario(usuario);                                                                                                                                                  
    }
	
	@PostMapping(value = "/validaUsuarioToken", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseLogin validaUsuarioToken(@RequestBody TokenRecupera token) {      
		return iUsuarioService.validaUsuarioToken(token);                                                                                                                                                  
    }
	
	@PostMapping(value = "/actualizarPass", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseUsuario actualizarPass(@RequestBody UsuarioAltaDTO usuario) {      
		return iUsuarioService.actualizarPass(usuario);                                                                                                                                                  
    }	
	
	@PostMapping(value = "/recuperarUsuarios", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseUsuario recuperarUsuarios() {      
		return iUsuarioService.recuperarUsuarios();                                                                                                                                                  
    }	
	
	@PostMapping(value = "/activarUsuario", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseUsuario activarUsuario(@RequestBody RecuperarUsuarioDTO usuario) {      
		return iUsuarioService.activarUsuario(usuario);                                                                                                                                                  
    }
	
	@PostMapping(value = "/eliminarUsuario", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseUsuario eliminarUsuario(@RequestBody RecuperarUsuarioDTO usuario) {      
		return iUsuarioService.eliminarUsuario(usuario);                                                                                                                                                  
    }
	
	@PostMapping(value = "/desactivarUsuario", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseUsuario desactivarUsuario(@RequestBody RecuperarUsuarioDTO usuario) {      
		return iUsuarioService.desactivarUsuario(usuario);                                                                                                                                                  
    }
	
	@PostMapping(value = "/actualizarUsuario", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseUsuario actualizarUsuario(@RequestBody UsuarioDTO usuario) {      
		return iUsuarioService.actualizarUsuario(usuario);                                                                                                                                                  
    }
	
	@PostMapping(value = "/recuperarUsuarioByEmail", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseUsuario recuperarUsuarioByEmail(@RequestBody RecuperarUsuarioDTO usuario) {      
		return iUsuarioService.recuperarUsuarioByEmail(usuario);                                                                                                                                                  
    }
	
}
