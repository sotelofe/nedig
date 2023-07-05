package mx.org.inai.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.org.inai.dto.Constantes;
import mx.org.inai.model.Catalogo;
import mx.org.inai.repository.CatalogoRepository;
import mx.org.inai.service.ICatalogoService;

@Service
public class CatalogoService implements ICatalogoService{
	
	
	
	@Autowired
	private CatalogoRepository catalogoRepositorio;

	@Override
	public List<Catalogo> getListaDatosCatalogo(String clave) {
		Catalogo catalogo = catalogoRepositorio.findAll().stream().filter(c -> c.getClave().equals(clave) && c.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
		List<Catalogo> listCatalogo = catalogoRepositorio.findAll().stream().filter(ca ->  ca.getIdSubCatalogo() == catalogo.getIdCatalogo()).collect(Collectors.toList());
		return listCatalogo;
	}
	
	
	
}
