package mx.org.inai.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import mx.org.inai.dto.AceptarFUnoDTO;
import mx.org.inai.dto.CuestionarioDTO;
import mx.org.inai.dto.FlujoDTO;
import mx.org.inai.dto.FormPreconsultaDTO;
import mx.org.inai.dto.ResponseUsuario;
import mx.org.inai.dto.SubeDocumento;
import mx.org.inai.dto.UsuarioDTO;

public interface IFlujoService {
	public ResponseUsuario altaFlujo1( FormPreconsultaDTO preconsulta);
	public List<FlujoDTO> obtenerListaFlujo1(UsuarioDTO usuario);
	public List<CuestionarioDTO> obtenerDetalleCuestionarioFlujo1(String folio);
	public ResponseUsuario aceptarfuno(AceptarFUnoDTO aceptar);
	public ResponseUsuario generarOpinion(SubeDocumento documento); 
	public List<CuestionarioDTO> obtenerDetalleOpinionFlujo1(String folio);
	public ResponseUsuario riafuno(AceptarFUnoDTO ria);
	public List<CuestionarioDTO> obtenerDetalleRiaFlujo1(String folio);
	public ResponseUsuario subirRiaRequerida(@RequestBody SubeDocumento documento); 
	public List<CuestionarioDTO> obtenerDetalleRiaFlujo12(String folio);
	public ResponseUsuario aceptarRiaUno(AceptarFUnoDTO aceptar);
	public ResponseUsuario notificaRiaNoPresentada(SubeDocumento documento);
	public List<CuestionarioDTO> obtenerDetalleRiaNoPresentada(String folio);
	public ResponseUsuario acuerdoAdmision(SubeDocumento documento);
}
