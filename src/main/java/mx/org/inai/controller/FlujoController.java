package mx.org.inai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mx.org.inai.dto.AceptarFUnoDTO;
import mx.org.inai.dto.CuestionarioDTO;
import mx.org.inai.dto.FlujoDTO;
import mx.org.inai.dto.FormPreconsultaDTO;
import mx.org.inai.dto.FormPresentacionDTO;
import mx.org.inai.dto.RecuperarUsuarioDTO;
import mx.org.inai.dto.ResponseUsuario;
import mx.org.inai.dto.SubeDocumento;
import mx.org.inai.dto.UsuarioDTO;
import mx.org.inai.service.IFlujo2Service;
import mx.org.inai.service.IFlujoService;

@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RestController
@RequestMapping("/eipdp") 
public class FlujoController {
	
	@Autowired
	IFlujoService iFlujoService;
	
	@Autowired
	IFlujo2Service iFlujo2Service;
	
	@PostMapping(value = "/flujo1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseUsuario altaFlujo1(@RequestBody FormPreconsultaDTO preconsulta) {      
		return iFlujoService.altaFlujo1(preconsulta);                                                                                                                                                  
    }
	
	@PostMapping(value = "/obtenerListaFlujo1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public List<FlujoDTO> obtenerListaFlujo1(@RequestBody UsuarioDTO usuario) {      
		return iFlujoService.obtenerListaFlujo1(usuario);                                                                                                                                                  
    }
	
	@PostMapping(value = "/obtenerDetalleCuestionarioFlujo1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public List<CuestionarioDTO> obtenerDetalleCuestionarioFlujo1(@RequestBody RecuperarUsuarioDTO folio) {      
		return iFlujoService.obtenerDetalleCuestionarioFlujo1(folio.getEmailInstitucional());                                                                                                                                                  
    }
	
	@PostMapping(value = "/aceptarfuno", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario aceptarfuno(@RequestBody AceptarFUnoDTO aceptar) {      
		return iFlujoService.aceptarfuno(aceptar);                                                                                                                                                  
    }
	
	@PostMapping(value = "/generarOpinion", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario generarOpinion(@RequestBody SubeDocumento documento) {      
		return iFlujoService.generarOpinion(documento);          				
    }
	
	@PostMapping(value = "/acuerdoAdmision", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario acuerdoAdmision(@RequestBody SubeDocumento documento) {      
		return iFlujoService.acuerdoAdmision(documento);          				
    }
	
	@PostMapping(value = "/obtenerDetalleOpinionFlujo1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public List<CuestionarioDTO> obtenerDetalleOpinionFlujo1(@RequestBody RecuperarUsuarioDTO folio) {      
		return iFlujoService.obtenerDetalleOpinionFlujo1(folio.getEmailInstitucional());                                                                                                                                                  
    }
	
	@PostMapping(value = "/riafuno", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario riafuno(@RequestBody AceptarFUnoDTO ria) {      
		return iFlujoService.riafuno(ria);                                                                                                                                                  
    }
	
	@PostMapping(value = "/obtenerDetalleRiaFlujo1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public List<CuestionarioDTO> obtenerDetalleRiaFlujo1(@RequestBody RecuperarUsuarioDTO folio) {      
		return iFlujoService.obtenerDetalleRiaFlujo1(folio.getEmailInstitucional());                                                                                                                                                  
    }
	
	@PostMapping(value = "/obtenerDetalleRiaFlujo12", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public List<CuestionarioDTO> obtenerDetalleRiaFlujo12(@RequestBody RecuperarUsuarioDTO folio) {      
		return iFlujoService.obtenerDetalleRiaFlujo12(folio.getEmailInstitucional());                                                                                                                                                  
    }
	
	@PostMapping(value = "/subirRiaRequerida", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario subirRiaRequerida(@RequestBody SubeDocumento documento) {      
		return iFlujoService.subirRiaRequerida(documento);                                                                                                                                                  
    }
	
	@PostMapping(value = "/aceptarRiaUno", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario aceptarRiaUno(@RequestBody AceptarFUnoDTO aceptarRia) {      
		return iFlujoService.aceptarRiaUno(aceptarRia);                                                                                                                                                  
    }
	
	@PostMapping(value = "/flujo2", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public ResponseUsuario altaFlujo2(@RequestBody FormPresentacionDTO preconsulta) {      
		return iFlujo2Service.altaFlujo2(preconsulta);                                                                                                                                                  
    }
	
	@PostMapping(value = "/obtenerListaFlujo2", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
    public List<FlujoDTO> obtenerListaFlujo2(@RequestBody UsuarioDTO usuario) {      
		return iFlujo2Service.obtenerListaFlujo2(usuario);                                                                                                                                                  
    }
	
	@PostMapping(value = "/obtenerDetalleCuestionarioFlujo2", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public List<CuestionarioDTO> obtenerDetalleCuestionarioFlujo2(@RequestBody RecuperarUsuarioDTO folio) {      
		return iFlujo2Service.obtenerDetalleCuestionarioFlujo2(folio.getEmailInstitucional());                                                                                                                                                  
    }
	
	@PostMapping(value = "/aceptarfdos", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario aceptarfdos(@RequestBody AceptarFUnoDTO aceptar) {      
		return iFlujo2Service.aceptarfdos(aceptar);                                                                                                                                                  
    }
	
	@PostMapping(value = "/guardarReunion", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario guardarReunion(@RequestBody SubeDocumento documento) {      
		return iFlujo2Service.guardarReunion(documento);                                                                                                                                                  
    }
	
	@PostMapping(value = "/obtenerListaReunion", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<CuestionarioDTO> obtenerListaReunion(@RequestBody SubeDocumento sb) {
		return iFlujo2Service.obtenerListaReunion(sb);
	}
	
	@PostMapping(value = "/actualizarMinuta", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario actualizarMinuta(@RequestBody AceptarFUnoDTO aceptar) {      
		return iFlujo2Service.actualizarMinuta(aceptar);                                                                                                                                                  
    }
	
	@PostMapping(value = "/subirRiesgos", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario subirRiesgos(@RequestBody SubeDocumento documento) {      
		return iFlujo2Service.subirRiesgos(documento);                                                                                                                                                  
    }
	
	@PostMapping(value = "/subirDictamen", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario subirDictamen(@RequestBody SubeDocumento documento) {      
		return iFlujo2Service.subirDictamen(documento);                                                                                                                                                  
    }
	
	@PostMapping(value = "/solicitarInforme", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario solicitarInforme(@RequestBody SubeDocumento documento) {      
		return iFlujo2Service.solicitarInforme(documento);                                                                                                                                                  
    }
	
	@PostMapping(value = "/subirInforme", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario subirInforme(@RequestBody SubeDocumento documento) {      
		return iFlujo2Service.subirInforme(documento);                                                                                                                                                  
    }
	
	@PostMapping(value = "/riafdos", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario riafdos(@RequestBody AceptarFUnoDTO ria) {      
		return iFlujo2Service.solicitaRia(ria);                                                                                                                                                  
    }
	
	@PostMapping(value = "/obtenerDetalleRiaFlujo2", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public List<CuestionarioDTO> obtenerDetalleRiaFlujo2(@RequestBody RecuperarUsuarioDTO folio) {      
		return iFlujo2Service.obtenerDetalleRiaFlujo2(folio.getEmailInstitucional());                                                                                                                                                  
    }
	
	@PostMapping(value = "/subirRiaRequerida2", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario subirRiaRequerida2(@RequestBody SubeDocumento documento) {      
		return iFlujo2Service.subirRiaRequerida(documento);                                                                                                                                                  
    }
	
	@PostMapping(value = "/aceptarRiaDos", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario aceptarRia2(@RequestBody AceptarFUnoDTO aceptarRia) {      
		return iFlujo2Service.aceptarRiaDos(aceptarRia);                                                                                                                                                  
    }
	
	@PostMapping(value = "/notificaRiaNoPresentada", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario notificaRiaNoPresentada1(@RequestBody SubeDocumento documento) {      
		return iFlujoService.notificaRiaNoPresentada(documento);                                                                                                                                                  
    }
	
	@PostMapping(value = "/obtenerDetalleRiaNoPresentada", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public List<CuestionarioDTO> obtenerDetalleRiaNoPresentada1(@RequestBody RecuperarUsuarioDTO folio) {      
		return iFlujoService.obtenerDetalleRiaNoPresentada(folio.getEmailInstitucional());                                                                                                                                                  
    }
	
	@PostMapping(value = "/notificaRiaNoPresentadaDos", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario notificaRiaNoPresentada(@RequestBody SubeDocumento documento) {      
		return iFlujo2Service.notificaRiaNoPresentada(documento);                                                                                                                                                  
    }
	
	@PostMapping(value = "/obtenerDetalleRiaNoPresentadaDos", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public List<CuestionarioDTO> obtenerDetalleRiaNoPresentada(@RequestBody RecuperarUsuarioDTO folio) {      
		return iFlujo2Service.obtenerDetalleRiaNoPresentada(folio.getEmailInstitucional());                                                                                                                                                  
    }
	
	@PostMapping(value = "/acuerdoAdmisionf2", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario acuerdoAdmisionf2(@RequestBody SubeDocumento documento) {      
		return iFlujo2Service.acuerdoAdmisionf2(documento);          				
    }
	
	@PostMapping(value = "/terminarNoVinculantes", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)   
	public ResponseUsuario terminarNoVinculantes(@RequestBody SubeDocumento documento) {      
		return iFlujo2Service.terminarNoVinculantes(documento);          				
    }
}
