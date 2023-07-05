package mx.org.inai.service;

import java.util.List;

import mx.org.inai.dto.AceptarFUnoDTO;
import mx.org.inai.dto.CuestionarioDTO;
import mx.org.inai.dto.FlujoDTO;
import mx.org.inai.dto.FormPresentacionDTO;
import mx.org.inai.dto.ResponseUsuario;
import mx.org.inai.dto.SubeDocumento;
import mx.org.inai.dto.UsuarioDTO;

public interface IFlujo2Service {
	public ResponseUsuario altaFlujo2( FormPresentacionDTO preconsulta);
	public List<FlujoDTO> obtenerListaFlujo2(UsuarioDTO usuario);
	public List<CuestionarioDTO> obtenerDetalleCuestionarioFlujo2(String folio);
	public ResponseUsuario aceptarfdos(AceptarFUnoDTO aceptar);
	public ResponseUsuario guardarReunion(SubeDocumento documento);
	public List<CuestionarioDTO> obtenerListaReunion(SubeDocumento sb);
	public ResponseUsuario actualizarMinuta(AceptarFUnoDTO aceptar);
	public ResponseUsuario subirRiesgos(SubeDocumento documento); 
	public ResponseUsuario subirDictamen(SubeDocumento documento);
	public ResponseUsuario solicitarInforme(SubeDocumento documento);
	public ResponseUsuario subirInforme(SubeDocumento documento);
	public ResponseUsuario aceptarRiaDos(AceptarFUnoDTO aceptar);
	public List<CuestionarioDTO> obtenerDetalleRiaFlujo2(String folio);
	public ResponseUsuario solicitaRia(AceptarFUnoDTO documento);
	public ResponseUsuario subirRiaRequerida(SubeDocumento documento);
	public ResponseUsuario notificaRiaNoPresentada(SubeDocumento documento);
	public List<CuestionarioDTO> obtenerDetalleRiaNoPresentada(String folio);
	public ResponseUsuario acuerdoAdmisionf2(SubeDocumento documento);
	public ResponseUsuario terminarNoVinculantes(SubeDocumento documento);
	
}
