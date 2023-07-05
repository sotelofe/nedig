package mx.org.inai.service.exencion.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.xwpf.usermodel.BreakClear;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import mx.org.inai.dto.exencion.ExencionPreguntasDTO;
import mx.org.inai.dto.exencion.ExencionRequestDTO;
import mx.org.inai.service.exencion.ExencionGeneraCuestionario;
import mx.org.inai.util.exencion.DateUtil;
import mx.org.inai.util.exencion.RutaUtil;

/**
 * The Class ExcencionGeneraCuestionarioImpl.
 *
 * @author A. Juarez
 */
@Component
public class ExencionGeneraCuestionarioImpl implements ExencionGeneraCuestionario {

	/** The Constant LOGGER. */
	//private static final Logger LOGGER = LoggerFactory.getLogger(ExencionGeneraCuestionarioImpl.class);
	/**
	 * The util.
	 */
	@Autowired
	private RutaUtil util;

	/**
	 * The Constant COLOR_BLACK.
	 */
	private static final String COLOR_BLACK = "000000";

	/**
	 * The Constant COLOR_RED.
	 */
	private static final String COLOR_RED = "AE105A";

	/**
	 * Agregar texto.
	 *
	 * @param document      the document
	 * @param texto         the texto
	 * @param idSubpregunta the id subpregunta
	 * @param pregunta      the pregunta
	 */
	private void agregarTexto(XWPFDocument document, String texto, int idSubpregunta, ExencionPreguntasDTO pregunta) {
		if (pregunta.getSubpregunta() == idSubpregunta) {
			XWPFParagraph paragraph = document.createParagraph();
			XWPFRun run = paragraph.createRun();
			run.setBold(false);
			run.setColor(COLOR_BLACK);
			run.setText(texto);
			run.addBreak(BreakClear.ALL);

			run = paragraph.createRun();
			run.setText(pregunta.getRespuesta());
			run.addBreak(BreakClear.ALL);
		}
	}

	/**
	 * Agregar titulo.
	 *
	 * @param document the document
	 * @param texto    the texto
	 */
	private void agregarTitulo(XWPFDocument document, String texto) {
		agregarTitulo(document, texto, COLOR_RED, true);
	}

	/**
	 * Agregar titulo.
	 *
	 * @param document the document
	 * @param texto    the texto
	 * @param color    the color
	 * @param bold     the bold
	 */
	private void agregarTitulo(XWPFDocument document, String texto, String color, boolean bold) {
		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run = paragraph.createRun();
		paragraph.setAlignment(ParagraphAlignment.BOTH);
		run.setBold(bold);
		run.setColor(color);
		run.setText(texto);
		run.addBreak(BreakClear.ALL);
	}

	/**
	 * Seccion 1.
	 *
	 * @param document the document
	 * @param pregunta the pregunta
	 */
	private void seccion1(XWPFDocument document, ExencionPreguntasDTO pregunta) {
		if (pregunta.getSubpregunta() == 1) {
			agregarTitulo(document,"1. La denominación y los objetivos generales y específicos que persigue la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología que implique un tratamiento intensivo o relevante de datos personales.");
		}
		agregarTexto(document, "a. Su denominación", 1, pregunta);
		agregarTexto(document, "b. El nombre de la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología que implique un tratamiento intensivo o relevante de datos personales.",
				2, pregunta);
		if (pregunta.getSubpregunta() == 3) {
			agregarTitulo(document, "c. Los objetivos generales y específicos que persigue la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología que implique un tratamiento intensivo o relevante de datos personales",
					COLOR_BLACK, false);
		}
		agregarTexto(document, "Objetivos generales", 3, pregunta);
		agregarTexto(document, "Objetivos específicos", 4, pregunta);
		agregarTexto(document,
				"d. Supuestos de tratamiento relevante o intensivo en lo general y/o en lo particular en los que encuadra la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología",
				5, pregunta);
		agregarTexto(document,
				"e. Razones por las cuales se considera que la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología implica un tratamiento intensivo o relevante de datos personales a consideración del sujeto obligado",
				6, pregunta);
	}

	/**
	 * Seccion 2.
	 *
	 * @param document the document
	 * @param pregunta the pregunta
	 */
	private void seccion2(XWPFDocument document, ExencionPreguntasDTO pregunta) {
		if (pregunta.getSubpregunta() == 1) {
			agregarTitulo(document, "2. Las finalidades del tratamiento intensivo o relevante de datos personales");
		}
		agregarTexto(document, "- Finalidades del tratamiento ", 2, pregunta);
	}

	/**
	 * Seccion 3.
	 *
	 * @param document the document
	 * @param pregunta the pregunta
	 */
	private void seccion3(XWPFDocument document, ExencionPreguntasDTO pregunta) {
		if (pregunta.getSubpregunta() == 1) {
			agregarTitulo(document, "3. Las razones o motivos que le permitieron determinar que la evaluación de impacto en la protección de datos personales compromete los efectos de la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología que implique un tratamiento intensivo o relevante de datos personales que pretende implementar o modificar, o bien, la situación de emergencia o urgencia que hacen inviable la presentación de ésta");
		}
		agregarTexto(document,
				"- Las razones o motivos que le permitieron determinar que la evaluación de impacto en la protección de datos personales compromete los efectos del tratamiento intensivo o relevante de datos personales que pretende implementar o modificar",
				2, pregunta);
		agregarTexto(document,
				"- La situación de emergencia o urgencia que hacen inviable la presentación de evaluación de impacto en la protección de datos personales",
				3, pregunta);
	}

	/**
	 * Seccion 4.
	 *
	 * @param document the document
	 */
	private void seccion4(XWPFDocument document) {
		agregarTitulo(document,
				"4. Las consecuencias negativas que se derivarían de la elaboración y presentación de la evaluación de impacto en la protección de datos personales");
	}

	/**
	 * Seccion 5.
	 *
	 * @param document the document
	 * @param pregunta the pregunta
	 */
	private void seccion5(XWPFDocument document, ExencionPreguntasDTO pregunta) {
		if (pregunta.getSubpregunta() == 1) {
			agregarTitulo(document,
					"5. El fundamento legal que habilitó el tratamiento de datos personales en el marco de la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología que se pretende poner en operación o modificar");
		}
		agregarTexto(document, "Fundamento legal", 2, pregunta);
		if (pregunta.getSubpregunta() == 3 && StringUtils.hasText(pregunta.getRespuesta())) {
		  agregarTexto(document, "Enlace al hipervínculo web", 3, pregunta);
		}
	}

	/**
	 * Seccion 6.
	 *
	 * @param document the document
	 * @param pregunta the pregunta
	 */
	private void seccion6(XWPFDocument document, ExencionPreguntasDTO pregunta) {
		if (pregunta.getSubpregunta() == 1) {
			agregarTitulo(document,
					"6. La fecha en que se puso en operación o modificó la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología que implique un tratamiento intensivo o relevante de datos personales, así como su periodo de duración");
		}
		String fecha = DateUtil.getFechaFormato(pregunta.getRespuesta());
		agregarTitulo(document, fecha, COLOR_BLACK, false);
	}

	/**
	 * Seccion 7.
	 *
	 * @param document the document
	 * @param pregunta the pregunta
	 */
	private void seccion7(XWPFDocument document, ExencionPreguntasDTO pregunta) {
		if (pregunta.getSubpregunta() == 1) {
			agregarTitulo(document,
					"7. La opinión técnica del oficial de protección de datos personales respecto del tratamiento intensivo o relevante de datos personales que implique la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología, en su caso");
		}
		agregarTexto(document, "- ¿Quién realiza la opinión?", 2, pregunta);
	}

	/**
	 * Seccion 8.
	 *
	 * @param document the document
	 * @param pregunta the pregunta
	 */
	private void seccion8(XWPFDocument document, ExencionPreguntasDTO pregunta) {
		if (pregunta.getSubpregunta() == 1) {
			agregarTitulo(document,
					"8. Los mecanismos o procedimientos adoptados por el responsable para que la política pública, programa, sistema o plataforma informática, aplicación electrónica o cualquier otra tecnología que implique un tratamiento intensivo o relevante de datos personales cumpla, desde el diseño y por defecto, con todas las obligaciones previstas en la Ley General o las legislaciones estatales en la materia y demás disposiciones aplicables.");
		}
		agregarTexto(document,
				"- Los mecanismos o procedimientos adoptados por el responsable para que el tratamiento intensivo o relevante de datos personales cumpla, desde el diseño con todas las obligaciones previstas en la Ley General o las legislaciones estatales en la materia y demás disposiciones aplicables.",
				2, pregunta);
		agregarTexto(document,
				"- Los mecanismos o procedimientos adoptados por el responsable para que el tratamiento intensivo o relevante de datos personales cumpla, por defecto con todas las obligaciones previstas en la Ley General o las legislaciones estatales en la materia y demás disposiciones aplicables",
				3, pregunta);
	}

	/**
	 * Seccion 9.
	 *
	 * @param document the document
	 * @param pregunta the pregunta
	 */
	private void seccion9(XWPFDocument document, ExencionPreguntasDTO pregunta) {
		if (pregunta.getSubpregunta() == 1) {
			agregarTitulo(document,
					"9. Cualquier otra información o documentos que considere conveniente hacer del conocimiento del Instituto.");
		}
		agregarTexto(document, "- Descripción general de la información adicional:", 2, pregunta);
		agregarTexto(document, "- Descripción general de los documentos anexos:", 3, pregunta);
	}

	/**
	 * Generar.
	 *
	 * @param form the form
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void generar(ExencionRequestDTO form) throws IOException {

		try (XWPFDocument document = new XWPFDocument()) {
			String archivo = String.format("%s%s.docx", util.getRuta(form.getTipoFlujo()),
					util.getNombreArchivo(form.getFolio()));
			for (ExencionPreguntasDTO pregunta : form.getPreguntas()) {
				switch (pregunta.getPregunta()) {
				case 1:
					seccion1(document, pregunta);
					break;
				case 2:
					seccion2(document, pregunta);
					break;
				case 3:
					seccion3(document, pregunta);
					break;
				case 4:
					seccion4(document);
					break;
				case 5:
					seccion5(document, pregunta);
					break;
				case 6:
					seccion6(document, pregunta);
					break;
				case 7:
					seccion7(document, pregunta);
					break;
				case 8:
					seccion8(document, pregunta);
					break;
				case 9:
					seccion9(document, pregunta);
					break;
				default:
					break;
				}
			}

			//LOGGER.info("Archivo: {}", archivo);
			try (OutputStream out = new FileOutputStream(new File(archivo))) {
				document.write(out);
			}
		}
	}

}
