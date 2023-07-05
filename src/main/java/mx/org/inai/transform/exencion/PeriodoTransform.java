package mx.org.inai.transform.exencion;

import java.time.LocalDate;
import java.util.Optional;
import mx.org.inai.dto.exencion.ExencionRequestDTO;
import mx.org.inai.model.Periodo;
import mx.org.inai.dto.Constantes;
import mx.org.inai.excepcion.exencion.FlujoException;
import mx.org.inai.model.Dia;
import mx.org.inai.repository.DiaRepository;
import mx.org.inai.repository.PeriodoRepository;
import mx.org.inai.util.exencion.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * The Class PeriodoTransform.
 *
 * @author A. Juarez
 */
@Component
@Qualifier("periodoConverter")
public class PeriodoTransform implements Transformer<Periodo, ExencionRequestDTO> {

	/** The dia repository. */
	@Autowired
	private DiaRepository diaRepository;

	/** The periodo repository. */
	@Autowired
	private PeriodoRepository periodoRepository;

	/**
	 * Calcular fecha final.
	 *
	 * @param numDias the num dias
	 * @return the string
	 */
	private String calcularFechaFinal(int numDias) {
		int i = 0;
		String fechaFinal = "";
		LocalDate fechaActual = DateUtil.date();
		while (i < numDias) {
			fechaActual = fechaActual.plusDays(1);
			fechaFinal = DateUtil.dateAsString(fechaActual, DateUtil.FORMAT_DDMMYYYY);
			Optional<Dia> dia = diaRepository.findByFecha(fechaFinal);
			if (!dia.isPresent()) {
				i++;
			}
		}
		return fechaFinal;
	}

	/**
	 * Obtener periodo registrar.
	 *
	 * @param form the form
	 * @return the periodo
	 */
	private Periodo obtenerPeriodoRegistrar(ExencionRequestDTO form) {
		Periodo periodo = new Periodo();
		periodo.setFolio(form.getFolio());
		periodo.setFechaInicioPeriodo(DateUtil.currentDate(DateUtil.FORMAT_DDMMYYYY));
		periodo.setFechaFinPeriodo(calcularFechaFinal(form.getNumDias()));
		periodo.setDiasPeriodo(form.getNumDias());
		periodo.setIdColor(32);
		periodo.setActivo(Constantes.ACTIVO);
		return periodo;
	}

	/**
	 * Obtener periodo aceptar.
	 *
	 * @param form the form
	 * @return the periodo
	 */
	private Periodo obtenerPeriodoAceptar(ExencionRequestDTO form) {
		Optional<Periodo> optPeriodo = periodoRepository.findByFolio(form.getFolio());
		if (!optPeriodo.isPresent()) {
			throw new FlujoException("El periodo no se encontro con el siguiente folio: " + form.getFolio());
		}
		Periodo periodo = optPeriodo.get();
		periodo.setFechaInicioPeriodo(DateUtil.currentDate(DateUtil.FORMAT_DDMMYYYY));
		periodo.setFechaFinPeriodo(calcularFechaFinal(form.getNumDias()));
		periodo.setDiasPeriodo(form.getNumDias());
		return periodo;
	}

	/**
	 * Obtener periodo default.
	 *
	 * @param form the form
	 * @return the periodo
	 */
	private Periodo obtenerPeriodoDefault(ExencionRequestDTO form) {
		Optional<Periodo> optPeriodo = periodoRepository.findByFolio(form.getFolio());
		if (!optPeriodo.isPresent()) {
			throw new FlujoException("El periodo no se encontro con el siguiente folio: " + form.getFolio());
		}
		Periodo periodo = optPeriodo.get();
		periodo.setActivo("A");
		return periodo;
	}
	
	private Periodo obtenerOpinion(ExencionRequestDTO form) {
		Optional<Periodo> optPeriodo = periodoRepository.findByFolio(form.getFolio());
		if (!optPeriodo.isPresent()) {
			throw new FlujoException("El periodo no se encontro con el siguiente folio: " + form.getFolio());
		}
		Periodo periodo = optPeriodo.get();
		periodo.setActivo("I");
		return periodo;
	}
	
	private Periodo obtenerPeriodoRia(ExencionRequestDTO form) {
		Optional<Periodo> optPeriodo = periodoRepository.findByFolio(form.getFolio());
		if (!optPeriodo.isPresent()) {
			throw new FlujoException("El periodo no se encontro con el siguiente folio: " + form.getFolio());
		}
		Periodo periodo = optPeriodo.get();
		periodo.setSuspendido(1);
		return periodo;
	}
	
	private Periodo obtenerPeriodoRiaAceptada(ExencionRequestDTO form) {
		Optional<Periodo> optPeriodo = periodoRepository.findByFolio(form.getFolio());
		if (!optPeriodo.isPresent()) {
			throw new FlujoException("El periodo no se encontro con el siguiente folio: " + form.getFolio());
		}
		Periodo periodo = optPeriodo.get();
		periodo.setSuspendido(0);
		return periodo;
	}

	/**
	 * Transform.
	 *
	 * @param form the form
	 * @return the periodo
	 */
	@Override
	public Periodo transform(ExencionRequestDTO form) {
		Periodo periodo;
		switch (form.getTipoEnvio()) {
		case REGISTRAR:
			periodo = obtenerPeriodoRegistrar(form);
			break;
		case ACEPTAR: periodo = obtenerPeriodoRiaAceptada(form);
		break;
		case RIA: periodo = obtenerPeriodoRia(form);
		break;
		case ENVIAR_RIA:
			periodo = obtenerPeriodoAceptar(form);
			break;
		case REGISTRAR_OPINION:
			periodo= obtenerOpinion(form);
			break;
		default:
			periodo = obtenerPeriodoDefault(form);
			break;
		}
		return periodo;
	}

	@Override
	public Periodo transform(ExencionRequestDTO form, String ruta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Periodo transform(ExencionRequestDTO form, boolean actualizacion) {
		// TODO Auto-generated method stub
		return null;
	}
}