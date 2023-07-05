package mx.org.inai.serviceImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.org.inai.dto.Constantes;
import mx.org.inai.dto.ListaParticularDTO;
import mx.org.inai.dto.ResponseListaParticular;
import mx.org.inai.model.TrataMientoParticulares;
import mx.org.inai.repository.ParticularesRepository;
import mx.org.inai.service.IListaParticularService;

@Service
public class ListaParticularServiceImpl implements IListaParticularService{
	
	//private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ParticularesRepository repository;
	
	
	
	
	public ResponseListaParticular getListaParticular() {
		ResponseListaParticular response = new ResponseListaParticular();
		List<TrataMientoParticulares> lp = new ArrayList<TrataMientoParticulares>();
		List<ListaParticularDTO> lpd = new ArrayList<ListaParticularDTO>();
        try {
			        	
        	//lp = repository.findAll().stream().filter(p -> p.getActivo().equals("A")).collect(Collectors.toList());
        	lp = repository.findByActivo(Constantes.ACTIVO);
        	lp = lp.stream().sorted(Comparator.comparing(TrataMientoParticulares::getOrden)).collect(Collectors.toList());
        	
        	if(lp.size()>0) {	         				
					response.setEstatus(Constantes.OK);
					response.setMensaje(Constantes.LISTA_PARTICULARES_CORRECTA);
					lp.forEach(a->{
						ListaParticularDTO pd = new ListaParticularDTO();						
						lpd.add(pd.getObjeto(a));
					});
					response.setListaParticular(lpd);
				
            }else {
				response.setEstatus(Constantes.OK);
				response.setMensaje(Constantes.LISTA_PARTICULARES_SIN_DATOS);
				response.setListaParticular(lpd);
			}
			
		} catch (Exception e) {
			response.setEstatus(Constantes.ERROR);
			response.setMensaje(response.getMensaje() + e.getMessage());
			//logger.info(e.getMessage());
		}
		
		
		return response;
	}
	
	
}

	
	

