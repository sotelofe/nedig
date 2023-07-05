package mx.org.inai.transform.exencion;

import java.util.Optional;
import mx.org.inai.dto.exencion.ExencionRequestDTO;
import mx.org.inai.model.Folio;
import mx.org.inai.dto.Constantes;
import mx.org.inai.repository.FolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * The Class FolioTransform.
 *
 * @author A. Juarez
 */
@Component
@Qualifier("folioConverter")
public class FolioTransform implements Transformer<Folio, ExencionRequestDTO> {

    /** The folio repositorio. */
    @Autowired
    private FolioRepository folioRepositorio;

    /**
     * Transform.
     *
     * @param form the form
     * @return the folio
     */
    @Override
    public Folio transform(ExencionRequestDTO form) {
        Optional<Folio> optFolio = folioRepositorio.findByFolio(form.getSiguienteFolio() - 1);
        if (optFolio.isPresent()) {
            Folio folio = optFolio.get();
            folio.setActivo(Constantes.ACTIVO);
            folio.setFolio(form.getSiguienteFolio());
            return folio;
        }
        Folio folio = new Folio();
        folio.setActivo(Constantes.ACTIVO);
        folio.setFolio(form.getSiguienteFolio());
        return folio;
    }

	@Override
	public Folio transform(ExencionRequestDTO form, String ruta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Folio transform(ExencionRequestDTO form, boolean actualizacion) {
		// TODO Auto-generated method stub
		return null;
	}
}
