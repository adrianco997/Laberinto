package laberinto;
public class Utils {
	/**
	 * Tamaño maximo de un string en C.
	 */
	static int MAX = 4096;
	public static String formatC(String txt, char c, int n) {return String.format(String.format("%" + (n - txt.length()) / 2 + "s%" + -(n - txt.length()) / 2 + "s", "%s", "").replace(' ', c),txt);}
	public static String formatL(String txt, char c, int n) {return String.format(String.format("%" + (n - txt.length()) + "s", "%s").replace(' ', c), txt);}
	public static String formatR(String txt, char c, int n) {return String.format(String.format("%" + -(n - txt.length()) + "s", "%s").replace(' ', c), txt);}
}