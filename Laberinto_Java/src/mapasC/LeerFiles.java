package mapasC;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LeerFiles {
	static String file = "m3.txt";

	public static void main(String[] args) {
		byte[] datos = null;
		List<String> lineas;
//		try {
//			System.out.println("try1");
//			/* lectura como bytes, no como caracteres */
//			datos = Files.readAllBytes(Paths.get(file));
//
//			for (int i = 0; i < datos.length; i++) {
//				System.out.println(datos[i]);
//			}
//		} catch (IOException e) {
//			e.getMessage();
//		}
		try {
			System.out.println("try2");
			/* charset habitual con .txt */
			lineas = Files.readAllLines(Paths.get(file), Charset.forName("ISO-8859-1"));
			for (int i = 0; i < lineas.size(); i++) {
				System.out.println(lineas.get(i));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//		try {
//			System.out.println("try3");
//			/* para ficheros XML el charset es UTF-8 */
//			lineas = Files.readAllLines(Paths.get(file), Charset.forName("UTF-8"));
//			for (int i = 0; i < lineas.size(); i++) {
//				System.out.println(lineas.get(i));
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
