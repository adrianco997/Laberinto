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
public /* static */ enum Movements {
    UP('^', R.string.UP, R.drawable.img_up),
    DOWN('v', R.string.DOWN, R.drawable.img_donw),
    LEFT('<', R.string.LEFT, R.drawable.img_left),
    RIGHT('>', R.string.RIGHT, R.drawable.img_right),
    /**
     * Simbolo para un espacio ya visitado.</br>
     */
    VISITED('x', R.string.VISITED, 0/*R.drawable.visited*/),
    /**
     * Simbolo para un espacio actualmente ocupado.</br>
     */
    ACTUAL('P', R.string.ACTUAL, R.drawable.xml_actual),
    ;
    public final char c;
    @StringRes public final int label;
    @DrawableRes public final int image;
    Movements(char c, @StringRes int label, @DrawableRes int image) { this.c = c;this.label = label;this.image = image; }
    public Bitmap getBitmap(Context contexto) { return BitmapFactory.decodeResource(contexto.getResources(), this.image); }
    public char getChar(Context contexto) { return contexto.getResources().getString(label).charAt(0); }
    public char getLabelChar(Context contexto) { Resources res = contexto.getResources();int resId = res.getIdentifier(this.name(), "string", contexto.getPackageName());if (0 != resId) return (res.getString(resId).charAt(0));else return this.getChar(contexto); }
    public String getLabel(Context contexto) { Resources res = contexto.getResources();int resId = res.getIdentifier(this.name(), "string", contexto.getPackageName());if (0 != resId) return (res.getString(resId));else return this.toString(contexto); }
    public String toString(Context contexto) { return contexto.getResources().getString(label); }
    public static Movements valueOf(char c) { for (Movements m : values()) if (m.c == c) return m;return null; }
    public static Movements valueOf(int id) { for (Movements m : values()) if (m.label == id || m.image == id) return m;return null; }
//    public char toLocalizedChar(Context contexto) { if (id != 0) return (contexto.getResources().getString(id).charAt(0));/*return (this.toString());*/return FileChars.ERRORCHAR.c; }
//    public char toLocalizedChar(CustomView contexto) { return toLocalizedChar(contexto.getContext()); }
//    Resources res = getResources(); MyEnum e = MyEnum.VALUE1; String localized = res.getString(res.getIdentifier(e.name(), "string", getPackageName()));
}
// typedef enum {RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3, STAY = 4} Move;

