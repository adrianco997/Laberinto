package dm2e.adriancaballero.laberinto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

/**
 * Created by BunyamiN on 25-5-2015.
 * https://stackoverflow.com/questions/20747138/android-drawing-a-maze-to-canvas-with-smooth-character-movement/
 *
 * @author Adrian Caballeo Orasio
 */
public class MazeView extends androidx.appcompat.widget.AppCompatImageView {
    private final String TAG = getClass().getName();
    private int iniWidth, iniHeight, width, height, cellsX = 5, cellsY = 5;
    private Point actual = null, input = null, output = null;
    private boolean hasGanado = false;
    private Bitmap[] bitmaps;
    private Maze maze;


    public MazeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, (attrs != null) ? attrs.getStyleAttribute() : 0);
        this.bitmaps = new Bitmap[]{
                FileChars.SPACE.getBitmap(context), FileChars.BARRIER.getBitmap(context),
                FileChars.INPUT.getBitmap(context), FileChars.OUTPUT.getBitmap(context),
                FileChars.ERRORCHAR.getBitmap(context),
                Movements.UP.getBitmap(context), Movements.DOWN.getBitmap(context),
                Movements.LEFT.getBitmap(context), Movements.RIGHT.getBitmap(context),
                Movements.VISITED.getBitmap(context), Movements.ACTUAL.getBitmap(context),
        };
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec), hMode = MeasureSpec.getMode(heightMeasureSpec);
        if (wMode == MeasureSpec.EXACTLY) this.width = MeasureSpec.getSize(widthMeasureSpec);
        else if (wMode == MeasureSpec.AT_MOST) this.width = 150;
        if (hMode == MeasureSpec.EXACTLY) this.height = MeasureSpec.getSize(heightMeasureSpec);
        else if (hMode == MeasureSpec.AT_MOST) this.height = 150;
        this.iniWidth = width;
        this.iniHeight = height;
//        Log.i(TAG + ".onMeasure", String.format("(y, x): [%s][%s]\n", yy, xx));Log.i(TAG + ".onMeasure", String.format("(h, w): [%s][%s]\n", height, width));Log.i(TAG + ".onMeasure", String.format("(iY, iX): [%s][%s]\n\n", indentY, indentX));
        setMeasuredDimension(width, height);
    }

    public int addCellsX() {
        if (cellsX < 10) {
            this.cellsX += 1;
        } else {
            this.cellsX = 3;
        }
        invalidate();
        return this.cellsX;
    }

    public int addCellsY() {
        if (cellsY < 10) {
            this.cellsY += 1;
        } else {
            this.cellsY = 3;
        }
        invalidate();
        return this.cellsY;
    }

    public int getCellsX() {
        return this.cellsX;
    }

    public int getCellsY() {
        return this.cellsY;
    }

    public void restart() {
        Maze.map_destroy(this.maze);
        this.maze = null;
        this.input = null;
        this.output = null;
        this.actual = null;
        this.width = this.iniWidth;
        this.height = this.iniHeight;
        this.hasGanado = false;
    }

    public boolean play(@RawRes int idFile) {
        restart();
        this.maze = new Maze(getContext());
        if (!maze.read(idFile)) {
            Log.e(TAG, "Error en play.read");
            return false;
        }
        if (checkFile(idFile) != -1) {
//            muestraMensaje(getResources().getString(R.string.encontrada));
            if (input == null) this.input = this.maze.getInput();
            if (output == null) this.output = this.maze.getOutput();
            if (actual == null) this.actual = this.input;
            Log.i(TAG, "input: " + input + ", output: " + output + ", actual: " + actual);
            return true;
        } else {
            muestraMensaje(getResources().getString(R.string.noEncontrada));
            return false;
        }
    }

    public int checkFile(@RawRes int mfile) {
        Movements[] strat = {Movements.LEFT, Movements.UP, Movements.RIGHT, Movements.DOWN,};
        //InputStream file = getResources().openRawResource(mfile);//File file = new File(mfile);
        Maze mazeAux = new Maze(getContext());
        int pathlength;
        if (!mazeAux.read(mfile)) {
            muestraMensaje("Error en recursiveSolver.");
            return -1;
        }
//		Log.i(TAG,("Mapa:\n" + mazeAux.toString()+"\nInput:  " + mazeAux.getInput()+"\nOutput: " + mazeAux.getOutput());
        pathlength = mazeAux.deepSearchStack(mazeAux.getInput(), strat);
//		pathlength = mazeAux.breadthSearchQueue(mazeAux.getInput(), strat);
//		pathlength = mazeAux.pathPaint(mazeAux.deepSearchRec(mazeAux.getInput(), strat, false));
        Maze.map_destroy(mazeAux);
        return pathlength;
    }

    @Override
    @SuppressLint("DrawAllocation")
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float indentX = (float) width / (2 * cellsX), indentY = (float) height / (2 * cellsY);
        float xx = (actual.getX() - 1) * indentX, yy = (actual.getY() - 1) * indentY;
        float xTemp = (2 * input.getX() - 1) * indentX, yTemp = (2 * input.getY() - 1) * indentY;
        float xMaze = xx + indentX, yMaze = yy + indentY;
//        float xCircle = xx + 2 * xTemp, yCircle = yy + 2 * yTemp;
        float xPlayer = xx + xTemp, yPlayer = yy + yTemp;
        drawMaze(canvas, xMaze, yMaze);
//        drawCircle(canvas, xCircle, yCircle);
        drawPlayer(canvas, xPlayer, yPlayer);
    }

    public void drawMaze(Canvas canvas, float dX, float dY) {
        this.maze.setDrawValues(bitmaps, cellsX, cellsY, width, height);
        Log.i(TAG, "input: " + input + ", output: " + output + ", actual: " + actual);
        this.maze.drawMaze(canvas, dX, dY);
    }

    public void drawPlayer(Canvas canvas, float dX, float dY) {
        RectF drawRect = new RectF();
        drawRect.set(0, 0, (float) width / cellsX, (float) height / cellsY);
        drawRect.offsetTo(dX, dY);
        canvas.drawBitmap(bitmaps[10], null, drawRect, null);
    }

//    public void drawCircle(Canvas canvas, float dX, float dY) { android.graphics.Paint paint = new android.graphics.Paint();paint.setColor(Color.CYAN);paint.setStyle(android.graphics.Paint.Style.FILL);canvas.drawCircle(dX, dY, Math.min(width / cellsX / 2, height / cellsY / 2), paint); }

    public boolean up() {
        if (!hasGanado && actual != null) {
            Point neighbor = maze.getNeighbor(actual, Movements.UP);
            Log.i(TAG, String.format("actual: %s --> neighbor: %s", actual, neighbor));
            if (neighbor != null && !neighbor.isErrorChar() && !neighbor.isBarrier()) {
                this.actual = neighbor;
                onGanar();
                invalidate();
                return true;
            }
            onGanar();
        }
        invalidate();
        return false;
    }

    public boolean down() {
        if (!hasGanado && actual != null) {
            Point neighbor = maze.getNeighbor(actual, Movements.DOWN);
            Log.i(TAG, String.format("actual: %s --> neighbor: %s", actual, neighbor));
            if (neighbor != null && !neighbor.isErrorChar() && !neighbor.isBarrier()) {
                this.actual = neighbor;
                onGanar();
                invalidate();
                return true;
            }
            onGanar();
        }
        invalidate();
        return false;
    }

    public boolean left() {
        if (!hasGanado && actual != null) {
            Point neighbor = maze.getNeighbor(actual, Movements.LEFT);
            Log.i(TAG, String.format("actual: %s --> neighbor: %s", actual, neighbor));
            if (neighbor != null && !neighbor.isErrorChar() && !neighbor.isBarrier()) {
                this.actual = neighbor;
                onGanar();
                invalidate();
                return true;
            }
            onGanar();
        }
        invalidate();
        return false;
    }

    public boolean right() {
        if (!hasGanado && actual != null) {
            Point neighbor = maze.getNeighbor(actual, Movements.RIGHT);
            Log.i(TAG, String.format("actual: %s --> neighbor: %s", actual, neighbor));
            if (neighbor != null && !neighbor.isErrorChar() && !neighbor.isBarrier()) {
                this.actual = neighbor;
                onGanar();
                invalidate();
                return true;
            }
            onGanar();
        }
        invalidate();
        return false;
    }

    public void onGanar() {
        if (actual.equals(output)) this.hasGanado = true;
    }

    public boolean hasGanado() {
        return this.hasGanado;
    }

    protected void muestraMensaje(String txt) {
        Toast.makeText(getContext(), txt, Toast.LENGTH_SHORT).show();
    }
//    protected void muestraMensaje(int resId) { Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show(); }
}
