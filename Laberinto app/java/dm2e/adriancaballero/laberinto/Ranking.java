package dm2e.adriancaballero.laberinto;

import android.content.Context;

/**
 * @author Adrian Caballeo Orasio
 */
public class Ranking {
    public Integer id;
    public String laberinto;
    public String nombre;
    public Integer puntuacion;

    // public Ranking() { }
    // public Ranking(Integer id) { this.id = id; }

    public Ranking(String nombre, String laberinto, Integer puntuacion) {
        this.nombre = nombre;
        this.laberinto = laberinto;
        this.puntuacion = puntuacion;
    }

    public Ranking(Integer id, String nombre, String laberinto, Integer puntuacion) {
        this.id = id;
        this.nombre = nombre;
        this.laberinto = laberinto;
        this.puntuacion = puntuacion;
    }

    public String toString(Context cont) { return cont.getString(R.string.rankingTxt, this.nombre, this.laberinto, this.puntuacion); }

//    public String toString2(Context cont) { return String.format(cont.getResources().getString(R.string.rankingTxt), this.nombre, this.laberinto, this.puntuacion); }
}
