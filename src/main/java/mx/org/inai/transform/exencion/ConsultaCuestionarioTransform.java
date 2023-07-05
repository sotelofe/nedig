package mx.org.inai.transform.exencion;

import mx.org.inai.dto.exencion.ExencionCuestionarioDTO;
import mx.org.inai.model.Cuestionario;
import org.springframework.stereotype.Component;

/**
 * The Class ConsultaCuestionarioTransform.
 *
 * @author A. Juarez
 */
@Component
public class ConsultaCuestionarioTransform implements Transformer<ExencionCuestionarioDTO, Cuestionario> {

    /**
     * Transform.
     *
     * @param cuestionario the cuestionario
     * @return the cuestionario DTO
     */
    @Override
    public ExencionCuestionarioDTO transform(Cuestionario cuestionario) {
        ExencionCuestionarioDTO cuestionarioDto = new ExencionCuestionarioDTO();
        cuestionarioDto.setActivo(cuestionario.getActivo());
        cuestionarioDto.setFolio(cuestionario.getFolio());
        cuestionarioDto.setIdCuestionario(cuestionario.getIdCuestionario());
        cuestionarioDto.setPregunta(cuestionario.getPregunta());
        cuestionarioDto.setRespuesta(cuestionario.getRespuesta());
        cuestionarioDto.setRutaArchivo(cuestionario.getRutaArchivo() == null ? "" 
                : cuestionario.getRutaArchivo());
        cuestionarioDto.setSubpregunta(cuestionario.getSubPregunta());
        return cuestionarioDto;
    }

	@Override
	public ExencionCuestionarioDTO transform(Cuestionario form, String ruta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExencionCuestionarioDTO transform(Cuestionario form, boolean actualizacion) {
		// TODO Auto-generated method stub
		return null;
	}

}
