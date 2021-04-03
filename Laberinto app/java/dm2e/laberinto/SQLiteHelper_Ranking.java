package dm2e.laberinto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adrian Caballeo Orasio
 */
public class SQLiteHelper_Ranking extends SQLiteOpenHelper {
    private static final String TAG = "Error en ranking. ";
    // Database Info
    private static final String DATABASE_NAME = "DBRanking";
    private static final int DATABASE_VERSION = 1;
    // Table Name and Columns
    private static final String TABLA = "Ranking", key_id = "id", key_nombre = "nombre", key_laberinto = "laberinto", key_puntuacion = "puntuacion";
    // SQLite Functions
    private static final String sqlCreate = "CREATE TABLE " + TABLA
            + " (" + key_id + " INTEGER " + " PRIMARY KEY"
            + ", " + key_nombre + " TEXT " //+ "NOT NULL"
            + ", " + key_laberinto + " TEXT " //+ "NOT NULL"
            + ", " + key_puntuacion + " NUMERIC " //+ "NOT NULL"
            + ")", //
            sqlDrop = "DROP TABLE IF EXISTS " + TABLA;
    // Instance
    private static SQLiteHelper_Ranking instance;

    /**********************************************************************************************/
    public static synchronized SQLiteHelper_Ranking getInstance(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version) { instance = (instance == null) ? new SQLiteHelper_Ranking(contexto.getApplicationContext(), nombre, factory, version) : instance;return instance; }
    public static synchronized SQLiteHelper_Ranking getInstance(Context contexto) { instance = (instance == null) ? new SQLiteHelper_Ranking(contexto.getApplicationContext()) : instance;return instance; }

    private SQLiteHelper_Ranking(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version) { super(contexto, nombre, factory, version); }
    private SQLiteHelper_Ranking(Context contexto) { super(contexto, DATABASE_NAME, null, DATABASE_VERSION); }

    @Override public void onConfigure(SQLiteDatabase db) { super.onConfigure(db);db.setForeignKeyConstraintsEnabled(true); }
    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }
    @Override public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) { if (versionAnterior != versionNueva) { db.execSQL(sqlDrop);this.onCreate(db); } }
    /**********************************************************************************************/
    public long addRanking(Ranking ranking) {
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(key_id, ranking.id);
            values.put(key_nombre, ranking.nombre);
            values.put(key_laberinto, ranking.laberinto);
            values.put(key_puntuacion, ranking.puntuacion);
            id = db.insertOrThrow(TABLA, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) { Log.d(TAG, "Error while trying to add ranking to database"); } finally { db.endTransaction(); }
        return id;
    }

    public long addOrUpdateRanking(Ranking ranking) {
        Ranking r = getRanking(ranking);
        long id = -1;
        if (r == null) { id = addRanking(ranking); } else if (r.puntuacion > ranking.puntuacion) { id = updateRankingPuntuacion(ranking); }
        return id;
    }

    public int updateRankingPuntuacion(Ranking ranking) {
        ContentValues values = new ContentValues();
        values.put(key_puntuacion, ranking.puntuacion);
        return this.getWritableDatabase().update(TABLA, values, String.format("%s = ? AND %s = ? AND %s >= ?", key_nombre, key_laberinto, key_puntuacion), new String[]{ranking.nombre, ranking.laberinto, String.valueOf(ranking.puntuacion)});
    }

    public Ranking getRanking(int id) {
        Cursor c = this.getReadableDatabase().rawQuery(String.format("SELECT * FROM %s WHERE %s = ? ", TABLA, key_id), new String[]{String.valueOf(id)});
        Ranking r = null;
        try {
            if (c.getCount() == 1 && c.moveToFirst()) {
                r = new Ranking(c.getInt(c.getColumnIndex(key_id)), c.getString(c.getColumnIndex(key_nombre)), c.getString(c.getColumnIndex(key_laberinto)), c.getInt(c.getColumnIndex(key_puntuacion)));
            } else throw new Exception();
        } catch (Exception e) { Log.d(TAG, "Error while trying to get ranking from database"); } finally { if (c != null && !c.isClosed()) { c.close(); } }
        return r;
    }
    public Ranking getRanking(Ranking ranking) {
        Cursor c = this.getReadableDatabase().rawQuery(String.format("SELECT * FROM %s WHERE %s = ? AND %s = ?", TABLA, key_nombre, key_laberinto), new String[]{ranking.nombre, ranking.laberinto});
        Ranking r = null;
        try {
            if (c.getCount() == 1 && c.moveToFirst()) {
                r = new Ranking(c.getInt(c.getColumnIndex(key_id)), c.getString(c.getColumnIndex(key_nombre)), c.getString(c.getColumnIndex(key_laberinto)), c.getInt(c.getColumnIndex(key_puntuacion)));
            } else throw new Exception();
        } catch (Exception e) { Log.d(TAG, "Error while trying to get ranking from database"); } finally { if (c != null && !c.isClosed()) { c.close(); } }
        return r;
    }
    public List<Ranking> getAllRankings() {
        Cursor c = this.getReadableDatabase().rawQuery(String.format("SELECT * FROM %s ORDER BY %s, %s", TABLA, key_laberinto, key_puntuacion), null);
        List<Ranking> rankings = new ArrayList<>();
        try {
            if (c.moveToFirst()) {
                do {
                    rankings.add(new Ranking(c.getInt(c.getColumnIndex(key_id)), c.getString(c.getColumnIndex(key_nombre)), c.getString(c.getColumnIndex(key_laberinto)), c.getInt(c.getColumnIndex(key_puntuacion))));
                } while (c.moveToNext());
            }
        } catch (Exception e) { Log.d(TAG, "Error while trying to get rankings from database"); } finally { if (c != null && !c.isClosed()) { c.close(); } }
        return rankings;
    }

    public void deleteRanking(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(TABLA, key_id + " = ?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } catch (Exception e) { Log.d(TAG, "Error while trying to delete Ranking"); } finally { db.endTransaction(); }
    }
    public void deleteRanking(Ranking ranking) {
        deleteRanking(ranking.id);
    }
    public void deleteAllRankings() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(TABLA, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) { Log.d(TAG, "Error while trying to delete all Rankings"); } finally { db.endTransaction(); }
    }
}