package mx.org.inai.util.exencion;

import mx.org.inai.excepcion.exencion.FlujoException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * The Class RutaUtil.
 *
 * @author A. Juarez
 */
@Component
public class RutaUtil {

    /** The env. */
    @Autowired
    private Environment env;

    /**
     * Gets the ruta.
     *
     * @param tipoFlujo the tipo flujo
     * @return the ruta
     */
    public String getRuta(TipoFlujo tipoFlujo) {
        String ruta;
        switch (tipoFlujo) {
            case CONSULTAS:
                ruta = env.getProperty("eipdp.dirflujo1");
                break;
            case EIPDP:
                ruta = env.getProperty("eipdp.dirflujo2");
                break;
            case EXENCION:
                ruta = env.getProperty("eipdp.dirflujo3");
                break;
            default:
                throw new FlujoException("El tipo de flujo no es valido");
        }
        return ruta;
    }

    /**
     * Gets the nombre archivo.
     *
     * @param folio the folio
     * @return the nombre archivo
     */
    public String getNombreArchivo(String folio) {
        return folio.replace("/", "");
    }
}
