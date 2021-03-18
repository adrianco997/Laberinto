package dm2e.adriancaballero.laberinto;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

/**
 * @author Adrian Caballeo Orasio
 */
public class MazePlayerDrawActivity extends Activity {
    private final String TAG = getClass().getName();
    private String laberintoType = "error", nombreJugador = "";
    private MazeView mazeViewer;
    private int moves = 0;
    private boolean hasGanado = false;
    private TextView tvContador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mazeplayer_draw);
        mazeViewer = findViewById(R.id.MazeView);
        mazeViewer.setScaleType(ImageView.ScaleType.MATRIX);

        this.nombreJugador = getIntent().getStringExtra("nombre");
        this.laberintoType = getIntent().getStringExtra("laberinto");

        TextView tv1 = findViewById(R.id.tv1);
        this.tvContador = findViewById(R.id.tvContador);
        tv1.setText(String.format(getResources().getString(R.string.ordenaLaberinto), this.nombreJugador));
        this.tvContador.setText(String.format(getResources().getString(R.string.movimientos), this.moves));

        findViewById(R.id.up).setOnClickListener(new DpadClick());
        findViewById(R.id.down).setOnClickListener(new DpadClick());
        findViewById(R.id.left).setOnClickListener(new DpadClick());
        findViewById(R.id.right).setOnClickListener(new DpadClick());

        //musica(AUDIO_PATH_RAW1);
        if (!this.mazeViewer.play(getResourceId(this.laberintoType, "raw"))) {
            onFinish(null);
        }
    }

    //public void main(int[] args) { if (args.length == 0) { main(new int[]{R.raw.m1});/*main(new int[] { R.raw.m2. });*//*main(new int[] { R.raw.m3. });*//*main(new int[] { R.raw.m4. });*/return; }/*ejsOld(args[0], 0);*/play(args[0]); }
    //public void ejsOld(int mfile, int i) { String texto = "SALIDA ENCONTRADA, longitud del camino con %s y estrategia %s, %s, %s, %s: %d unidades.\n", tipo = "algoritmo recursivo";System.out.println("-->Recursivo:\n");MazeSolver ms = new MazeSolver();int pathlength = ms.recursiveSolver(this, mfile, strat1, true);if (pathlength != -1) { System.out.printf(texto, tipo, strat[i][0], strat[i][1], strat[i][2], strat[i][3], pathlength); } else { System.err.println("SALIDA NO ENCONTRADA."); }ms.runSolver(this, mfile, strat); }
    ///**llama a stackSolver, queueSolver y recursiveSolver con una matriz de movimientos*/public void runSolver(Context contexto, int mfile, Movements[][] strat) { for (Movements[] movements : strat) { if (movements.length != 4) { System.err.println("Numero de movimientos incorrecto.");return; }System.out.printf("ESTRATEGIA %s, %s, %s, %s\n", movements[0], movements[1], movements[2], movements[3]);struct(contexto, "pila", mfile, movements, true, 2);struct(contexto, "cola", mfile, movements, true, 2);struct(contexto, "algoritmo recursivo", mfile, movements, true, 3); } }
    //public void struct(Context contexto, String solver, int mfile, Movements[] strat, boolean compare, int type) { String texto = "SALIDA ENCONTRADA, longitud del camino con %s y estrategia %s, %s, %s, %s: %d unidades.\n";System.out.println(Utils.formatR("Con " + solver + ":", '-', 108));int pathlength = -1;if (type == 1) { pathlength = stackSolver(contexto, mfile, strat); } else if (type == 2) { pathlength = queueSolver(contexto, mfile, strat); } else if (type == 3) { pathlength = recursiveSolver(contexto, mfile, strat, compare); } //else { }if (pathlength != -1) { System.out.printf(texto, solver, strat[0], strat[1], strat[2], strat[3], pathlength); } else { System.err.println("SALIDA NO ENCONTRADA."); }System.out.println(Utils.formatL("", '-', 108)); }
    ///**resolve un mapa usando una pila.*/public int stackSolver(Context contexto, int mfile, Movements[] strat) { Maze maze = new Maze();if (strat.length != 4) { return -1; }if (!maze.read(contexto, mfile)) { System.err.println("Error en recursiveSolver.");return -1; }System.out.println("Maze:\n" + maze.toString());System.out.println("Input:\n" + maze.getInput().print());int pathlength;pathlength = maze.deepSearchStack(maze.getInput(), strat);System.out.println(((pathlength != -1) ? "Output found, length of path: " + pathlength : "No path found."));System.out.println(maze.toString());System.out.printf("Final maze: %s%n", maze.pathPaint(maze.getOutput()));return pathlength; }
    ///**resolve un mapa usando una cola.*/public int queueSolver(Context contexto, int mfile, Movements[] strat) { Maze maze = new Maze();if (strat.length != 4) { return -1; }if (!maze.read(contexto, mfile)) { System.err.println("Error en recursiveSolver.");return -1; }System.out.println("Maze:\n" + maze.toString());System.out.println("Input:\n" + maze.getInput().print());int pathlength;pathlength = maze.breadthSearchQueue(maze.getInput(), strat);System.out.println(((pathlength != -1) ? "Output found, length of path: " + pathlength : "No path found."));System.out.println(maze.toString());System.out.printf("Final maze: %s%n", maze.pathPaint(maze.getOutput()));return pathlength; }
    ///**resolve un mapa usando un algoritmo recursivo.*/public int recursiveSolver(Context contexto, int mfile, Movements[] strat, boolean compare) { Maze maze = new Maze();if (strat.length != 4) { return -1; }if (!maze.read(contexto, mfile)) { System.err.println("Error en recursiveSolver.");return -1; }System.out.println("Maze:\n" + maze.toString() + "\n" + "Input:\n" + maze.getInput().print());int pathlength;Point out = maze.deepSearchRec(maze.getInput(), strat, compare);if (out != null) { pathlength = maze.pathPaint(out);System.out.printf("Output is in point: %s%n", out.toString()); } else { pathlength = -1; }System.out.println(((pathlength != -1) ? "Output found, length of path: " + pathlength : "No path found."));System.out.println(maze.toString());System.out.printf("Final maze: %s%n", maze.pathPaint(maze.getOutput()));return pathlength; }

    private class DpadClick implements View.OnClickListener {
        public void onClick(View v) {
            if (!hasGanado) {
                boolean hayMovimiento = false;
                switch (v.getId()) {
                    case R.id.up: {
                        hayMovimiento = mazeViewer.up();
                        break;
                    }
                    case R.id.down: {
                        hayMovimiento = mazeViewer.down();
                        break;
                    }
                    case R.id.left: {
                        hayMovimiento = mazeViewer.left();
                        break;
                    }
                    case R.id.right: {
                        hayMovimiento = mazeViewer.right();
                        break;
                    }
                }
                if (hayMovimiento) {
                    moves++;
                    tvContador.setText(String.format(getResources().getString(R.string.movimientos), moves));
                }
                if (mazeViewer.hasGanado()) {
//                hasGanado = true;

                    tvContador.setText(String.format(getResources().getString(R.string.movimientos), moves));
                    muestraMensaje(String.format(getResources().getString(R.string.ganado), moves));
                    onGanar();
                }
            } else {
                onFinish(v);
            }
        }
    }

    public int getResourceId(String name, String defType) {
        return getResources().getIdentifier(name, defType, getPackageName());
    }

    protected void onGanar() {
        long id = 0;
        this.hasGanado = true;
        onPause();
        try {
            SQLiteHelper_Ranking db = SQLiteHelper_Ranking.getInstance(this);
            Ranking ranking = new Ranking(this.nombreJugador, this.laberintoType, this.moves);
            id = db.addOrUpdateRanking(ranking);/*db.addRanking(ranking);*/
//            actualizarBD();
        } catch (Exception e) {
            Log.e(TAG, "(" + id + ")" + e);
        }
//        actualizarBD();
    }

    //    protected void actualizarBD() { String txt = "Rankings:\n";try { SQLiteHelper_Ranking db = SQLiteHelper_Ranking.getInstance(this);List<Ranking> ranking = db.getAllRankings();for (Ranking r : ranking) { txt = String.format("%s%s\n", txt, r.toString(this)); }((TextView) findViewById(R.id.tvGanardores)).setText(txt); } catch (Exception e) { muestraMensaje(getResources().getString(R.string.errorGanardores)); } }
    public void onFinish(View v) {
        onPause();/*setResult(RESULT_OK, new Intent().putExtra("respuesta", String.format(getResources().getString(R.string.ganado), movimientos)));*/
        finish();
    }

    public void musica(int music) {
        try {
            Musica.musicaRaw(this, music);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        if (Musica.getMp() != null && Musica.getMp().isPlaying()) {
            Musica.onPause();
            Musica.retirar();
        }
        super.onPause();
    }

    protected void muestraMensaje(String txt) {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
