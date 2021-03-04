package laberinto;

public class Point {
	private int x;
	private int y;
	private char symbol;
	private Point parent;

	/**
	 * Inicializa un punto, reservando memoria y devolviendo el punto inicializado
	 * si lo ha hecho correctamente o NULL si no
	 * 
	 * @param x
	 * @param y
	 * @param symbol
	 */
	public Point(int x, int y, char symbol) {
		this.x = x;
		this.y = y;
		this.symbol = symbol;
		this.parent = null;
	}

	/**
	 * 
	 */
	public Point() {
		this.x = -1;
		this.y = -1;
		this.symbol = FileChars.ERRORCHAR.c;
		this.parent = null;
	}

	/** Libera la memoria dinamica reservada para un punto */
//	public static void destroy(Point p) {if (p != null)p = null;}
	/**
	 * Devuelve la coordenada X de un punto dado, o -1 si se produce algun error
	 * 
	 * @return the x
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Devuelve la coordenada Y de un punto dado, o -1 si se produce algun error
	 * 
	 * @return the y
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Devuelve el simbolo de un punto dado, o ERRORCHAR si se produce algun error
	 * 
	 * @return the symbol
	 */
	public char getSymbol() {
		return this.symbol;
	}

	/**
	 * Devuelve el punto padre de un punto dado, o NULL si se produce algun error
	 * 
	 * @return the parent
	 */
	public Point getParent() {
		if (this.parent == null) {
			return null;
		} else {
			return this.parent;
		}
	}

	/**
	 * @return
	 */
	public boolean hasParent() {
		if (this.parent == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Modifica la coordenda X de un punto dado, devuelve ERROR si se produce algun
	 * error
	 * 
	 * @param x the x to set
	 * @return
	 */
	public boolean setX(int x) {
		if (x < 0) {
			return false;
		}
		this.x = x;
		return (this.x == x);
	}

	/**
	 * Modifica la coordenda Y de un punto dado, devuelve ERROR si se produce algun
	 * error
	 * 
	 * @param y the y to set
	 * @return
	 */
	public boolean setY(int y) {
		if (y < 0) {
			return false;
		}
		this.y = y;
		return (this.y == y);
	}

	/**
	 * Modifica el simbolo de un punto dado, devuelve ERROR si se produce algun
	 * error
	 * 
	 * @param symbol the symbol to set
	 * @return
	 */
	public boolean setSymbol(char symbol) {
		this.symbol = symbol;
		return (this.symbol == symbol);
	}

	/**
	 * Modifica el punto padre de un punto dado, o ERROR si se produce algun error
	 * 
	 * @param parent the parent to set
	 * @return
	 */
	public boolean setParent(Point parent) {
		if (parent == null) {
			return false;
		}
		this.parent = parent;
		return (this.parent == parent);
	}

	/**
	 * Funciones derivadas que indican el tipo de punto que tenemos
	 * 
	 * @return true if symbol of Point is Input, else false
	 */
	public boolean isInput() {
		if (this.symbol != FileChars.INPUT.c) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Funciones derivadas que indican el tipo de punto que tenemos
	 * 
	 * @return true if symbol of Point is Output, else false
	 */
	public boolean isOutput() {
		if (this.symbol != FileChars.OUTPUT.c) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Funciones derivadas que indican el tipo de punto que tenemos
	 * 
	 * @return true if symbol of Point is Barrier, else false
	 */
	public boolean isBarrier() {
		if (this.symbol != FileChars.BARRIER.c) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Funciones derivadas que indican el tipo de punto que tenemos
	 * 
	 * @return true if symbol of Point is Space, else false
	 */
	public boolean isSpace() {
		if (this.symbol != FileChars.SPACE.c) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * Funciones derivadas que indican el tipo de punto que tenemos
	 * 
	 * @return true if symbol of Point is Actual, else false
	 */
	public boolean isActual() {
		if (this.symbol != Movements.ACTUAL.c) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Devuelve TRUE si los dos puntos pasados como argumentos son iguales
	 * (revisando todos sus campos). Devuelve FALSE en otro caso.
	 * 
	 * @param p
	 * @return
	 */
	public boolean equals(Point p) {
		if (p == null || (getX() != p.getX()) || (getY() != p.getY()) || (getSymbol() != p.getSymbol())) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Copia los datos de un punto a otro devolviendo el punto copiado (incluyendo
	 * la reserva de la memoria necesaria) si todo ha ido bien, o NULL en otro caso
	 * 
	 * @return
	 * @throws CloneNotSupportedException
	 */
	protected Point copy() throws CloneNotSupportedException {
		Point cp = new Point(this.x, this.y, this.symbol);
		cp.setParent(this.parent);
		return cp;
	}

//	int point_print(FILE *file, const Point *p);
	/**
	 * Imprime en un fichero dado los datos de un punto con el siguiente
	 * formato:[(x,y): symbol]. Por ejemplo, un punto con simbolo #, con coordenada
	 * X 3 e Y 7 se representara como [(3, 7): #]. Ademas, devolvera el numero de
	 * caracteres que se han escrito con exito (mirar documentacion de fprintf)
	 * 
	 * @return
	 */
	public String print() {
		return toString() + "\n";
	}

	/** Formato: [(x,y): symbol]. */
	@Override
	public String toString() {
		return String.format("[(%2d, %2d): %c]", this.x, this.y, this.symbol);
	}
}
