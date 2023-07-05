package mx.org.inai.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mx.org.inai.dto.ResponseLogin;
import mx.org.inai.dto.UsuarioDTO;
import mx.org.inai.service.ILoginService;

@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RestController
@RequestMapping("/login") 
public class LoginController {

	
	
	@Autowired
	ILoginService iLoginService;
	
	@PostMapping(value = "/acceso", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseLogin acceso(@RequestBody UsuarioDTO usuario) {      		
		return iLoginService.validarUsuario(usuario); 
    }
}
