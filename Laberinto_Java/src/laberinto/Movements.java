package laberinto;

/**
 * Caracteres de movimiento
 * 
 * @author Adrian Caballeo Orasio
 */
public enum Movements {
	RIGHT('>'), //
	UP('^'), //
	LEFT('<'), //
	DOWN('v'), //
	/**
	 * Simbolo para un espacio ya visitado<br>
	 */
	VISITED('x'), //
	/**
	 * Simbolo para un espacio actualmente ocupado<br>
	 */
	ACTUAL('P'), //
	;

	public final char c;

	private Movements(char label) {
		this.c = label;
	}

	public static Movements valueOf(char c) {
		for (Movements m : values()) {
			if (m.c == c) {
				return m;
			}
		}
		return null;
	}
}