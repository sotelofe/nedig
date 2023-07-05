package mx.org.inai.service.exencion.impl;

import java.util.Optional;
import mx.org.inai.dto.Constantes;
import mx.org.inai.excepcion.exencion.FlujoException;
import mx.org.inai.model.Catalogo;
import mx.org.inai.repository.CatalogoRepository;
import mx.org.inai.repository.FolioRepository;
import mx.org.inai.service.exencion.FolioGeneratorService;
import mx.org.inai.util.exencion.DateUtil;
import mx.org.inai.util.exencion.TipoFlujo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The Class FolioGeneratorServiceImpl.
 *
 * @author A. Juarez
 */
@Service
public class FolioGeneratorServiceImpl implements FolioGeneratorService {

    /** The catalogo repository. */
    @Autowired
    private CatalogoRepository catalogoRepository;

    /** The folio repositorio. */
    @Autowired
    private FolioRepository folioRepositorio;

    /** The Constant MAX_FILL. */
    private static final int MAX_FILL = 3;

    /** The Constant KEY_FOLIO1. */
    private static final String KEY_FOLIO1 = "folio1";
    
    /** The Constant KEY_FOLIO3. */
    private static final String KEY_FOLIO3 = "folio3";
    
    /** The Constant KEY_FOLIO2. */
    private static final String KEY_FOLIO2 = "folio2";

    /**
     * Genera folio.
     *
     * @param numFolio the num folio
     * @param tipoFlujo the tipo flujo
     * @return the string
     */
    @Override
    public String generaFolio(Integer numFolio, TipoFlujo tipoFlujo) {
        if (numFolio == null) {
            throw new FlujoException("El folio es nulo");
        }
        String numeroFolio = String.valueOf(numFolio);
        String cveFolio = obtenerCatalogo(obtenerClave(tipoFlujo));
        return String.format("%s%s%s%s%d", cveFolio, Constantes.SEPARADORD,
                rellenar(numeroFolio, MAX_FILL), Constantes.SEPARADORD,
                DateUtil.getYear());
    }

    /**
     * Obtener clave.
     *
     * @param tipo the tipo
     * @return the string
     */
    private String obtenerClave(TipoFlujo tipo) {
        switch (tipo) {
            case CONSULTAS:
                return KEY_FOLIO1;
            case EXENCION:
                return KEY_FOLIO3;
            case EIPDP:
                return KEY_FOLIO2;
            default:
                throw new FlujoException("El tipo no es valido");
        }
    }

    /**
     * Obtener etapa.
     *
     * @param key the key
     * @return the string
     */
    @Override
    public String obtenerEtapa(String key) {
        return obtenerCatalogo(key);
    }

    /**
     * Obtener folio.
     *
     * @return the integer
     */
    @Override
    public Integer obtenerFolio() {
        Optional<Integer> optFolio = folioRepositorio.obtenerSiguienteFolio();
        if(optFolio.isPresent()) {
            return optFolio.get();
        }
        return 1;
    }

    /**
     * Rellenar.
     *
     * @param cadena the cadena
     * @param longitud the longitud
     * @return the string
     */
    private String rellenar(String cadena, int longitud) {
    	String formato = "%1$" + longitud + "s";
        return String.format(formato, cadena).replace(' ', '0');
    }

    /**
     * Obtener catalogo.
     *
     * @param clave the clave
     * @return the string
     */
    private String obtenerCatalogo(String clave) {
        Optional<Catalogo> catalogo = catalogoRepository.findByClave(clave);
        if (!catalogo.isPresent()) {
            throw new FlujoException(String.format("La clave %s no existe", clave));
        }
        return catalogo.get().getValor();
    }

}
