package mx.org.inai.service;

import java.util.List;

import mx.org.inai.model.Catalogo;

public interface ICatalogoService {
	public List<Catalogo> getListaDatosCatalogo(String clave);
}
