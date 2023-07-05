package mx.org.inai.controller;

import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import mx.org.inai.dto.exencion.ExencionResponseDTO;
import mx.org.inai.dto.exencion.ExencionRequestDTO;
import mx.org.inai.dto.exencion.ExencionCuestionarioDTO;
import mx.org.inai.dto.exencion.ExencionFlujoDTO;
import mx.org.inai.service.exencion.ExencionConsultaService;
import mx.org.inai.service.exencion.ExencionDescargaService;
import mx.org.inai.service.exencion.ExencionStrategyFactory;
import mx.org.inai.util.exencion.TipoExencion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class ExcencionController.
 *
 * @author A. Juarez
 */
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping("/api/excencion")
public class ExencionController {


    /** The descarga service. */
    @Autowired
    private ExencionDescargaService descargaService;

    /** The consulta service. */
    @Autowired
    private ExencionConsultaService consultaService;
    
    /** The strategy. */
    @Autowired
    private ExencionStrategyFactory<String> strategy;

    /**
     * Registrar respuesta.
     *
     * @param peticion the peticion
     * @param folio the folio
     * @return the excencion response DTO
     */
    @ApiOperation(
            value = "Servicio que registra la respuesta de excencion",
            response = ExencionResponseDTO.class)
    @PostMapping("respuesta/registrar")
    @ResponseBody
    public ExencionResponseDTO<String> registrarRespuesta(@RequestBody ExencionRequestDTO peticion,
            @RequestParam(name = "folio", required = true) String folio) {
        return strategy
                .getStrategy(TipoExencion.REGISTRAR_RESPUESTA)
                .registrar(peticion, folio);
    }

    /**
     * Registrar flujo.
     *
     * @param peticion the peticion
     * @return the excencion response DTO
     */
    @ApiOperation(
            value = "Servicio que registra el flujo de excencion",
            response = ExencionResponseDTO.class)
    @PostMapping("cuestionario/registrar")
    @ResponseBody
    public ExencionResponseDTO<String> registrarFlujo(@RequestBody ExencionRequestDTO peticion) {
    	return strategy
                .getStrategy(TipoExencion.REGISTRAR_CUESTIONARIO)
                .registrar(peticion, "");
    }

    /**
     * Aceptar flujo.
     *
     * @param peticion the peticion
     * @param folio the folio
     * @return the excencion response DTO
     */
    @ApiOperation(
            value = "Servicio que acepta el flujo de excencion",
            response = ExencionResponseDTO.class)
    @PostMapping("cuestionario/aceptar")
    @ResponseBody
    public ExencionResponseDTO<String> aceptarFlujo(@RequestBody ExencionRequestDTO peticion,
            @RequestParam(name = "folio", required = true) String folio) {
    	return strategy
                .getStrategy(TipoExencion.ACEPTAR_RESPUESTA)
                .registrar(peticion, folio);
    }

    /**
     * Registra ria.
     *
     * @param peticion the peticion
     * @param folio the folio
     * @return the excencion response DTO
     */
    @ApiOperation(
            value = "Servicio que registra RIA",
            response = ExencionResponseDTO.class)
    @PostMapping("ria/registrar")
    @ResponseBody
    public ExencionResponseDTO<String> registraRia(@RequestBody ExencionRequestDTO peticion,
            @RequestParam(name = "folio", required = true) String folio) {
    	return strategy
                .getStrategy(TipoExencion.REGISTRAR_RIA)
                .registrar(peticion, folio);
    }

    /**
     * Aceptar ria.
     *
     * @param peticion the peticion
     * @param folio the folio
     * @return the excencion response DTO
     */
    @ApiOperation(
            value = "Servicio que acepta RIA",
            response = ExencionResponseDTO.class)
    @PostMapping("ria/aceptar")
    @ResponseBody
    public ExencionResponseDTO<String> aceptarRia(@RequestBody ExencionRequestDTO peticion,
            @RequestParam(name = "folio", required = true) String folio) {
    	return strategy
                .getStrategy(TipoExencion.ACEPTAR_RIA)
                .registrar(peticion, folio);
    }

    /**
     * Enviar ria.
     *
     * @param peticion the peticion
     * @param folio the folio
     * @return the excencion response DTO
     */
    @ApiOperation(
            value = "Servicio que envia RIA",
            response = ExencionResponseDTO.class)
    @PostMapping("ria/enviar")
    @ResponseBody
    public ExencionResponseDTO<String> enviarRia(@RequestBody ExencionRequestDTO peticion,
            @RequestParam(name = "folio", required = true) String folio) {
    	return strategy
                .getStrategy(TipoExencion.ENVIAR_RIA)
                .registrar(peticion, folio);
    }

    /**
     * Descargar cuestionario.
     *
     * @param folio the folio
     * @param id the id
     * @param pregunta the pregunta
     * @param response the response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @ApiOperation(
            value = "Servicio que descarga los documentos",
            response = Void.class)
    @GetMapping("cuestionario/descargar")
    @ResponseBody
    public void descargarCuestionario(
            @RequestParam(name = "folio", required = false) String folio,
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "pregunta", required = false) Integer pregunta,
            HttpServletResponse response) throws IOException {
        descargaService.descargar(id, folio, pregunta, response);
    }

    /**
     * Consultar cuestionario.
     *
     * @param folio the folio
     * @return the excencion response DTO
     */
    @ApiOperation(
            value = "Servicio que consulta el cuestionario",
            response = ExencionResponseDTO.class,
            nickname = "consultar")

    @GetMapping("cuestionario/consultar")
    @ResponseBody
    public ExencionResponseDTO<List<ExencionCuestionarioDTO>> consultarCuestionario(String folio) {
        return consultaService.consultarCuestionario(folio);
    }

    /**
     * Consultar cuestionario.
     *
     * @param folio the folio
     * @param pregunta the pregunta
     * @return the excencion response DTO
     */
    @ApiOperation(
            value = "Servicio que consulta el detalle cuestionario",
            response = ExencionResponseDTO.class,
            nickname = "detalle")

    @GetMapping("cuestionario/detalle")
    @ResponseBody
    public ExencionResponseDTO<List<ExencionCuestionarioDTO>> consultarCuestionario(String folio, Integer pregunta) {
        return consultaService.consultarCuestionario(folio, pregunta);
    }

    /**
     * Consultar flujo.
     *
     * @param idUsuario the id usuario
     * @return the excencion response DTO
     */
    @ApiOperation(
            value = "Servicio que consulta por flujo",
            response = ExencionResponseDTO.class)
    @GetMapping("flujo/consultar")
    @ResponseBody
    public ExencionResponseDTO<List<ExencionFlujoDTO>> consultarFlujo(Integer idUsuario) {
        return consultaService.consultarFlujo(idUsuario);
    }
    
    @PostMapping("registrar/opinion")
    @ResponseBody
    public ExencionResponseDTO<String> registrarOpinion(@RequestBody ExencionRequestDTO peticion, @RequestParam(name = "folio", required = true) String folio) {
    	return strategy
                .getStrategy(TipoExencion.REGISTRAR_OPINION)
                .registrar(peticion, folio);
    }
}