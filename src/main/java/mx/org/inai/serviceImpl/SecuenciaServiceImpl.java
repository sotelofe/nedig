package mx.org.inai.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.org.inai.dto.Constantes;
import mx.org.inai.dto.RequestSecuencia;
import mx.org.inai.dto.ResponseSecuencia;
import mx.org.inai.dto.SecuenciaFlujoDTO;
import mx.org.inai.model.Cuestionario;
import mx.org.inai.repository.CuestionarioRepository;
import mx.org.inai.repository.SecuenciaFlujoRepository;
import mx.org.inai.service.ISecuenciaService;

@Service
public class SecuenciaServiceImpl implements ISecuenciaService{

	@Autowired
	private SecuenciaFlujoRepository repositorySecuencia;
	
	@Autowired
	private CuestionarioRepository repoCuestionario;
	
	private ModelMapper modelMapper = new ModelMapper();
	
	@Override
	public ResponseSecuencia getSecuencia(RequestSecuencia folio) {
		ResponseSecuencia response = new ResponseSecuencia();
		List<SecuenciaFlujoDTO> listaSecuencia = new ArrayList<SecuenciaFlujoDTO>();
		
		try {
			List<SecuenciaFlujoDTO> lista =  repositorySecuencia.findByFolioOrderByIdSecuencia(folio.getFolio()).stream().map(m-> modelMapper.map(m,SecuenciaFlujoDTO.class)).collect(Collectors.toList());
			lista.forEach(f->{
				f.setFecha(formatearFecha(f.getFecha()));
			});
			response.setListaSecuencia(lista);
			response.setEstatus(Constantes.OK);
			response.setMensaje(Constantes.OK);
		} catch (Exception e) {
			response.setListaSecuencia(listaSecuencia);
			response.setEstatus(Constantes.FAIL);
			response.setMensaje(e.getMessage());
			e.printStackTrace();
		}
		
		return response;
	}
	
	String formatearFecha(String f) {
		String fecha="";
		String[] afecha = f.split("-");
		fecha = afecha[2]+"/"+afecha[1]+"/"+afecha[0];		
		return fecha;
	}
	
	@Override
	public ResponseSecuencia getComentario(RequestSecuencia folio) {
		ResponseSecuencia response = new ResponseSecuencia();
		
		try {
			Cuestionario cue = repoCuestionario.findByFolioAndPreguntaAndSubPregunta(folio.getFolio(),50,1);
			if(cue != null){
				response.setMensaje(cue.getRespuesta());
				response.setEstatus(Constantes.OK);
			}
		} catch (Exception e) {
			response.setMensaje(e.getMessage());
			response.setEstatus(Constantes.FAIL);
			e.printStackTrace();
		}
		
		return response;
	}
	
	@Override
	public ResponseSecuencia altaComentario(RequestSecuencia folio) {
		ResponseSecuencia response = new ResponseSecuencia();
		
		try {
			Cuestionario cue = repoCuestionario.findByFolioAndPreguntaAndSubPregunta(folio.getFolio(),50,1);
			if(cue==null) {
				Cuestionario c = new Cuestionario();
				c.setFolio(folio.getFolio());
				c.setPregunta(50);
				c.setSubPregunta(1);
				c.setRespuesta(folio.getMensaje());
				c.setActivo(Constantes.ACTIVO);
				repoCuestionario.save(c);
			}else {
				cue.setRespuesta(folio.getMensaje());
				repoCuestionario.save(cue);
			}
			
			response.setMensaje(Constantes.OK);
			response.setEstatus(Constantes.OK);
			
		} catch (Exception e) {
			response.setMensaje(e.getMessage());
			response.setEstatus(Constantes.FAIL);
			e.printStackTrace();
		}
		
		return response;
	}
	
}

	
	

