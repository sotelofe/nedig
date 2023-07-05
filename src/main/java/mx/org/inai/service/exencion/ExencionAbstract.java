package mx.org.inai.service.exencion;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import mx.org.inai.dto.exencion.ExencionRequestDTO;
import mx.org.inai.dto.exencion.ExencionResponseDTO;
import mx.org.inai.excepcion.exencion.FlujoException;
import mx.org.inai.repository.CuestionarioRepository;
import mx.org.inai.repository.FlujoRepository;
import mx.org.inai.repository.FolioRepository;
import mx.org.inai.repository.PeriodoRepository;
import mx.org.inai.repository.SecuenciaFlujoRepository;
import mx.org.inai.repository.SemaforoRiaRepository;
import mx.org.inai.transform.exencion.CuestionarioTransform;
import mx.org.inai.transform.exencion.FlujoTransform;
import mx.org.inai.transform.exencion.FolioTransform;
import mx.org.inai.transform.exencion.PeriodoTransform;
import mx.org.inai.transform.exencion.SecuenciaTransform;
import mx.org.inai.transform.exencion.SemaforoTransform;

/**
 * The Class ExencionAbstract.
 *
 * @author A. Juarez
 * @param <T> the generic type
 */
public abstract class ExencionAbstract<T> {

    /** The Constant LOGGER. */
    //protected static final Logger LOGGER = LoggerFactory.getLogger(ExencionAbstract.class);

    /** The Constant KEY_ETAPAH1. */
    public static final String KEY_ETAPAH1 = "admitido";
    
    /** The Constant KEY_ETAPAH2. */
    public static final String KEY_ETAPAH2 = "emires";
    
    /** The Constant KEY_ETAPAH3. */
    public static final String KEY_ETAPAH3 = "conexen";
    
    /** The Constant KEY_ETAPAH4. */
    public static final String KEY_ETAPAH4 = "ria-flujo3";
    
    /** The Constant KEY_ETAPAH5. */
    public static final String KEY_ETAPAH5 = "revria-flujo3";
    
    public static final String KEY_ETAPAH6 = "optecre";

    /** The periodo repository. */
    @Autowired
    private PeriodoRepository periodoRepository;

    /** The folio repositorio. */
    @Autowired
    private FolioRepository folioRepositorio;

    /** The cuestionaro repository. */
    @Autowired
    private CuestionarioRepository cuestionaroRepository;

    /** The flujo repository. */
    @Autowired
    private FlujoRepository flujoRepository;

    /** The periodo converter. */
    @Autowired
    private PeriodoTransform periodoConverter;

    /** The flujo converter. */
    @Autowired
    private FlujoTransform flujoConverter;

    /** The folio converter. */
    @Autowired
    private FolioTransform folioConverter;

    /** The cuestionario converter. */
    @Autowired
    private CuestionarioTransform cuestionarioConverter;

    /** The documento service. */
    @Autowired
    private ExencionGeneraCuestionario documentoService;

    /** The correo service. */
    @Autowired
    private ExencionCorreoService correoService;
    
    @Autowired
    private SecuenciaFlujoRepository secuenciaRepository;
    
    @Autowired
    private SemaforoRiaRepository semaforoRepository;
    
    @Autowired
    private SecuenciaTransform secuenciaTransform;
    
    @Autowired
    private SemaforoTransform semaforoTransform;
    
    
    
    /**
     * Registrar.
     *
     * @param peticion the peticion
     * @param folio the folio
     * @return the excencion response DTO
     */
    public abstract ExencionResponseDTO<T> registrar(ExencionRequestDTO peticion, String folio);
    

    /**
     * Validar peticion.
     *
     * @param peticion the peticion
     */
    public void validarPeticion(ExencionRequestDTO peticion) {
        
        if (!StringUtils.hasText(peticion.getFolio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El folio es requerido");
        }

        if (!StringUtils.hasText(peticion.getEtapa())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La etapa es requerida");
        }

        if (peticion.getPreguntas().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las preguntas son requeridas");
        }
    }

    /**
     * Enviar correo.
     *
     * @param form the form
     */
    protected void enviarCorreo(ExencionRequestDTO form) {
        correoService.enviar(form);
    }

    /**
     * Crear documento.
     *
     * @param form the form
     */
    protected void crearDocumento(ExencionRequestDTO form) {
        try {
            documentoService.generar(form);
        } catch (IOException ex) {
        	//LOGGER.error(ex.getMessage(), ex);
            throw new FlujoException("No se pudo crear el documento");
        }
    }

    /**
     * Guardar periodo.
     *
     * @param form the form
     */
    protected void guardarPeriodo(ExencionRequestDTO form) {
        periodoRepository.save(periodoConverter.transform(form));
    }

    /**
     * Guardar folio.
     *
     * @param form the form
     */
    protected void guardarFolio(ExencionRequestDTO form) {
        folioRepositorio.save(folioConverter.transform(form));
    }

    /**
     * Guardar flujo.
     *
     * @param form the form
     */
    protected void guardarFlujo(ExencionRequestDTO form) {
        flujoRepository.save(flujoConverter.transform(form));
    }

    /**
     * Guardar cuestionario.
     *
     * @param form the form
     */
    protected void guardarCuestionario(ExencionRequestDTO form) {
        cuestionaroRepository.saveAll(cuestionarioConverter.transform(form));
    }
    
    
    protected void guardarSecuencia(ExencionRequestDTO form) {
        secuenciaRepository.save(secuenciaTransform.transform(form));
    }
    
    protected void guardarSecuenciaRuta(ExencionRequestDTO form, String ruta) {
        secuenciaRepository.save(secuenciaTransform.transform(form, ruta));
    }
    
    protected void guardarSemaforo(ExencionRequestDTO form) {
        semaforoRepository.save(semaforoTransform.transform(form));
    }
    
    protected void guardarSecuenciaActualizacion(ExencionRequestDTO form, boolean actualizacion) {
        secuenciaRepository.save(secuenciaTransform.transform(form, actualizacion));
    }
    
    
    /**
     * Log error.
     *
     * @param ex the ex
     */
    public void logError(Throwable ex) {
    	//LOGGER.error(ex.getMessage(), ex);
    }
    
    /**
     * Log error.
     *
     * @param mensaje the mensaje
     */
    public void logError(String mensaje) {
    	//LOGGER.error("Error: {}", mensaje);
    }
}
