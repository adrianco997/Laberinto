package laberinto;

/**
 * Constantes publicas que definen los tipos de puntos que se permiten en un
 * laberinto.<br>
 * Simbolos de entrada de fichero
 * 
 * @author Adrian Caballeo Orasio
 */
public /* static */ enum FileChars {
	ERRORCHAR('E'), //
	INPUT('i'), //
	OUTPUT('o'), //
	BARRIER('+'), //
	SPACE(' '), //
	;

	public final char c;

	private FileChars(char label) {
		this.c = label;
	}

	public static FileChars valueOf(char c) {
		for (FileChars m : values()) {
			if (m.c == c) {
				return m;
			}
		}
		return null;
	}

}
// typedef enum {RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3, STAY = 4} Move;
