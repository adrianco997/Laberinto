package dm2e.laberinto;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Adrian Caballeo Orasio
 */
public class MazePlayerDrawActivity extends /*android.app.Activity*/AppCompatActivity {
    private final String TAG = getClass().getName();
    private String laberintoType = "error", nombreJugador = "";
    //static final int AUDIO_PATH_RAW1 = R.raw.;
    private Point actual = null, input = null, output = null;
    private MazeView mazeViewer = null;
    private TextView tvContador = null;
    private boolean hasGanado = false;
    private Maze maze = null;
    private int moves = 0;
    private int /*iniWidth, iniHeight,*/ cellsX = 5, cellsY = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mazeplayer_draw);
        this.mazeViewer = findViewById(R.id.MazeView);
        this.mazeViewer.setScaleType(ImageView.ScaleType.MATRIX);
        if (getIntent() != null && getIntent().getStringExtra("nombre") != null) {
            if (!getIntent().getStringExtra("nombre").equals(""))
                this.nombreJugador = getIntent().getStringExtra("nombre");
        } else this.nombreJugador = "jugador";
        if (getIntent() != null && getIntent().getStringExtra("laberinto") != null) {
            if (!getIntent().getStringExtra("laberinto").equals(""))
                this.laberintoType = getIntent().getStringExtra("laberinto");
        } else this.laberintoType = "m5";
        this.tvContador = findViewById(R.id.tvContador);
        TextView tv1 = findViewById(R.id.tv1);
        tv1.setText(String.format(getResources().getString(R.string.ordenaLaberinto), this.nombreJugador));

        findViewById(R.id.up).setOnClickListener(new DpadDrawClick());
        findViewById(R.id.down).setOnClickListener(new DpadDrawClick());
        findViewById(R.id.left).setOnClickListener(new DpadDrawClick());
        findViewById(R.id.right).setOnClickListener(new DpadDrawClick());

        //musica(AUDIO_PATH_RAW1);
        iniPlay();
    }

    public void iniPlay() {
        //if (!this.mazeViewer.play(getResourceId(this.laberintoType, "raw"))) onFinish(null);
        if (!play(getResourceId(this.laberintoType, "raw"))) onFinish(null);
        this.moves = 0;
        this.hasGanado = false;
        this.tvContador.setText(String.format(getResources().getString(R.string.movimientos), this.moves));
    }

    public void restart() {
        Maze.map_destroy(this.maze);
        this.maze = null;
        this.input = null;
        this.output = null;
        this.actual = null;
        this.hasGanado = false;/*this.width = this.iniWidth;this.height = this.iniHeight;*/
    }

    public boolean play(@RawRes int idFile) {
        restart();
        this.mazeViewer.restart();
        this.maze = new Maze(this);
        if (!this.maze.read(idFile)) {
            Log.e(TAG, "Error en play.read");
            return false;
        }
        if (checkFile(idFile) != -1) {
//            muestraMensaje(getResources().getString(R.string.encontrada));
            if (this.input == null) this.input = maze.getInput();
            if (this.output == null) this.output = maze.getOutput();
            if (this.actual == null) this.actual = this.input;
            Log.i(TAG, "input: " + this.input + ", output: " + this.output + ", actual: " + this.actual);
            printMaze();
            this.mazeViewer.setCellsX(this.cellsX);
            this.mazeViewer.setCellsY(this.cellsY);
            return true;
        } else {
            muestraMensaje(getResources().getString(R.string.noEncontrada));
            return false;
        }
    }

    public int checkFile(@RawRes int mfile) {
        Movements[] strat = {Movements.LEFT, Movements.UP, Movements.RIGHT, Movements.DOWN,};
        //InputStream file = getResources().openRawResource(mfile);//File file = new File(mfile);
        Maze mazeAux = new Maze(this);
        int pathlength;
        if (!mazeAux.read(mfile)) {
            muestraMensaje("Error en recursiveSolver.");
            return -1;
        }
//		Log.i(TAG,("Mapa:\n" + mazeAux.toString()+"\nInput:  " + mazeAux.getInput()+"\nOutput: " + mazeAux.getOutput());
        pathlength = mazeAux.deepSearchStack(mazeAux.getInput(), strat);
//		pathlength = mazeAux.breadthSearchQueue(mazeAux.getInput(), strat);
//		pathlength = mazeAux.pathPaint(mazeAux.deepSearchRec(mazeAux.getInput(), strat, false))
        Maze.map_destroy(mazeAux);
        return pathlength;
    }

    /***********************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mazeplayer_draw, menu);
        MenuItem x = menu.getItem(0).getSubMenu().getItem(0);
        MenuItem y = menu.getItem(0).getSubMenu().getItem(1);
        x.setTitle(getString(R.string.x_size, mazeViewer.getCellsX()));
        y.setTitle(getString(R.string.y_size, mazeViewer.getCellsY()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.x_size: {
                item.setTitle(getString(R.string.x_size, mazeViewer.addCellsX()));
                printMaze();
                return true;
            }
            case R.id.y_size: {
                item.setTitle(getString(R.string.y_size, mazeViewer.addCellsY()));
                printMaze();
                return true;
            }
            case R.id.restart: {
                muestraMensaje("restart");
                iniPlay();
                return true;
            }
            case R.id.help: {
                //muestraMensaje("help, por hacer");
                final View v = findViewById(R.id.action_settings);/*item.getActionView()*/
                openPopup(v, R.id.help);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    public void openPopup(View view) {
        openPopup(view, view.getId());
    }

    public void openPopup(View view, /*@IdRes*/ int idPopup) {
        if (view == null) {
            muestraMensaje("Error al abrir poup");
        }
        int width = LinearLayout.LayoutParams.WRAP_CONTENT, height = LinearLayout.LayoutParams.WRAP_CONTENT;
        LayoutInflater lInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popup_window, null, false), 100, 100, true);

        View popupView = lInflater.inflate(R.layout.popup_window, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        TextView tv = (TextView) popupView.findViewById(R.id.tv_popup);
        View accept = popupView.findViewById(R.id.accept_popup);
        View cancel = popupView.findViewById(R.id.cancel_popup);

        switch (idPopup) {
            case R.id.help: {
                tv.setText(getSpannedText(getString(R.string.popup_help)));
                tv.setGravity(Gravity.NO_GRAVITY);
                accept.setVisibility(View.GONE);
                cancel.setVisibility(View.VISIBLE);
                break;
            }
        }

//        if(accept.getVisibility() == View.VISIBLE) {accept.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { }});}
        if (cancel.getVisibility() == View.VISIBLE) {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        } else {
            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow.dismiss();
                    return true;
                }
            });
        }
    }

    private Spanned getSpannedText(String text) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return Html.fromHtml(text);
        else return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
    }//public static CharSequence getText(Context context, int id, Object... args) { for (int i = 0; i < args.length; ++i) args[i] = (args[i] instanceof String) ? TextUtils.htmlEncode((String) args[i]) : args[i];return Html.fromHtml(String.format(Html.toHtml(new SpannedString(context.getText(id))), args)); }

    /***********************************************************************************************/
    //public void main(int[] args) { if (args.length == 0) { main(new int[]{R.raw.m1});/*main(new int[] { R.raw.m2. });*//*main(new int[] { R.raw.m3. });*//*main(new int[] { R.raw.m4. });*/return; }/*ejsOld(args[0], 0);*/play(args[0]); }
    //public void ejsOld(int mfile, int i) { String texto = "SALIDA ENCONTRADA, longitud del camino con %s y estrategia %s, %s, %s, %s: %d unidades.\n", tipo = "algoritmo recursivo";System.out.println("-->Recursivo:\n");MazeSolver ms = new MazeSolver();int pathlength = ms.recursiveSolver(this, mfile, strat1, true);if (pathlength != -1) { System.out.printf(texto, tipo, strat[i][0], strat[i][1], strat[i][2], strat[i][3], pathlength); } else { System.err.println("SALIDA NO ENCONTRADA."); }ms.runSolver(this, mfile, strat); }
    ///**llama a stackSolver, queueSolver y recursiveSolver con una matriz de movimientos*/public void runSolver(Context contexto, int mfile, Movements[][] strat) { for (Movements[] movements : strat) { if (movements.length != 4) { System.err.println("Numero de movimientos incorrecto.");return; }System.out.printf("ESTRATEGIA %s, %s, %s, %s\n", movements[0], movements[1], movements[2], movements[3]);struct(contexto, "pila", mfile, movements, true, 2);struct(contexto, "cola", mfile, movements, true, 2);struct(contexto, "algoritmo recursivo", mfile, movements, true, 3); } }
    //public void struct(Context contexto, String solver, int mfile, Movements[] strat, boolean compare, int type) { String texto = "SALIDA ENCONTRADA, longitud del camino con %s y estrategia %s, %s, %s, %s: %d unidades.\n";System.out.println(Utils.formatR("Con " + solver + ":", '-', 108));int pathlength = -1;if (type == 1) { pathlength = stackSolver(contexto, mfile, strat); } else if (type == 2) { pathlength = queueSolver(contexto, mfile, strat); } else if (type == 3) { pathlength = recursiveSolver(contexto, mfile, strat, compare); } //else { }if (pathlength != -1) { System.out.printf(texto, solver, strat[0], strat[1], strat[2], strat[3], pathlength); } else { System.err.println("SALIDA NO ENCONTRADA."); }System.out.println(Utils.formatL("", '-', 108)); }
    ///**resolve un mapa usando una pila.*/public int stackSolver(Context contexto, int mfile, Movements[] strat) { Maze maze = new Maze();if (strat.length != 4) { return -1; }if (!maze.read(contexto, mfile)) { System.err.println("Error en recursiveSolver.");return -1; }System.out.println("Maze:\n" + maze.toString());System.out.println("Input:\n" + maze.getInput().print());int pathlength;pathlength = maze.deepSearchStack(maze.getInput(), strat);System.out.println(((pathlength != -1) ? "Output found, length of path: " + pathlength : "No path found."));System.out.println(maze.toString());System.out.printf("Final maze: %s%n", maze.pathPaint(maze.getOutput()));return pathlength; }
    ///**resolve un mapa usando una cola.*/public int queueSolver(Context contexto, int mfile, Movements[] strat) { Maze maze = new Maze();if (strat.length != 4) { return -1; }if (!maze.read(contexto, mfile)) { System.err.println("Error en recursiveSolver.");return -1; }System.out.println("Maze:\n" + maze.toString());System.out.println("Input:\n" + maze.getInput().print());int pathlength;pathlength = maze.breadthSearchQueue(maze.getInput(), strat);System.out.println(((pathlength != -1) ? "Output found, length of path: " + pathlength : "No path found."));System.out.println(maze.toString());System.out.printf("Final maze: %s%n", maze.pathPaint(maze.getOutput()));return pathlength; }
    ///**resolve un mapa usando un algoritmo recursivo.*/public int recursiveSolver(Context contexto, int mfile, Movements[] strat, boolean compare) { Maze maze = new Maze();if (strat.length != 4) { return -1; }if (!maze.read(contexto, mfile)) { System.err.println("Error en recursiveSolver.");return -1; }System.out.println("Maze:\n" + maze.toString() + "\n" + "Input:\n" + maze.getInput().print());int pathlength;Point out = maze.deepSearchRec(maze.getInput(), strat, compare);if (out != null) { pathlength = maze.pathPaint(out);System.out.printf("Output is in point: %s%n", out.toString()); } else { pathlength = -1; }System.out.println(((pathlength != -1) ? "Output found, length of path: " + pathlength : "No path found."));System.out.println(maze.toString());System.out.printf("Final maze: %s%n", maze.pathPaint(maze.getOutput()));return pathlength; }

    private class DpadDrawClick implements View.OnClickListener {
        public void onClick(View v) {
            if (!hasGanado && actual != null) {
                Point neighbor = null;
                switch (v.getId()) {
                    case R.id.up: {
                        neighbor = maze.getNeighbor(actual, Movements.UP);
                        break;
                    }
                    case R.id.down: {
                        neighbor = maze.getNeighbor(actual, Movements.DOWN);
                        break;
                    }
                    case R.id.left: {
                        neighbor = maze.getNeighbor(actual, Movements.LEFT);
                        break;
                    }
                    case R.id.right: {
                        neighbor = maze.getNeighbor(actual, Movements.RIGHT);
                        break;
                    }
//                    default: { throw new IllegalStateException("Unexpected value: " + v.getId()); }
                }
                if (neighbor != null && !neighbor.isErrorChar() && !neighbor.isBarrier()) {
                    actual = neighbor;
                    moves++;
                    tvContador.setText(String.format(getResources().getString(R.string.movimientos), moves));
                }
                printMaze();
                if (actual.equals(output)) {
                    tvContador.setText(String.format(getResources().getString(R.string.movimientos), moves));
                    muestraMensaje(String.format(getResources().getString(R.string.ganado), moves));
                    onGanar();
                }
            } else {
                onFinish(v);
            }
        }
    }

    public void printMaze() {
        this.mazeViewer.setActual(actual);
        this.mazeViewer.setMaze(this.maze);
        this.mazeViewer.invalidate();
        //this.mazeViewer.drawMaze( this.maze, this.actual);
    }

    /****************************************************************/
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
