package mx.org.inai.scheduledtasks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import mx.org.inai.dto.Constantes;
import mx.org.inai.model.Dia;
import mx.org.inai.model.SecuenciaFlujo;
import mx.org.inai.model.SemaforoRia;
import mx.org.inai.repository.DiaRepository;
import mx.org.inai.repository.SecuenciaFlujoRepository;
import mx.org.inai.repository.SemaforoRiaRepository;

@Component
public class SemaforoRiaTask {
		
	@Autowired
	private DiaRepository diaRepository;
	
	@Autowired
	private SecuenciaFlujoRepository secuenciaRepository;
	
	@Autowired
	private SemaforoRiaRepository semaforoRepository;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Scheduled(cron = "0 6 13 ? * * ")	
	public void validaPeridoEtapas() {
				
		List<SemaforoRia> listPeriodo = semaforoRepository.findByActivo(Constantes.ACTIVO);		
		LocalDate fechaActual = obtenerFecha();
				
		for(SemaforoRia pe: listPeriodo) {
												
			LocalDate fechaFin = LocalDate.parse(pe.getFechaFinPeriodo(), formatter);
															
			if(fechaActual.isEqual(fechaFin)){
				pe.setIdColor(33);
				semaforoRepository.save(pe);
			}else {																												
					SecuenciaFlujo ria = secuenciaRepository.findByFolioAndEtapa(pe.getFolio(),Constantes.RIA);																													
					
					if(ria!=null) {
						LocalDate fechaInicioRia = LocalDate.parse(ria.getFecha(), format);						
						Integer diasTranscurridos = getDiasTranscurridosCambioEtapa(fechaInicioRia, fechaActual);								
						Integer idColor = getColorEstatus(diasTranscurridos, pe.getDiasPeriodo());								
						pe.setIdColor(idColor);
						semaforoRepository.save(pe);
						
						if(diasTranscurridos>5) {							
							pe.setActivo(Constantes.INACTIVO);						
							semaforoRepository.save(pe);
						}
					}
			}									
		}
	}
	
	
	private Integer getDiasTranscurridosCambioEtapa(LocalDate fechaInicioAcuerdo, LocalDate fechaActual){
		Integer incr=0;
		
		while(fechaInicioAcuerdo.isBefore(fechaActual) || fechaInicioAcuerdo.isEqual(fechaActual)) {
			Dia dia = null;
			fechaInicioAcuerdo = fechaInicioAcuerdo.plusDays(1);
			String afecha[] = fechaInicioAcuerdo.toString().split("-");
			String sfecha = afecha[2]+"/"+afecha[1]+"/"+afecha[0];
			dia = diaRepository.findAll().stream().filter(d -> d.getFecha().equals(sfecha) && d.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
			if(dia == null) {
				if(fechaInicioAcuerdo.isBefore(fechaActual) || fechaInicioAcuerdo.isEqual(fechaActual)) {
					incr++;
				}
			}	
			System.out.println("dentro");
		}
		System.out.println("incr: "+ incr);
		
		return incr;
	}
	
	private LocalDate obtenerFecha() {		
		return LocalDate.now();
	}
	
	private Integer getColorEstatus(int diasTranscurridos, int diasPermitidos) {
		Integer idColor = 0;
		
		switch (diasPermitidos) {
		case 5: 
			if(diasTranscurridos >=0 && diasTranscurridos <=2) {
				idColor = 32;
			}else if(diasTranscurridos >=3 && diasTranscurridos <=4) {
				idColor = 34;
			}else if(diasTranscurridos == 5) {
				idColor = 33;
			}else if(diasTranscurridos >5) {
				idColor = 35;
			}
			break;
			
		default:
			break;
		}
		
		return idColor;
	}
}
