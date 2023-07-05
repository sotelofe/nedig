package mx.org.inai.util.exencion;

/**
 * The Enum TipoFlujo.
 *
 * @author Negocios Digitales
 */
public enum TipoFlujo {

	/** The consultas. */
	CONSULTAS(1),
	/** The eipdp. */
	EIPDP(2),
	/** The exencion. */
	EXENCION(3);

	/** The valor. */
	private final int valor;

	/**
	 * Instantiates a new tipo flujo.
	 *
	 * @param valor the valor
	 */
	TipoFlujo(int valor) {
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
