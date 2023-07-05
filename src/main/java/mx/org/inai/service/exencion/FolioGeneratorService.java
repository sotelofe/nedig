package mx.org.inai.service.exencion;

import mx.org.inai.util.exencion.TipoFlujo;

/**
 * The Interface IFolioService.
 *
 * @author A. Juarez
 */
public interface FolioGeneratorService {
  
  /**
   * Genera folio.
   *
   * @param numFolio the num folio
   * @param tipoFlujo the tipo flujo
   * @return the string
   */
  String generaFolio(Integer numFolio, TipoFlujo tipoFlujo);    
  
  /**
   * Obtener etapa.
   *
   * @param key the key
   * @return the string
   */
  String obtenerEtapa(String key);
  
  /**
   * Obtener folio.
   *
   * @return the integer
   */
  Integer obtenerFolio();
}
