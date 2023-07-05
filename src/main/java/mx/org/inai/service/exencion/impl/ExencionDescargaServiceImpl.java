package mx.org.inai.service.exencion.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import mx.org.inai.model.Cuestionario;
import mx.org.inai.repository.CuestionarioRepository;
import mx.org.inai.service.exencion.ExencionDescargaService;
import mx.org.inai.util.exencion.RutaUtil;
import mx.org.inai.util.exencion.TipoFlujo;

/**
 * The Class ExcencionDescargaServiceImpl.
 *
 * @author A. Juarez
 */
@Service
public class ExencionDescargaServiceImpl implements ExencionDescargaService {

    /** The Constant LOGGER. */
   // private static final Logger LOGGER = LoggerFactory.getLogger(ExencionDescargaServiceImpl.class);

    /** The cuestionaro repository. */
    @Autowired
    private CuestionarioRepository cuestionaroRepository;

    /** The util. */
    @Autowired
    private RutaUtil util;

    /**
     * Valida peticion.
     *
     * @param id the id
     * @param folio the folio
     */
    private void validaPeticion(Integer id, String folio) {
        if (StringUtils.hasText(folio) && id != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Es necesario especificar solo un argumento");
        }
        if (!StringUtils.hasText(folio) && id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Es necesario especificar un argumento");
        }
    }

    /**
     * Validar descarga.
     *
     * @param cuestionario the cuestionario
     */
    private void validarDescarga(Optional<Cuestionario> cuestionario) {
        if (!cuestionario.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el documento");
        }

        if (!StringUtils.hasText(cuestionario.get().getRutaArchivo())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La ruta del archivo esta vacia");
        }

        File file = new File(cuestionario.get().getRutaArchivo());
        if (!file.exists()) {
        	//LOGGER.info("{}",file.getAbsolutePath());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El archivo no existe en la ruta especificada");
        }
    }

    /**
     * Obtener archivo.
     *
     * @param id the id
     * @param pregunta the pregunta
     * @param folio the folio
     * @return the optional
     */
    private Optional<Cuestionario> obtenerArchivo(Integer id, Integer pregunta, String folio) {
        Optional<Cuestionario> cuestionario;
        if (StringUtils.hasText(folio) && pregunta != null) {
            List<Cuestionario> lista = cuestionaroRepository.findByFolioAndPregunta(folio, pregunta);
            if (lista.isEmpty()) {
                return Optional.empty();
            }
            cuestionario = Optional.of(lista.get(0));
        } else if (!StringUtils.hasText(folio)) {
            cuestionario = cuestionaroRepository.findById(id);
        } else {
            String ruta = util.getRuta(TipoFlujo.EXENCION);
            String archivo = String.format("%s.docx", util.getNombreArchivo(folio));
            Cuestionario c = new Cuestionario();
            c.setRutaArchivo(ruta + archivo);
            cuestionario = Optional.of(c);
        }
        return cuestionario;
    }

    /**
     * Obtener mime.
     *
     * @param file the file
     * @return the string
     */
    private String obtenerMime(File file) {
        String tipoMime = URLConnection.guessContentTypeFromName(file.getName());
        if (tipoMime == null) {
            tipoMime = "application/octet-stream";
        }
        //LOGGER.info("Mime: {}", tipoMime);
        return tipoMime;
    }

    /**
     * Descargar.
     *
     * @param id the id
     * @param folio the folio
     * @param pregunta the pregunta
     * @param response the response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Override
    public void descargar(Integer id, String folio, Integer pregunta, HttpServletResponse response) throws IOException {
        //LOGGER.info("Folio: {} - Id: {} - Pregunta: {}", folio, id, pregunta);
        validaPeticion(id, folio);
        Optional<Cuestionario> cuestionario = obtenerArchivo(id, pregunta, folio);
        validarDescarga(cuestionario);
        File file = new File(cuestionario.get().getRutaArchivo());
        String tipoMime = obtenerMime(file);

        //LOGGER.info("Ruta: {}", file.getParent());
        //LOGGER.info("Archivo: {}", file.getName());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(tipoMime);
        response.setContentLengthLong(file.length());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getName() + "\"");
        try (OutputStream out = response.getOutputStream();
                InputStream input = new FileInputStream(file)) {
            copiar(input, out);
        }
    }

    /**
     * Copiar.
     *
     * @param input the input
     * @param out the out
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void copiar(InputStream input, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
    }
}
