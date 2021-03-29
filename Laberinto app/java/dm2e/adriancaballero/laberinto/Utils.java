package dm2e.adriancaballero.laberinto;

/**
 * @author Adrian Caballeo Orasio
 */
public class Utils {
    /**
     * Tamaño maximo de un string en C.
     */
    static int MAX = 2048; // 4096

	//4,12,8
	public static String formatC(String txt, char c, int n) {
		if (txt.length() % 2 == 0 && n % 2 == 0) return String.format(String.format("%" + ((n - txt.length()) / 2 + 0) + "s%s%" + -((n - txt.length()) / 2 + 0) + "s", "", "%s", "").replace(' ', c), txt)/*+1*/;
		if (txt.length() % 2 == 0 && n % 2 == 1) return String.format(String.format("%" + ((n - txt.length()) / 2 + 0) + "s%s%" + -((n - txt.length()) / 2 + 1) + "s", "", "%s", "").replace(' ', c), txt)/*+2*/;
		if (txt.length() % 2 == 1 && n % 2 == 0) return String.format(String.format("%" + ((n - txt.length()) / 2 + 1) + "s%s%" + -((n - txt.length()) / 2 + 0) + "s", "", "%s", "").replace(' ', c), txt)/*+3*/;
		if (txt.length() % 2 == 1 && n % 2 == 1) return String.format(String.format("%" + ((n - txt.length()) / 2 + 0) + "s%s%" + -((n - txt.length()) / 2 + 0) + "s", "", "%s", "").replace(' ', c), txt)/*+4*/;
//		return String.format(String.format("%" + ((n - txt.length()) / 2) + "s%s%" + -((n - txt.length()) / 2 + ((n - txt.length() %2==0)?0:1)) + "s", "", "%s", "").replace(' ', c), txt);
		return "zz";
    }

    public static String formatL(String txt, char c, int n) { return String.format(String.format("%" + +(n-txt.length()+2) + "s", "%s").replace(' ', c), txt); }
    public static String formatR(String txt, char c, int n) { return String.format(String.format("%" + -(n-txt.length()+2) + "s", "%s").replace(' ', c), txt); }
}