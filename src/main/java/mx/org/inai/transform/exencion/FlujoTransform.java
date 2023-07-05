package mx.org.inai.transform.exencion;

import java.time.LocalDate;
import mx.org.inai.dto.exencion.ExencionRequestDTO;
import mx.org.inai.model.Flujo;
import mx.org.inai.dto.Constantes;
import mx.org.inai.repository.FlujoRepository;
import mx.org.inai.util.exencion.DateUtil;
import mx.org.inai.util.exencion.TipoEnvio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * The Class FlujoTransform.
 *
 * @author A. Juarez
 */
@Component
@Qualifier("flujoConverter")
public class FlujoTransform implements Transformer<Flujo, ExencionRequestDTO> {

    /** The flujo repository. */
    @Autowired
    private FlujoRepository flujoRepository;

    /**
     * Transform.
     *
     * @param form the form
     * @return the flujo
     */
    @Override
    public Flujo transform(ExencionRequestDTO form) {
        if (form.getTipoEnvio() == TipoEnvio.REGISTRAR) {
            Flujo flujo = new Flujo();
            flujo.setActivo(Constantes.ACTIVO);
            flujo.setEtapa(form.getEtapa());
            flujo.setFolio(form.getFolio());
            flujo.setFlujo(form.getTipoFlujo().getValor());
            flujo.setIdUsuario(form.getIdUsuario());
            flujo.setFecha(DateUtil.dateAsString(LocalDate.now()));
            return flujo;
        } else {
            Flujo flujo = flujoRepository.findByFolio(form.getFolio());
            flujo.setEtapa(form.getEtapa());
            return flujo;
        }

    }

	@Override
	public Flujo transform(ExencionRequestDTO form, String ruta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Flujo transform(ExencionRequestDTO form, boolean actualizacion) {
		// TODO Auto-generated method stub
		return null;
	}
}
