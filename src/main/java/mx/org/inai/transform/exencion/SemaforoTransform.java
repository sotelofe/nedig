package mx.org.inai.transform.exencion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import mx.org.inai.dto.Constantes;
import mx.org.inai.dto.exencion.ExencionRequestDTO;
import mx.org.inai.model.Dia;
import mx.org.inai.model.SemaforoRia;
import mx.org.inai.repository.DiaRepository;

/**
 * The Class FlujoTransform.
 *
 * @author A. Juarez
 */
@Component
@Qualifier("semaforoConverter")
public class SemaforoTransform implements Transformer<SemaforoRia, ExencionRequestDTO> {

	@Autowired
	private DiaRepository diaRepository;

    /**
     * Transform.
     *
     * @param form the form
     * @return the flujo
     */
    @Override
    public SemaforoRia transform(ExencionRequestDTO form) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate fechaInicio = obtenerFecha();
		LocalDate fechaFinal = obtenerFecha();
		Dia dia = null;
		int i = 0;
		String sfechaFinal ="";
		
			
			while(i<5) {
				fechaFinal = fechaFinal.plusDays(1);
				sfechaFinal = fechaFinal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));				
				dia = diaRepository.findByFechaActivo(sfechaFinal.toString(), Constantes.ACTIVO);
				if(dia == null ) {
					i++;
				}else {
					dia =null;
				}
			}
			
			 SemaforoRia sr = new SemaforoRia();
	       	 sr.setFolio(form.getFolio());
	       	 sr.setFechaInicioPeriodo(formatter.format(fechaInicio));
	       	 sr.setFechaFinPeriodo(formatter.format(fechaFinal));
	       	 sr.setDiasPeriodo(5);
	       	 sr.setIdColor(32);
	       	 sr.setActivo(Constantes.ACTIVO);

	       	 return sr;
    }
		
		private LocalDate obtenerFecha() {
			LocalDate date = LocalDate.now();
			return date;
		}

		@Override
		public SemaforoRia transform(ExencionRequestDTO form, String ruta) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SemaforoRia transform(ExencionRequestDTO form, boolean actualizacion) {
			// TODO Auto-generated method stub
			return null;
		}
}
