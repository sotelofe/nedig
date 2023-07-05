package mx.org.inai.util.exencion;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * The Class DateUtil.
 *
 * @author A. Juarez
 */
public final class DateUtil {

    //private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    /**
     * The Constant FORMAT_YYYYMMDD.
     */
    public static final String FORMAT_YYYYMMDD = "yyyy-MM-dd";

    /**
     * The Constant FORMAT_DDMMYYYY.
     */
    public static final String FORMAT_DDMMYYYY = "dd/MM/yyyy";

    /**
     * The Constant ZONE.
     */
    private static final String ZONE = "America/Mexico_City";

    /**
     * Instantiates a new date util.
     */
    private DateUtil() {
    }

    /**
     * Date.
     *
     * @return the local date
     */
    public static LocalDate date() {
        return LocalDate.now(ZoneId.of(ZONE));
    }

    /**
     * Current date.
     *
     * @param patron the patron
     * @return the string
     */
    public static String currentDate(String patron) {
        return dateAsString(date(), patron);
    }

    /**
     * Date as string.
     *
     * @param date the date
     * @return the string
     */
    public static String dateAsString(final LocalDate date) {
        return dateAsString(date, FORMAT_YYYYMMDD);
    }

    /**
     * Date as string.
     *
     * @param date the date
     * @param patron the patron
     * @return the string
     */
    public static String dateAsString(final LocalDate date, String patron) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patron);
        return date.format(formatter);
    }

    /**
     * Gets the year.
     *
     * @return the year
     */
    public static int getYear() {
        return date().getYear();
    }

    /**
     * Gets the fecha formato.
     *
     * @param fecha the fecha
     * @return the fecha formato
     */
    public static String getFechaFormato(String fecha) {
        try {
            String[] afecha = fecha.split("-");
            return afecha[2] + "/" + afecha[1] + "/" + afecha[0];
        } catch (Exception ex) {
        	//LOGGER.info("Fecha: {}", fecha);
            //LOGGER.error("Error: {}", ex.getMessage());
            return fecha;
        }
    }

}
