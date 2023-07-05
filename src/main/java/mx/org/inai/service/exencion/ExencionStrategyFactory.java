package mx.org.inai.service.exencion;

import mx.org.inai.util.exencion.TipoExencion;

/**
 * A factory for creating ExencionStrategy objects.
 *
 * @author A. Juarez
 * @param <T> the generic type
 */
public interface ExencionStrategyFactory<T> {
    
    /**
     * Gets the strategy.
     *
     * @param tipoApertura the tipo apertura
     * @return the strategy
     */
    ExencionAbstract<T> getStrategy(final TipoExencion tipoApertura);
            
}
