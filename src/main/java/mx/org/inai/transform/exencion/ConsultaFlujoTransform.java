package mx.org.inai.transform.exencion;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.org.inai.dto.exencion.ExencionFlujoDTO;
import mx.org.inai.model.Cuestionario;
import mx.org.inai.model.Flujo;
import mx.org.inai.model.Periodo;
import mx.org.inai.model.Usuario;
import mx.org.inai.repository.CuestionarioRepository;
import mx.org.inai.repository.PeriodoRepository;
import mx.org.inai.repository.UsuarioRepository;
import mx.org.inai.util.exencion.DateUtil;

/**
 * The Class ConsultaFlujoTransform.
 *
 * @author A. Juarez
 */
@Component
public class ConsultaFlujoTransform implements Transformer<ExencionFlujoDTO, Flujo> {

    /** The Constant LOGGER. */
   // private static final Logger LOGGER = LoggerFactory.getLogger(ConsultaFlujoTransform.class);

    /** The periodo repository. */
    @Autowired
    private PeriodoRepository periodoRepository;

    /** The usuario repository. */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /** The cuestionaro repository. */
    @Autowired
    private CuestionarioRepository cuestionaroRepository;

    /**
     * Transform.
     *
     * @param flujo the flujo
     * @return the flujo DTO
     */
    @Override
    public ExencionFlujoDTO transform(Flujo flujo) {
        ExencionFlujoDTO flujoDto = new ExencionFlujoDTO();
        flujoDto.setIdFlujo(flujo.getIdFlujo());
        flujoDto.setIdUsuario(flujo.getIdUsuario());
        flujoDto.setFlujo(flujo.getFlujo());
        flujoDto.setFecha(DateUtil.getFechaFormato(flujo.getFecha()));
        flujoDto.setIdColor(getIdColor(flujo.getFolio()));
        flujoDto.setTuvoRia(tuvoRia(flujo.getFolio()));
        flujoDto.setTuvoRiaNoPresentada(tuvoRiaNoPresentada(flujo.getFolio()));
        flujoDto.setFolio(flujo.getFolio());
        flujoDto.setEtapa(flujo.getEtapa());
        flujoDto.setActivo(flujo.getActivo());
        flujoDto.setUsuario(getUsuario(flujo.getIdUsuario()));
        return flujoDto;
    }

    /**
     * Tuvo ria.
     *
     * @param folio the folio
     * @return true, if successful
     */
    private boolean tuvoRia(String folio) {
        List<Cuestionario> lista = cuestionaroRepository.findByFolioAndPregunta(folio, 11);
        return !lista.isEmpty();
    }

    /**
     * Gets the usuario.
     *
     * @param idUsuario the id usuario
     * @return the usuario
     */
    private String getUsuario(Integer idUsuario) {
        Optional<Usuario> usuario = usuarioRepository.findUsuario(idUsuario);
        if (!usuario.isPresent()) {
            return "";
        }
        return usuario.get().getUsuario();
    }

    /**
     * Gets the id color.
     *
     * @param folio the folio
     * @return the id color
     */
    private Integer getIdColor(String folio) {
        Optional<Periodo> periodo = periodoRepository.findByFolio(folio);
        if (!periodo.isPresent()) {
        	//LOGGER.info("Folio: {}", folio);
            return 35;
        }
        return periodo.get().getIdColor();
    }
    
    /**
     * Tuvo ria no presentada.
     *
     * @param folio the folio
     * @return true, if successful
     */
    private boolean tuvoRiaNoPresentada(String folio) {
        List<Cuestionario> lista = cuestionaroRepository.findByFolioAndPregunta(folio, 20);
        return !lista.isEmpty();
    }

	@Override
	public ExencionFlujoDTO transform(Flujo form, String ruta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExencionFlujoDTO transform(Flujo form, boolean actualizacion) {
		// TODO Auto-generated method stub
		return null;
	}

    
}
