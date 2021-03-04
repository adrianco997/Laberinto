package laberinto;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Maze {
	private int nX, nY;
	private Point coord[][];

	/**
	 * Inicializa un mapa, reservando memoria y devolviendo el mapa inicializado si
	 * lo ha hecho correctamente o NULL si no
	 */
	public Maze() {
		this.coord = new Point[Utils.MAX][Utils.MAX];
		for (int i = 0; i < Utils.MAX; i++) {
			for (int j = 0; j < Utils.MAX; j++) {
				this.coord[i][j] = null;
			}
		}
		this.nX = -1;
		this.nY = -1;
	}

	/** Libera la memoria dinamica reservada para un mapa */
	// void map_destroy(Map *pl);
	/**
	 * Devuelve el numero de filas de un mapa dado, o -1 si se produce algun error
	 */
	public int getNx() {
		return nX;
	}

	/**
	 * Devuelve el numero de columnas de un mapa dado, o -1 si se produce algun
	 * error
	 */
	public int getNy() {
		return nY;
	}

	/**
	 * Devuelve el punto de entrada en un mapa dado, o NULL si se produce algun
	 * error o no existe un punto de ese tipo
	 */
	public Point getInput() {
		for (int x = 0; x < this.nX; x++) {
			for (int y = 0; y < this.nY; y++) {
				if (getPoint(x, y) != null) {
					if (getPoint(x, y).isInput() == true) {
						return getPoint(x, y);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Devuelve el punto de salida en un mapa dado, o NULL si se produce algun error
	 * o no existe un punto de ese tipo
	 */
	public Point getOutput() {
		for (int x = 0; x < this.nX; x++) {
			for (int y = 0; y < this.nY; y++) {
				if (getPoint(x, y) != null) {
					if (getPoint(x, y).isOutput() == true) {
						return getPoint(x, y);
					}
				}
			}
		}
		return null;
	}

//	public Point[][] getCoord() {return coord;}
	/** Devuelve el punto situado en (x,y), o NULL si se produce algun error */
	public Point getPoint(int x, int y) {
		if (x < 0 || x > getNx() || y < 0 || y > getNy()) {
			return null;
		}
		return this.coord[x][y];
	}

	/**
	 * Devuelve el punto resultante al realizar un movimiento en un mapa a partir de
	 * un punto inicial, o NULL si se produce algun error
	 */
	public Point getNeighbor(Point p, Movements strat) {
		int x = p.getX(), y = p.getY();
		if (p == null || strat == Movements.RIGHT && strat == Movements.UP && strat == Movements.LEFT
				&& strat == Movements.DOWN /* && mov == Moves.STAY */) {
			return null;
		}
		if (strat == Movements.DOWN/* Moves.RIGHT */) {
			if (x < this.nX) {
				if ((this.coord[x + 1][y] != null)) {
					x++;
				} else {
					System.out.printf("coord[%d+1][%d] no existe\n", x, y);
				}
			} else {
				System.out.printf("coord[%d+1][%d] x es mayor que Nrows=%d\n", x, y, nX);
			}
		} //
		else if (strat == Movements.LEFT/* Moves.UP */) {
			if (y > 0) {
				if ((this.coord[x][y - 1] != null)) {
					y--;
				} else {
					System.out.printf("coord[%d][%d-1] no existe\n", x, y);
				}
			} else {
				System.out.printf("coord[%d][%d-1] y es menor que 0\n", x, y);
			}
		} //
		else if (strat == Movements.UP/* Moves.LEFT */) {
			if (x > 0) {
				if ((this.coord[x - 1][y] != null)) {
					x--;
				} else {
					System.out.printf("coord[%d-1][%d] no existe\n", x, y);
				}
			} else {
				System.out.printf("coord[%d-1][%d] x es menor que 0\n", x, y);
			}
		} //
		else if (strat == Movements.RIGHT/* Moves.DOWN */) {
			if (y < this.nY) {
				if ((this.coord[x][y + 1] != null)) {
					y++;
				} else {
					System.out.printf("coord[%d][%d+1] no existe\n", x, y);
				}
			} else {
				System.out.printf("coord[%d][%d+1] y es mayor que Ncols=%d\n", x, y, nY);
			}
		}
		return getPoint(x, y);
	}

	/* Indica el tamaño de un mapa, devuelve NULL si se produce algun error */
	public boolean setSize(int nX, int nY) {
		if (nX <= 0 || nX > Utils.MAX || nY <= 0 || nY > Utils.MAX) {
			return false;
		}
		this.nX = nX;
		this.nY = nY;
		return (this.nX == nX && this.nY == nY);
	}

	/**
	 * Añade un punto a un mapa dado reservando nueva memoria (de ahi que el
	 * argumento sea declarado como const), o modifica el punto si ya se encuentra.
	 * Debe comprobar si el punto es de tipo Output o Input para guardarlo como
	 * corresponda. Devuelve OK si todo ha ido correctamente (se ha podido
	 * incluir/actualizar el punto).
	 */
	public boolean setPoint(Point p) {
		if (p == null) {
			System.err.println("error en setPoint");
			return false;
		}
		int x = p.getX(), y = p.getY();
		if (x < 0 || x > getNx() || y < 0 || y > getNy()) {
			return false;
		}
		if (p.getSymbol() == FileChars.INPUT.c && getInput() != null/* && p == getInput() */) {
			this.getInput().setSymbol(FileChars.SPACE.c);
		}
		if (p.getSymbol() == FileChars.OUTPUT.c && getOutput() != null/* && p == getOutput() */) {
			this.getOutput().setSymbol(FileChars.SPACE.c);
		}
		this.coord[x][y] = p;
		return (getPoint(x, y).equals(p));
	}

	public boolean setPoint(int x, int y, char symbol) {
		return (this.setPoint(new Point(x, y, symbol)));
	}

//	public void setNcols(int nX) {this.nX = nX;}
//	public void setNrows(int nY) {this.nY = nY;}
//	public void setCoord(Point[][] coord) {this.coord = coord;}
	/**
	 * Imprime en un fichero dado los datos de un mapa. Ademas, devolvera el numero
	 * de caracteres que se han escrito con exito (mirar documentacion de fprintf)
	 */
//	int map_print(FILE *file, const Map *pl);
	public int length() {
		String txt = String.format("%s", this.toString());
		return txt.toCharArray().length;
	}

	@Override
	public String toString() {
		String txt = "";
//		txt+= nX + " " + nY + "\n";
		for (int x = 0; x < nX; txt += "\n", x++) {
			for (int y = 0; y < nY; y++) {
				try {
					txt += String.format("%c", getPoint(x, y)/* coord[x][y] */.getSymbol());
				} catch (Exception e) {
					System.err.println("[" + x + "," + y + "] en [" + nX + "," + nY + "]");
				}
			}
		}
		return txt;
	}

	public String toCoordString() {
		String txt = ""; // txt+= nX + " " + nY + "\n";
		for (int x = 0; x < nX; txt += "\n", x++) {
			for (int y = 0; y < nY; y++) {
				try {
					txt += String.format("%c", getPoint(x, y));
				} catch (Exception e) {
					txt += String.format("[(%2d, %2d)ERR]", x, y, FileChars.ERRORCHAR.c);
				}
			}
		}
		return txt;
	}

	/****************************************************************/
	/** Lee los datos de un archivo necesarios de un archivo para crear un mapa */
	public boolean read(File pf) {
		if (pf == null) {
			System.err.println("Error in read.File == null");
			return false;
		}
		/* creamos punto que se utiliza como buffer */
//		Point temp = new Point(0, 0, FileChars.ERRORCHAR.c);
//		if(temp==null){System.err.println("Error in read.temp = new Point()");return false;}
		/* asignamos dimension al laberinto */
		List<String> lineas = null;
		try {
			lineas = Files.readAllLines(Paths.get(pf.toURI()), Charset.forName("ISO-8859-1"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (lineas == null) {
			System.err.println("Error in read.Files.readAllLines()");
			return false;
		}
		int nX = Integer.parseInt(lineas.get(0).split(" ")[0]), nY = Integer.parseInt(lineas.get(0).split(" ")[1]);
		boolean st = this.setSize(nX, nY);
		if (st == false) {
			System.err.println("Error in read.setSize()");
			return false;
		}
		/* leemos el fichero linea a linea */
		char buff[] = new char[Utils.MAX];
		for (int x = 0; x < nX; x++) {
			buff = lineas.get(x + 1).toCharArray();
			for (int y = 0; y < nY; y++) {
				char caracter = buff[y];
				/* ajustamos los atributos del punto leido (falta añadir control de errores) */
//				if (temp.setX(x) == false) {System.err.println("Error in read.temp.setX(" + x + ")");}
//				if (temp.setY(y) == false) {System.err.println("Error in read.temp.setY(" + x + ")");}
//				if (temp.setSymbol(caracter) == false) {System.err.println("Error in read.temp.setSymbol(" + caracter + ")");}
				/* insertamos el punto en el laberinto (falta añadir control de errores) */
//				if (this.setPoint(temp) == false) {System.err.println("Error in read.temp.setPoint(" + temp + ")");}
				this.setPoint(new Point(x, y, caracter));
			}
		}
		return true;
	}

	/** Pasa los datos de un mapa a una pila, Null en caso de error */
	public Stack<Point> toStack() {
		Stack<Point> stack = new Stack<Point>();
		Point cp = null;
		for (int y = 0; y < this.getNy(); y++) {
			for (int x = 0; x < this.getNx(); x++) {
				if (stack.size() == this.getNy() * this.getNx()) {
					System.err.println("Stack is too small for maze.");
					return null;
				}
				cp = this.getPoint(x, y);
				if (stack.push(cp) == null) {
					System.err.println("Error while adding point to stack.");
					return null;
				}
				cp = null;
			}
		}
		return stack;
	}

	/** Pasa los datos de un mapa a una cola, Null en caso de error */
	public Queue<Point> toQueue() {
		Queue<Point> queue = new LinkedList<Point>();
		Point cp = null;
		int x = 0, y = 0;
		for (y = 0; y < this.getNy(); y++) {
			for (x = 0; x < this.getNx(); x++) {
				if (queue.size() == this.getNy() * this.getNx()) {
					System.err.println("Queue is too small for maze.");
					return null;
				}
				cp = this.getPoint(x, y);
				if (queue.add(cp) == false) {
					System.err.println("Error while adding point to queue.\n");
					return null;
				}
				cp = null;
			}
		}
		return queue;
	}

	/**
	 * Realiza el recorrido completo de un mapa en busca del OUTPUT desde el INPUT,
	 * usa una pila.
	 */
	public int deepSearchStack(Point input, Movements[] strat) {
		Stack<Point> stack = new Stack<Point>();
		Point cp = null, neighbor = null;
		stack.push(input);
		while (stack.isEmpty() == false) {
			cp = (Point) stack.pop();
			if (cp.getSymbol() != Movements.VISITED.c) {
				if (this.getPoint(cp.getX(), cp.getY()).setSymbol(Movements.VISITED.c) == false) {
					return -1;
				}
				for (int i = strat.length - 1; i >= 0; i--) {
					/* stack is LIFO-> movements with higher preference have to be checked last */
					neighbor = this.getNeighbor(cp, strat[i]);
					if (neighbor != null) {
						if (neighbor.isOutput() == true) {
							neighbor.setParent(this.getPoint(cp.getX(), cp.getY()));
							return this.pathPaint(neighbor);
						}
						if (neighbor.isSpace() == true) {
							neighbor.setParent(this.getPoint(cp.getX(), cp.getY()));
							if (stack.push(neighbor) == null) {
								System.err.println("Error in stack_push.");
								return -1;
							}
						}
						neighbor = null;
					}
				}
			}
			cp = null;
		}
		return -1;
	}

	/**
	 * Realiza el recorrido completo de un mapa en busca del OUTPUT desde el INPUT,
	 * usa una cola.
	 */
	public int breadthSearchQueue(Point input, Movements strat[]) {
		Point cp = null, neighbor = null;
		Queue<Point> queue = new LinkedList<Point>();
		queue.add(input);
		while (queue.isEmpty() == false) {
			cp = (Point) queue.remove();
			if (cp.getSymbol() != Movements.VISITED.c) {
				if (cp.isInput() != true) {
					if (this.getPoint(cp.getX(), cp.getY()).setSymbol(Movements.VISITED.c) == false) {
						return -1;
					}
				}
				for (int m = 0; m < strat.length; m++) {
					/* queue is FIFO-> movements with higher preference have to be checked first */
					neighbor = this.getNeighbor(cp, strat[m]);
					if (neighbor != null) {
						if (neighbor.isOutput() == true) {
							neighbor.setParent(this.getPoint(cp.getX(), cp.getY()));
							return this.pathPaint(neighbor);
						}
						if (neighbor.isSpace() == true) {
							neighbor.setParent(this.getPoint(cp.getX(), cp.getY()));
							if (queue.add(neighbor) == false) {
								System.err.println("Error in queue_insert.\n");
								return -1;
							}
						}
						neighbor = null;
					}
				}
			}
			cp = null;
		}
		return -1;
	}

	Point deepSearchRec(Point input, Movements strat[], boolean compare) {
		Point neighbor = null, auxpoint = null;
		if (compare == true) {
			System.out.println("Current maze:");
			System.out.println(this.toString());
		}
		if (input.isOutput()) {
			return input;
		}
		input.setSymbol(Movements.VISITED.c);
		for (int i = 0; i < strat.length; i++) {
			neighbor = this.getNeighbor(input, strat[i]);
			if (neighbor != null) {
				if (neighbor.getSymbol() != Movements.VISITED.c && neighbor.getSymbol() != Movements.ACTUAL.c
						&& neighbor.getSymbol() != FileChars.BARRIER.c) {
					neighbor.setParent(this.getPoint(input.getX(), input.getY()));
					if (neighbor.isOutput() == false) {
						neighbor.setSymbol(Movements.ACTUAL.c);
					}
					auxpoint = this.deepSearchRec(neighbor, strat, compare);
					if (auxpoint != null) {
						if (auxpoint.getParent().equals(input)) {
							/* if(compare==true){System.out.println("Solution!");paintPath();} */}
						return auxpoint;
					}
				}
			}
		}
		return null;
	}

	/****************************************************************/
	public int pathPaint(Point output) {
		int pathlength = 0;
		Point cp = output;
		if (cp == null) {
			return -1;
		}
		while (cp.getParent() != null) {
			if (cp.getParent().getX() > cp.getX()) {
				cp.getParent().setSymbol(Movements.UP.c);
			} else if (cp.getParent().getX() < cp.getX()) {
				cp.getParent().setSymbol(Movements.DOWN.c);
			} else if (cp.getParent().getY() > cp.getY()) {
				cp.getParent().setSymbol(Movements.LEFT.c);
			} else if (cp.getParent().getY() < cp.getY()) {
				cp.getParent().setSymbol(Movements.RIGHT.c);
			} else {
				cp.getParent().setSymbol(Movements.ACTUAL.c);
			}
			cp = cp.getParent();
			pathlength++;
		}
		cp.setSymbol(FileChars.INPUT.c);
		output.setSymbol(FileChars.OUTPUT.c);
		return pathlength;
	}

	public void paintPath() {
		Point cp = this.getOutput();
		while (cp.getParent() != null) {
			System.out.print(cp + " <-- ");
			cp = cp.getParent();
		}
		cp.setSymbol(FileChars.INPUT.c);
		System.out.println(cp);
	}

	/****************************************************************/
	public void startMaze() {
		Scanner scan = new Scanner(System.in);
		Point input = getInput();
		Point output = getOutput();
		Point actual = input;
		int moves = 0;
		char move;
		System.out.println("START");
		while (actual.equals(output) == false) {
			System.out.println(Utils.formatL("", '-', 108));
			System.out.println(this.toString());
			System.out.printf("Moves: %3d              w%n", moves);
			System.out.print("Introduce movimiento: a s d: ");
			move = scan.next().charAt(0);
			try {
				Point neighbor = null;
				switch (move) {
				case 'a': {
					neighbor = this.getNeighbor(actual, Movements.LEFT);
					break;
				}
				case 'd': {
					neighbor = this.getNeighbor(actual, Movements.RIGHT);
					break;
				}
				case 'w': {
					neighbor = this.getNeighbor(actual, Movements.UP);
					break;
				}
				case 's': {
					neighbor = this.getNeighbor(actual, Movements.DOWN);
					break;
				}
				default: {
					throw new IllegalArgumentException("Unexpected value: " + move);
				}
				}
				if (neighbor.isOutput() == false) {
					if (actual.isInput() == false && actual.isOutput() == false) {
						actual.setSymbol(FileChars.SPACE.c);
					}
					if (neighbor.isInput() == false) {
						neighbor.setSymbol(Movements.ACTUAL.c);
					}
				} else {
					System.out.println("isOutput");
					actual.setSymbol(FileChars.SPACE.c);
				}
				moves++;/* if(temp.equals(actual)==false){moves++;} */
				actual = neighbor;
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		System.out.println(Utils.formatL("", '-', 108));
		System.out.println(this.toString());
		System.out.println("YOU WIN!! in " + moves + " MOVESMENTS");
		scan.close();
	}

}
