package mx.org.inai.excepcion.exencion;

/**
 * The Class FlujoException.
 *
 * @author A. Juarez
 */
public class FlujoException extends RuntimeException {

    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new instance of <code>FlujoException</code> without detail
     * message.
     */
    public FlujoException() {
    }

    /**
     * Constructs an instance of <code>FlujoException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public FlujoException(String msg) {
        super(msg);
    }
    
    /**
     * Instantiates a new flujo exception.
     *
     * @param ex the ex
     */
    public FlujoException(Throwable ex) {
        super(ex);
    }
}
