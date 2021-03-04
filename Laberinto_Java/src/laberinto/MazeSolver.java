package laberinto;

import java.io.File;

public class MazeSolver {
	/**
	 * llama a stackSolver, queueSolver y recursiveSolver con una matriz de
	 * movimientos
	 */
	public static void runSolver(String mfile, Movements strat[][]) throws Exception {
		for (int i = 0; i < strat.length; i++) {
			if (strat[i].length != 4) {
				System.err.println("Numero de movimientos incorrecto.");
				return;
			}
			System.out.printf("ESTRATEGIA %s, %s, %s, %s\n", strat[i][0], strat[i][1], strat[i][2], strat[i][3]);
			struct("pila", mfile, strat[i], false, 2);
			struct("cola", mfile, strat[i], false, 2);
			struct("algoritmo recursivo", mfile, strat[i], false, 3);
		}
	}

	/**/
	public static void struct(String solver, String mfile, Movements strat[], boolean compare, int type)
			throws Exception {
		String texto = "SALIDA ENCONTRADA, longitud del camino con %s y estrategia %s, %s, %s, %s: %d unidades.\n";
		System.out.println(Utils.formatR("Con " + solver + ":", '-', 108));
		int pathlength = -1;
		if (type == 1) {
			pathlength = stackSolver(mfile, strat);
		} else if (type == 2) {
			pathlength = queueSolver(mfile, strat);
		} else if (type == 3) {
			pathlength = recursiveSolver(mfile, strat, true);
		} else {
		}
		if (pathlength != -1) {
			System.out.printf(texto, solver, strat[0], strat[1], strat[2], strat[3], pathlength);
		} else {
			System.err.println("SALIDA NO ENCONTRADA.");
		}
		System.out.println(Utils.formatL("", '-', 108));
	}

	/****************************************************************/
	/** resolve un mapa usando una pila. */
	public static int stackSolver(String mfile, Movements strat[]) throws Exception {
		File file = new File(mfile);// file = fopen(mfile,"r");
		Maze maze = new Maze();
		if (file == null || maze == null || strat.length != 4) {
			return -1;
		}
		if (maze.read(file) == false) {
			System.err.println("Error en recursiveSolver.");
			return -1;
		}
		System.out.println("Maze:\n" + maze.toString());
		System.out.println("Input:\n" + maze.getInput().print());
		int pathlength = 0;
		pathlength = maze.deepSearchStack(maze.getInput(), strat);
		System.out.println(((pathlength != -1) ? "Output found, length of path: " + pathlength : "No path found."));
		System.out.println(maze.toString());
		System.out.printf("Final maze: %s%n", maze.pathPaint(maze.getOutput()));
		return pathlength;
	}

	/** resolve un mapa usando una cola. */
	public static int queueSolver(String mfile, Movements strat[]) throws Exception {
		File file = new File(mfile);
		Maze maze = new Maze();
		if (file == null || maze == null || strat.length != 4) {
			return -1;
		}
		if (maze.read(file) == false) {
			System.err.println("Error en recursiveSolver.");
			return -1;
		}
		System.out.println("Maze:\n" + maze.toString());
		System.out.println("Input:\n" + maze.getInput().print());
		int pathlength = 0;
		pathlength = maze.breadthSearchQueue(maze.getInput(), strat);
		System.out.println(((pathlength != -1) ? "Output found, length of path: " + pathlength : "No path found."));
		System.out.println(maze.toString());
		System.out.printf("Final maze: %s%n", maze.pathPaint(maze.getOutput()));
		return pathlength;
	}

	/**
	 * resolve un mapa usando un algoritmo recursivo.
	 */
	public static int recursiveSolver(String mfile, Movements[] strat, boolean compare) throws Exception {
		File file = new File(mfile);
		Maze maze = new Maze();
		if (file == null || maze == null || strat.length != 4) {
			return -1;
		}
		if (maze.read(file) == false) {
			System.err.println("Error en recursiveSolver.");
			return -1;
		}
		System.out.println("Maze:\n" + maze.toString() + "\n" + "Input:\n" + maze.getInput().print());
		int pathlength = 0;
		Point out = maze.deepSearchRec(maze.getInput(), strat, compare);
		if (out != null) {
			pathlength = maze.pathPaint(out);
			System.out.printf("Output is in point: %s%n", out.toString());
		} else {
			pathlength = -1;
		}
		System.out.println(((pathlength != -1) ? "Output found, length of path: " + pathlength : "No path found."));
		System.out.println(maze.toString());
		System.out.printf("Final maze: %s%n", maze.pathPaint(maze.getOutput()));
		return pathlength;
	}
}
