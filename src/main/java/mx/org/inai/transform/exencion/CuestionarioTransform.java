package mx.org.inai.transform.exencion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import mx.org.inai.dto.Constantes;
import mx.org.inai.dto.exencion.ExencionArchivoDTO;
import mx.org.inai.dto.exencion.ExencionPreguntasDTO;
import mx.org.inai.dto.exencion.ExencionRequestDTO;
import mx.org.inai.excepcion.exencion.FlujoException;
import mx.org.inai.model.Cuestionario;
import mx.org.inai.util.exencion.RutaUtil;
import mx.org.inai.util.exencion.TipoFlujo;

/**
 * The Class CuestionarioTransform.
 *
 * @author A. Juarez
 */
@Component
@Qualifier("cuestionarioConverter")
public class CuestionarioTransform implements Transformer<List<Cuestionario>, ExencionRequestDTO> {

    /** The Constant LOGGER. */
  //  private static final Logger LOGGER = LoggerFactory.getLogger(CuestionarioTransform.class);

    /** The util. */
    @Autowired
    private RutaUtil util;

    /**
     * Transform.
     *
     * @param form the form
     * @return the list
     */
    @Override
    public List<Cuestionario> transform(ExencionRequestDTO form) {
        List<Cuestionario> lista = new ArrayList<>();       
        form.getPreguntas().stream().map(pregunta -> {
            Cuestionario cuestionario = new Cuestionario();
            cuestionario.setFolio(form.getFolio());
            cuestionario.setActivo(Constantes.ACTIVO);
            cuestionario.setSubPregunta(pregunta.getSubpregunta());
            cuestionario.setPregunta(pregunta.getPregunta());
            List<String> archivo = guardarArchivo(pregunta, form.getTipoFlujo(), form.getFolio());
            if (!archivo.isEmpty()) {
                cuestionario.setRutaArchivo(archivo.get(1));
                cuestionario.setRespuesta(archivo.get(0));
            } else {
                cuestionario.setRespuesta(pregunta.getRespuesta());
                cuestionario.setRutaArchivo(null);
            }
            return cuestionario;
        }).forEach(lista::add);
        return lista;
    }

    /**
     * Guardar archivo.
     *
     * @param pregunta the pregunta
     * @param tipoFlujo the tipo flujo
     * @param folio the folio
     * @return the list
     */
    private List<String> guardarArchivo(ExencionPreguntasDTO pregunta, TipoFlujo tipoFlujo, String folio) {
        try {
            List<String> lista = new ArrayList<>();
            ExencionArchivoDTO archivo = pregunta.getArchivo();
            if (archivo == null) {
                return lista;
            }
           // LOGGER.info("Archivo: {}", archivo.getFilename());
           // LOGGER.info("Pregunta: {}", pregunta);
            String rutaArchivo = String.format("%s%s%s%d%s%d%s", 
                    util.getRuta(tipoFlujo),
                    util.getNombreArchivo(folio), Constantes.SEPARADORD,
                    pregunta.getPregunta(), Constantes.SEPARADORD,
                    pregunta.getSubpregunta(), Constantes.SEPARADORD);
            String nombreArchivo = rutaArchivo + archivo.getFilename();
            File file = new File(rutaArchivo);
            if (!file.mkdirs()) {                
               // LOGGER.debug("No se pudieron crear los directorios");
                throw new FlujoException("No hay permisos para crear las carpetas");
            }
            
            try (OutputStream out = new FileOutputStream(nombreArchivo)) {
                byte[] decodedBytes = Base64.getDecoder().decode(archivo.getValue().getBytes(StandardCharsets.UTF_8));
                out.write(decodedBytes);
                out.flush();
            }
            lista.add(archivo.getFilename());
            lista.add(nombreArchivo);
            return lista;
        } catch (IOException ex) {
            throw new FlujoException(ex);
        }
    }

	@Override
	public List<Cuestionario> transform(ExencionRequestDTO form, String ruta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Cuestionario> transform(ExencionRequestDTO form, boolean actualizacion) {
		// TODO Auto-generated method stub
		return null;
	}

}
