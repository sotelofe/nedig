package mx.org.inai.service.exencion.impl;

import java.util.EnumMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import mx.org.inai.excepcion.exencion.FlujoException;
import mx.org.inai.service.exencion.ExencionAbstract;
import mx.org.inai.service.exencion.ExencionStrategyFactory;
import mx.org.inai.service.exencion.Strategy;
import mx.org.inai.util.exencion.TipoExencion;

/**
 * The Class ExencionStrategyFactoryImpl.
 *
 * @author A. Juarez
 */
@Component
public class ExencionStrategyFactoryImpl implements ExencionStrategyFactory<String> {

    /** The Constant LOGGER. */
    //private static final Logger LOGGER = LoggerFactory.getLogger(ExencionStrategyFactoryImpl.class);

    /** The application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** The strategy cache. */
    private final Map<TipoExencion, Object> strategyCache = new EnumMap<>(TipoExencion.class);

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        //LOGGER.debug("Cargando las estrategias");
        Map<String, Object> annotatedBeanClasses = this.applicationContext.getBeansWithAnnotation(Strategy.class);

        annotatedBeanClasses.entrySet().forEach(entry -> {
            String key = entry.getKey();
            Object bean = entry.getValue();
            Strategy strategyAnnotation = this.applicationContext.findAnnotationOnBean(key, Strategy.class);
            //LOGGER.debug("<key={},bean={},strategyAnnotation={}>", key, bean, strategyAnnotation);
            if (strategyAnnotation == null) {
                throw new FlujoException("El objeto es nulo");
            }
            this.strategyCache.put(strategyAnnotation.tipoExencion(), bean);
        });
    }

    /**
     * Gets the strategy.
     *
     * @param tipoApertura the tipo apertura
     * @return the strategy
     */
    @SuppressWarnings("unchecked")
	@Override
    public ExencionAbstract<String> getStrategy(final TipoExencion tipoApertura) {
        return (ExencionAbstract<String>) this.strategyCache.get(tipoApertura);
    }
}
