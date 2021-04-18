package dm2e.laberinto;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

/**
 * Constantes publicas que definen los tipos de puntos que se permiten en un laberinto.</br>
 * Simbolos de entrada de fichero
 *
 * @author Adrian Caballeo Orasio
 */
public /* static */ enum FileChars {
    // new MazeSolver().getApplicationContext().getResources().getString(R.string.SPACE).toCharArray()[0]
    SPACE(' ', R.string.SPACE, R.drawable.img_space),
    BARRIER('+', R.string.BARRIER, R.drawable.xml_barrier),
    INPUT('i', R.string.INPUT, R.drawable.img_input),
    OUTPUT('o', R.string.OUTPUT, R.drawable.xml_output),
    ERRORCHAR('E', R.string.ERRORCHAR, R.drawable.xml_errorchar),;
    public final char c;public final @StringRes int label;public final @DrawableRes int image;
    FileChars(char c, @StringRes int label, @DrawableRes int image) { this.c = c;this.label = label;this.image = image; }
    public Bitmap getBitmap(Context contexto){ return BitmapFactory.decodeResource(contexto.getResources(), this.image); }
    public char getChar(Context contexto) { return contexto.getResources().getString(label).charAt(0); }
    public char getLabelChar(Context contexto) { Resources res = contexto.getResources();int resId = res.getIdentifier(this.name(), "string", contexto.getPackageName());if (0 != resId) return (res.getString(resId).charAt(0));else return this.getChar(contexto); }
    public String toString(Context contexto) { return contexto.getResources().getString(label); }
    public String getLabel(Context contexto) { Resources res = contexto.getResources();int resId = res.getIdentifier(this.name(), "string", contexto.getPackageName());if (0 != resId) return (res.getString(resId));else return this.toString(contexto); }
    //    public Bitmap getBitmap() { return this.getBitmap(new MainActivity().getApplicationContext()); }
    //    public char getChar() { return this.getChar(new MainActivity().getApplicationContext()); }
    //    public char getLabelChar() { return this.getLabelChar(new MainActivity().getApplicationContext()); }
    //    @Override public String toString() { return this.toString(new MainActivity().getApplicationContext()); }
    //    public String getLabel() { return this.getLabel(new MainActivity().getApplicationContext()); }
    public static FileChars valueOf(char c) { for (FileChars m : values()) if (m.c == c) return m;return null; }
    public static FileChars valueOf(int id) { for (FileChars m : values()) if (m.label == id) return m;else if (m.image == id) return m;return null; }
//    public char toLocalizedChar(Context contexto) { if (id != 0) return (contexto.getResources().getString(id).charAt(0));/*return (this.toString());*/return FileChars.ERRORCHAR.c; }
//    public char toLocalizedChar(CustomView contexto) { return toLocalizedChar(contexto.getContext()); }
//    Resources res = getResources(); MyEnum e = MyEnum.VALUE1; String localized = res.getString(res.getIdentifier(e.name(), "string", getPackageName()));
}
// typedef enum {RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3, STAY = 4} Move;