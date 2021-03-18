package dm2e.adriancaballero.laberinto;

/**
 * @author Adrian Caballeo Orasio
 */
public class Point {
    private int x, y;
    private char symbol;
    private Point parent;

    /**
     * Inicializa un punto, reservando memoria y devolviendo el punto inicializado si lo ha hecho correctamente
     *
     * @param x      valor x del punto
     * @param y      valor y del punto
     * @param symbol simbolo del punto
     * @param parent padre del punto
     */
    public Point(int x, int y, char symbol, Point parent) { this.x = x;this.y = y;this.symbol = symbol;this.parent = parent; }
    public Point(int x, int y, char symbol) { this((x >= 0) ? x : -1, (y >= 0) ? y : -1, (FileChars.valueOf(symbol) != null || Movements.valueOf(symbol) != null) ? symbol : FileChars.ERRORCHAR.c, null); }
    public Point() { this(-1, -1, FileChars.ERRORCHAR.c, null); }

///** Libera la memoria dinamica reservada para un punto */public static void destroy(Point p) {if (p != null)p = null;}

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
        if (hasParent() && parent.getX() != -1 && parent.getY() != -1) {
            return this.parent;
        } else {
            return null;
        }
    }

    /**
     * @return false si no tiene padre
     */
    public boolean hasParent() {
        return (this.parent != null);
    }

    /**
     * Modifica la coordenda X de un punto dado.
     *
     * @param x the x to set
     * @return false si se produce algun error
     */
    public boolean setX(int x) {
        if (x >= 0) {
            this.x = x;
        }
        return (this.x == x);
    }

    /**
     * Modifica la coordenda Y de un punto dado
     *
     * @param y the y to set
     * @return false si se produce algun error
     */
    public boolean setY(int y) {
        if (x >= 0) {
            this.y = y;
        }
        return (this.y == y);
    }

    /**
     * Modifica el simbolo de un punto dado
     *
     * @param symbol the symbol to set
     * @return false si se produce algun error
     */
    public boolean setSymbol(char symbol) {
//        if (FileChars.valueOf(symbol) != null && Movements.valueOf(symbol) != null) {
        this.symbol = symbol;
//        }
        return (this.symbol == symbol);
    }

    /**
     * Modifica el punto padre de un punto dado
     *
     * @param parent the parent to set
     * @return false si se produce algun error
     */
    public boolean setParent(Point parent) {
        if (parent == null) {
            return false;
        }
        this.parent = parent;
        return true;
    }

    /**
     * Funciones derivadas que indican el tipo de punto que tenemos
     *
     * @return true if symbol of Point is Input, else false
     */
    public boolean isInput() {
        return (this.symbol == FileChars.INPUT.c);
    }

    /**
     * Funciones derivadas que indican el tipo de punto que tenemos
     *
     * @return true if symbol of Point is Output, else false
     */
    public boolean isOutput() {
        return (this.symbol == FileChars.OUTPUT.c);
    }

    /**
     * Funciones derivadas que indican el tipo de punto que tenemos
     *
     * @return true if symbol of Point is Barrier, else false
     */
    public boolean isBarrier() {
        return (this.symbol == FileChars.BARRIER.c);
    }

    /**
     * Funciones derivadas que indican el tipo de punto que tenemos
     *
     * @return true if symbol of Point is Space, else false
     */
    public boolean isSpace() {
        return (this.symbol == FileChars.SPACE.c);
    }

    /**
     * Funciones derivadas que indican el tipo de punto que tenemos
     *
     * @return true if symbol of Point is ErrorChar, else false
     */
    public boolean isErrorChar() {
        return (this.symbol == FileChars.ERRORCHAR.c);
    }

    /**
     * Funciones derivadas que indican el tipo de punto que tenemos
     *
     * @return true if symbol of Point is Up, else false
     */
    public boolean isUp() {
        return (this.symbol == Movements.UP.c);
    }

    /**
     * Funciones derivadas que indican el tipo de punto que tenemos
     *
     * @return true if symbol of Point is Down, else false
     */
    public boolean isDown() {
        return (this.symbol == Movements.DOWN.c);
    }

    /**
     * Funciones derivadas que indican el tipo de punto que tenemos
     *
     * @return true if symbol of Point is Left, else false
     */
    public boolean isLeft() {
        return (this.symbol == Movements.LEFT.c);
    }

    /**
     * Funciones derivadas que indican el tipo de punto que tenemos
     *
     * @return true if symbol of Point is Right, else false
     */
    public boolean isRight() {
        return (this.symbol == Movements.RIGHT.c);
    }

    /**
     * Funciones derivadas que indican el tipo de punto que tenemos
     *
     * @return true if symbol of Point is Visited, else false
     */
    public boolean isVisited() {
        return (this.symbol == Movements.VISITED.c);
    }

    /**
     * Funciones derivadas que indican el tipo de punto que tenemos
     *
     * @return true if symbol of Point is Actual, else false
     */
    public boolean isActual() {
        return (this.symbol == Movements.ACTUAL.c);
    }

    /**
     * @param p punto a comparar
     * @return Devuelve TRUE si los dos puntos pasados como argumentos son iguales (revisando todos sus campos, salvo padre). Devuelve FALSE en otro caso.
     */
    public boolean equals(Point p) {
        return p != null && (getX() == p.getX()) && (getY() == p.getY()) && (getSymbol() == p.getSymbol());
    }

    /**
     * Copia los datos de un punto a otro devolviendo el punto copiado (incluyendo la reserva de la memoria necesaria) si ha ido bien, o null en otro caso
     *
     * @return copia del punto
     * ####@throws CloneNotSupportedException en caso de error
     */
    protected Point copy() /*throws CloneNotSupportedException*/ {
        Point cp = new Point(this.x, this.y, this.symbol);
        cp.setParent(this.parent);
        return cp;
    }

//	int point_print(FILE *file, const Point *p);

    /**
     * Imprime en un fichero dado los datos de un punto con el siguiente formato:[(x,y): symbol]. Por ejemplo, un punto con simbolo #, con coordenada X 3 e Y 7 se representara como [(3, 7): #]. Ademas, devolvera el numero de caracteres que se han escrito con exito (mirar documentacion de fprintf)
     *
     * @return [(x, y): symbol]\n.
     */
    public String print() {
        return toString() + "\n";
    }
//<ScrollView
//    android:id="@+id/swv1"
//    android:layout_width="fill_parent"
//    android:layout_height="wrap_content">
//
//        <TextView android:id="@+id/tvGanardores" android:layout_width="fill_parent" android:layout_height="wrap_content" android:text="" />
//    </ScrollView>
    /**
     * @return [(x, y): symbol].
     */
    @android.annotation.SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("[(%2d, %2d): %c]", this.y, this.x, this.symbol);
    }
}
