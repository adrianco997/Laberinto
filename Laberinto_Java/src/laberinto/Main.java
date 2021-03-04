package laberinto;

import java.io.File;

public class Main {
//	OBJS_p4e1 = p4_e1.o map_solver.o point.o map.o stack_fp.o functions.o queue.o
	private final static Movements strat[][] = { //
			{ Movements.UP, Movements.DOWN, Movements.LEFT, Movements.RIGHT, }, //
			{ Movements.RIGHT, Movements.UP, Movements.DOWN, Movements.LEFT, }, //
			{ Movements.LEFT, Movements.RIGHT, Movements.UP, Movements.DOWN, }, //
			{ Movements.DOWN, Movements.LEFT, Movements.RIGHT, Movements.UP, },//
	};
	private final static Movements strat1[] = { Movements.LEFT, Movements.UP, Movements.RIGHT, Movements.DOWN, };

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			main(new String[] { "m1.txt" });
//			main(new String[] { "m2.txt" });
//			main(new String[] { "m3.txt" });
//			main(new String[] { "m4.txt" });
			return;
		}
		if (args.length < 1) {
			System.err.println("Falta el fichero de datos.");
			return;
		}
		/* ejsOld(args, 0); */
		play(args);
	}

	public static void ejsOld(String[] args, int i) throws Exception {
		String texto = "SALIDA ENCONTRADA, longitud del camino con %s y estrategia %s, %s, %s, %s: %d unidades.\n",
				tipo = "algoritmo recursivo";
		System.out.println("->Recursivo:\n");
		int pathlength = MazeSolver.recursiveSolver(args[i], strat1, true);
		if (pathlength != -1) {
			System.out.printf(texto, tipo, strat[i][0], strat[i][1], strat[i][2], strat[i][3], pathlength);
		} else {
			System.err.println("SALIDA NO ENCONTRADA.");
		}
		MazeSolver.runSolver(args[0], strat);
	}

	public static void play(String[] args) throws Exception {
		String mfile = args[0];
		File file = new File(mfile);// file = fopen(mfile,"r");
		Maze maze = new Maze();
		if (file == null || maze == null || strat.length != 4) {
			return;
		}
		if (maze.read(file) == false) {
			System.err.println("Error en play.read");
			return;
		}
		if (checkFile(args[0], strat1) != -1) {
			maze.startMaze();
		} else {
			System.err.println("SALIDA NO ENCONTRADA.");
			return;
		}
	}

	/****************************************************************/
	public static int checkFile(String mfile, Movements strat[]) {
		File file = new File(mfile);
		Maze maze = new Maze();
		int pathlength = -1;
		if (file == null || maze == null || strat.length != 4) {
			return -1;
		}
		if (maze.read(file) == false) {
			System.err.println("Error en recursiveSolver.");
			return -1;
		}
//		System.out.println("Mapa:\n" + maze.toString());
//		System.out.println("Input:\n" + maze.getInput());
//		System.out.println("Output:\n" + maze.getOutput());
		pathlength = maze.deepSearchStack(maze.getInput(), strat);
//		pathlength = maze.breadthSearchQueue(maze.getInput(), strat);
//		pathlength = maze.pathPaint(maze.deepSearchRec(maze.getInput(), strat, false));
		return pathlength;
	}
}
