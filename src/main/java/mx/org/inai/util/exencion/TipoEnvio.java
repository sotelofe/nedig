package mx.org.inai.util.exencion;

/**
 * The Enum TipoEnvio.
 *
 * @author A. Juarez
 */
public enum TipoEnvio {

	/** The registrar. */
	REGISTRAR(1),
	/** The aceptar. */
	ACEPTAR(2),
	/** The respuesta. */
	RESPUESTA(3),
	/** The ria. */
	RIA(4),
	/** The enviar ria. */
	ENVIAR_RIA(5),
	
	REGISTRAR_OPINION(6);

	/** The valor. */
	private final int valor;

	/**
	 * Instantiates a new tipo envio.
	 *
	 * @param valor the valor
	 */
	TipoEnvio(int valor) {
		this.valor = valor;
	}

	/**
	 * Gets the valor.
	 *
	 * @return the valor
	 */
	public int getValor() {
		return valor;
	}
}
