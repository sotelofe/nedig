package mx.org.inai.scheduledtasks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import mx.org.inai.dto.Constantes;
import mx.org.inai.model.Dia;
import mx.org.inai.model.Flujo;
import mx.org.inai.model.Periodo;
import mx.org.inai.model.SecuenciaFlujo;
import mx.org.inai.repository.DiaRepository;
import mx.org.inai.repository.FlujoRepository;
import mx.org.inai.repository.PeriodoRepository;
import mx.org.inai.repository.SecuenciaFlujoRepository;

@Component
public class PeriodoTask {

	private static final Logger logger = LoggerFactory.getLogger(PeriodoTask.class);
	
	@Autowired
	PeriodoRepository periodoRepository;

	@Autowired
	private FlujoRepository flujoRepositorio;

	@Autowired
	DiaRepository diaRepository;

	@Autowired
	SecuenciaFlujoRepository secuenciaRepository;

	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	//@Scheduled(fixedRate = 30000)
	//@Scheduled(cron = "0 19 22 ? * * ")
	//@Scheduled(cron = "0 0 0 ? * * ")
	@Scheduled(cron = "0 6 13 ? * * ")
	public void validaPeridoEtapas() {
		System.out.println("Inicio periodo");
		logger.info("Iniciando CRON de actualizacion de periodos");
		
		List<Periodo> listPeriodo = periodoRepository.findAll().stream()
				.filter(p -> p.getActivo().equals(Constantes.ACTIVO)).collect(Collectors.toList());
		LocalDate fechaActual = obtenerFecha();
		int inc = 0;
		for (Periodo pe : listPeriodo) {

			LocalDate fechaFin = LocalDate.parse(pe.getFechaFinPeriodo(), formatter);
			System.out.println("Fecha Inicio: " + pe.getFechaInicioPeriodo());
			System.out.println("Fecha Fin: " + pe.getFechaFinPeriodo());


				Flujo flujo = flujoRepositorio.findAll().stream()
						.filter(f -> f.getFolio().equals(pe.getFolio()) && f.getActivo().equals(Constantes.ACTIVO))
						.findAny().orElse(null);
				
				try {
						if(flujo.getFlujo()==1) {
							flujo1(flujo, pe);
						}else if(flujo.getFlujo()==2) {
							flujo2(flujo, pe);
						}else if(flujo.getFlujo()==3) {
							flujo3(flujo, pe);
						}
		
						if (pe.getSuspendido() == null || pe.getSuspendido() == 0) {
							
							Dia dia = diaRepository.findAll().stream().filter(d -> d.getFecha().equals(getFechaNormal()) && d.getActivo().equals(Constantes.ACTIVO)).findAny().orElse(null);
							
							if(dia==null) {
								Integer dias = (pe.getDiasTranscurridos() == null ? 0 : pe.getDiasTranscurridos()) + 1;
								Integer idColor = getColorEstatus(dias, pe.getDiasPeriodo());
								pe.setDiasTranscurridos(dias);
								pe.setIdColor(idColor);
								if(flujo.getEtapa().equals(Constantes.RNP)) {
									pe.setIdColor(35);
									pe.setActivo(Constantes.INACTIVO);
								}
								periodoRepository.save(pe);
							}
						}
		
						
				}catch (Exception e) {
					e.printStackTrace();
					logger.info("folio: "+pe.getFolio());
					logger.info("flujo: "+flujo.getFlujo());
					logger.info(e.getMessage());
				}
			
		}
		System.out.println("Fin periodo");
	}
	
	private String getFechaNormal() {
		LocalDate fechaInhabil = LocalDate.parse(obtenerFecha().toString(), format);
		String afecha[] = fechaInhabil.toString().split("-");
		String sfecha = afecha[2] +"/"+afecha[1]+"/"+afecha[0];
		return sfecha;
	}

	private void flujo1(Flujo flujo, Periodo pe) {
				
		if ((pe.getDiasPeriodo() == 5) && (pe.getDiasTranscurridos() !=null && pe.getDiasTranscurridos() == 5)) {
			
			SecuenciaFlujo secu = secuenciaRepository.findByFolioAndEtapa(flujo.getFolio(), Constantes.ACUERDO);
			
			if(secu==null) {
				flujo.setEtapa(Constantes.ACUERDO);
				flujoRepositorio.save(flujo);
				pe.setDiasPeriodo(15);
				periodoRepository.save(pe);
			
				altaSecuenciaFlujo(pe.getFolio(), Constantes.ACUERDO, "");
			}
			
		} else if (flujo.getEtapa().equals(Constantes.ACUERDO)) {

			SecuenciaFlujo secuencia = secuenciaRepository.findByFolioAndEtapa(pe.getFolio(), Constantes.ACUERDO);
			LocalDate fechaInicioAcuerdo = LocalDate.parse(secuencia.getFecha(), format);

			Integer diasTranscurridos = getDiasTranscurridosCambioEtapa(fechaInicioAcuerdo, obtenerFecha());

			if (diasTranscurridos == 3 || diasTranscurridos == 8) {
				flujo.setEtapa(Constantes.GOT);
				flujo.setPermiteAcuerdo(1);
				flujoRepositorio.save(flujo);

				altaSecuenciaFlujo(pe.getFolio(), Constantes.GOT, "");

			}
		} else if ((flujo.getEtapa().equals(Constantes.GOT))
				|| (flujo.getFlujo() == 1 && flujo.getEtapa().equals(Constantes.OTR))) {

			SecuenciaFlujo secuencia = secuenciaRepository.findByFolioAndEtapa(pe.getFolio(), Constantes.GOT);
			LocalDate fechaInicioGenerando = LocalDate.parse(secuencia.getFecha(), format);
			
			Integer diasTranscurridos = getDiasTranscurridosCambioEtapa(fechaInicioGenerando, obtenerFecha());

			if (diasTranscurridos == 5 ) {
				flujo.setPermiteAcuerdo(-1);
				flujoRepositorio.save(flujo);
			}
		} else if (flujo.getEtapa().equals(Constantes.RIA)) {

			SecuenciaFlujo secuencia = secuenciaRepository.findByFolioAndEtapa(pe.getFolio(), Constantes.RIA);
			LocalDate fechaInicioRIA = LocalDate.parse(secuencia.getFecha(), format);

			Integer diasTranscurridos = getDiasTranscurridosCambioEtapa(fechaInicioRIA, obtenerFecha());

			if (diasTranscurridos > 5) {
				flujo.setEtapa(Constantes.RNP);
				flujoRepositorio.save(flujo);
				pe.setSuspendido(0);
				pe.setDiasPeriodo(15);
				pe.setIdColor(35);
				periodoRepository.save(pe);
				altaSecuenciaFlujo(pe.getFolio(), Constantes.RNP, "");
			}

		}
	}
			
	private void flujo2(Flujo flujo, Periodo pe) {
		
		if (pe.getDiasPeriodo() == 5 && (pe.getDiasTranscurridos() !=null && pe.getDiasTranscurridos() == 5)) {
			
			SecuenciaFlujo secu = secuenciaRepository.findByFolioAndEtapa(flujo.getFolio(), Constantes.ACUERDO);
			
			if(secu==null) {
				flujo.setEtapa(Constantes.ACUERDO);
				flujoRepositorio.save(flujo);
				pe.setDiasPeriodo(30);
				periodoRepository.save(pe);
							
				altaSecuenciaFlujo(pe.getFolio(), Constantes.ACUERDO, "");
			}
			
		} else if (flujo.getEtapa().equals(Constantes.ACUERDO)) {

			SecuenciaFlujo secuencia = secuenciaRepository.findByFolioAndEtapa(pe.getFolio(), Constantes.ACUERDO);
			LocalDate fechaInicioAcuerdo = LocalDate.parse(secuencia.getFecha(), format);

			Integer diasTranscurridos = getDiasTranscurridosCambioEtapa(fechaInicioAcuerdo, obtenerFecha());

			if (diasTranscurridos == 3 || diasTranscurridos == 8) {
				flujo.setEtapa(Constantes.GDM);
				flujo.setPermiteAcuerdo(1);
				flujoRepositorio.save(flujo);

				altaSecuenciaFlujo(pe.getFolio(), Constantes.GDM, "");

			}
		}else if ( flujo.getEtapa().equals(Constantes.RIA)) {

			SecuenciaFlujo secuencia = secuenciaRepository.findByFolioAndEtapa(pe.getFolio(), Constantes.RIA);
			LocalDate fechaInicioRIA = LocalDate.parse(secuencia.getFecha(), format);

			Integer diasTranscurridos = getDiasTranscurridosCambioEtapa(fechaInicioRIA, obtenerFecha());

			if (diasTranscurridos > 5) {
				flujo.setEtapa(Constantes.RNP);
				flujoRepositorio.save(flujo);
				pe.setSuspendido(0);
				pe.setDiasPeriodo(30);
				pe.setIdColor(35);
				periodoRepository.save(pe);
				altaSecuenciaFlujo(pe.getFolio(), Constantes.RNP, "");
			}

		} else {

			SecuenciaFlujo secuencia = secuenciaRepository.findByFolioAndEtapa(pe.getFolio(), Constantes.GDM);
			if(secuencia!=null) {
				LocalDate fechaInicioGenerando = LocalDate.parse(secuencia.getFecha(), format);

				Integer diasTranscurridos = getDiasTranscurridosCambioEtapa(fechaInicioGenerando, obtenerFecha());

				if (diasTranscurridos == 5) {
					flujo.setPermiteAcuerdo(-1);
					flujoRepositorio.save(flujo);
				}
			}
		}
	}
	
	private void flujo3(Flujo flujo, Periodo pe) {
		
		if ((pe.getDiasPeriodo() == 5) && (pe.getDiasTranscurridos() !=null && pe.getDiasTranscurridos() == 5)) {
			
			SecuenciaFlujo secu = secuenciaRepository.findByFolioAndEtapa(flujo.getFolio(), Constantes.ACUERDO);
			
			if(secu==null) {
				flujo.setEtapa(Constantes.ACUERDO);
				flujoRepositorio.save(flujo);
				pe.setDiasPeriodo(15);
				periodoRepository.save(pe);
			
				altaSecuenciaFlujo(pe.getFolio(), Constantes.ACUERDO, "");
			}
			
		} else if (flujo.getEtapa().equals(Constantes.ACUERDO)) {

			SecuenciaFlujo secuencia = secuenciaRepository.findByFolioAndEtapa(pe.getFolio(), Constantes.ACUERDO);
			LocalDate fechaInicioAcuerdo = LocalDate.parse(secuencia.getFecha(), format);

			Integer diasTranscurridos = getDiasTranscurridosCambioEtapa(fechaInicioAcuerdo, obtenerFecha());

			if (diasTranscurridos == 3 || diasTranscurridos == 8) {
				flujo.setEtapa(Constantes.GOT2);
				flujo.setPermiteAcuerdo(1);
				flujoRepositorio.save(flujo);

				altaSecuenciaFlujo(pe.getFolio(), Constantes.GOT2, "");

			}
		} else if ((flujo.getEtapa().equals(Constantes.GOT2))
				|| (flujo.getEtapa().equals(Constantes.OTR))) {

			SecuenciaFlujo secuencia = secuenciaRepository.findByFolioAndEtapa(pe.getFolio(), Constantes.GOT2);
			LocalDate fechaInicioGenerando = LocalDate.parse(secuencia.getFecha(), format);

			
			Integer diasTranscurridos = getDiasTranscurridosCambioEtapa(fechaInicioGenerando, obtenerFecha());

			if (diasTranscurridos == 5 ) {
				flujo.setPermiteAcuerdo(-1);
				flujoRepositorio.save(flujo);
			}
		} else if (flujo.getEtapa().equals(Constantes.RIA)) {

			SecuenciaFlujo secuencia = secuenciaRepository.findByFolioAndEtapa(pe.getFolio(), Constantes.RIA);
			LocalDate fechaInicioRIA = LocalDate.parse(secuencia.getFecha(), format);

			Integer diasTranscurridos = getDiasTranscurridosCambioEtapa(fechaInicioRIA, obtenerFecha());

			if (diasTranscurridos > 5) {
				flujo.setEtapa(Constantes.RNP);
				flujoRepositorio.save(flujo);
				pe.setSuspendido(0);
				pe.setDiasPeriodo(15);
				pe.setIdColor(35);
				periodoRepository.save(pe);
				altaSecuenciaFlujo(pe.getFolio(), Constantes.RNP, "");
			}

		}
	}
	private Integer getDiasTranscurridosCambioEtapa(LocalDate fechaInicioAcuerdo, LocalDate fechaActual) {
		Integer incr = 0;

		while (fechaInicioAcuerdo.isBefore(fechaActual) || fechaInicioAcuerdo.isEqual(fechaActual)) {
			Dia dia = null;
			fechaInicioAcuerdo = fechaInicioAcuerdo.plusDays(1);
			String afecha[] = fechaInicioAcuerdo.toString().split("-");
			String sfecha = afecha[2] + "/" + afecha[1] + "/" + afecha[0];
			dia = diaRepository.findAll().stream()
					.filter(d -> d.getFecha().equals(sfecha) && d.getActivo().equals(Constantes.ACTIVO)).findAny()
					.orElse(null);
			if (dia == null) {
				if (fechaInicioAcuerdo.isBefore(fechaActual) || fechaInicioAcuerdo.isEqual(fechaActual)) {
					incr++;
				}
			}
			System.out.println("dentro");
		}
		System.out.println("incr: " + incr);

		return incr;
	}

	private void altaSecuenciaFlujo(String folio, String etapa, String rutaArchivo) {
		SecuenciaFlujo flu = new SecuenciaFlujo();
		flu.setFolio(folio);
		flu.setEtapa(etapa);
		flu.setFecha(obtenerFecha().toString());
		flu.setRutaArchivo(rutaArchivo);
		flu.setActivo("A");
		secuenciaRepository.save(flu);

	}

	private LocalDate obtenerFecha() {
		LocalDate fecha = LocalDate.now();

		return fecha;
	}

	private Integer getColorEstatus(int diasTranscurridos, int diasPermitidos) {
		Integer idColor = 0;

		switch (diasPermitidos) {
		case 5:
			if (diasTranscurridos >= 0 && diasTranscurridos <= 2) {
				idColor = 32;
			} else if (diasTranscurridos >= 3 && diasTranscurridos <= 4) {
				idColor = 34;
			} else if (diasTranscurridos == 5) {
				idColor = 33;
			} else if (diasTranscurridos > 5) {
				idColor = 35;
			}
			break;

		case 15:
			if (diasTranscurridos >= 0 && diasTranscurridos <= 7) {
				idColor = 32;
			} else if (diasTranscurridos >= 8 && diasTranscurridos <= 13) {
				idColor = 34;
			} else if (diasTranscurridos >= 14) {
				idColor = 33;
			}
			break;
		case 30:
			if (diasTranscurridos >= 0 && diasTranscurridos <= 14) {
				idColor = 32;
			} else if (diasTranscurridos >= 15 && diasTranscurridos <= 26) {
				idColor = 34;
			} else if (diasTranscurridos >= 27 && diasTranscurridos <= 30) {
				idColor = 33;
			} else if (diasTranscurridos > 30) {
				idColor = 35;
			}
			break;

		default:
			break;
		}

		return idColor;
	}
}
