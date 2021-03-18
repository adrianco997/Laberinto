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
    private Point actual = null, input = null, output = null;
    private int width, height, cellsX = 5, cellsY = 5;
    private float xx, yy, indentX, indentY;
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

//    public void setCells(int cx, int cy) { if (cx >= 5 && cx <= 15)this.cellsX = cx;else setCells(cx - 15, cy);if (cy >= 5 && cy <= 15) this.cellsY = cy;else setCells(cx, cy - 15); }

    public void setIndentXY() {
        this.indentX = (float) this.width / this.cellsX / 2;
        this.indentY = (float) this.height / this.cellsY / 2;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec), hMode = MeasureSpec.getMode(heightMeasureSpec);
        if (wMode == MeasureSpec.EXACTLY) width = MeasureSpec.getSize(widthMeasureSpec);
        else if (wMode == MeasureSpec.AT_MOST) width = 150;
        if (hMode == MeasureSpec.EXACTLY) height = MeasureSpec.getSize(heightMeasureSpec);
        else if (hMode == MeasureSpec.AT_MOST) height = 150;
        setIndentXY();
//        Log.i(TAG + ".onMeasure", String.format("(y, x): [%s][%s]\n", yy, xx));Log.i(TAG + ".onMeasure", String.format("(h, w): [%s][%s]\n", height, width));Log.i(TAG + ".onMeasure", String.format("(iY, iX): [%s][%s]\n\n", indentY, indentX));
        setMeasuredDimension(width, height);
    }


    public boolean play(@RawRes int idFile) {
        this.maze = new Maze(getContext());
        if (!this.maze.read(idFile)) {
            Log.e(TAG, "Error en play.read");
            return false;
        }
        if (checkFile(idFile) != -1) {
            muestraMensaje(getResources().getString(R.string.encontrada));
            if (this.input == null) this.input = maze.getInput();
            if (this.output == null) this.output = maze.getOutput();
            if (this.actual == null) this.actual = this.input;
            Log.i(TAG, "input: " + this.input + ", output: " + this.output + ", actual: " + this.actual);
            //this.mazeText.setText(this.maze.toString());
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
//		System.out.println("Mapa:\n" + mazeAux.toString()+"\nInput:  " + mazeAux.getInput()+"\nOutput: " + mazeAux.getOutput());
        pathlength = mazeAux.deepSearchStack(mazeAux.getInput(), strat);
//		pathlength = mazeAux.breadthSearchQueue(mazeAux.getInput(), strat);
//		pathlength = mazeAux.pathPaint(mazeAux.deepSearchRec(mazeAux.getInput(), strat, false));
        return pathlength;
    }

    @Override
    @SuppressLint("DrawAllocation")
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float xTemp = 1, yTemp = 1;
//        setCellsXY(this.maze.getNcols(), this.maze.getNrows());
        setIndentXY();
//        for (int y = 0; y < mazeMap.length; y++) { for (int x = 0; x < mazeMap[y].length; x++) { char aux = getChar(x, y);if (aux != FileChars.ERRORCHAR.c && aux == FileChars.INPUT.c) { xTemp = (float) ((2 * x) * this.indentX);yTemp = (float) ((2 * y) * this.indentY); } } }
        for (int y = 0; y < this.maze.getNrows()/*mazeMap.length*/; y++) {
            for (int x = 0; x < this.maze.getNcols()/*mazeMap[y].length*/; x++) {
                char aux = this.maze.getPoint(x, y).getSymbol()/*getChar(x, y)*/;
                if (aux != FileChars.ERRORCHAR.c && aux == FileChars.INPUT.c) {
                    xTemp = (float) ((2 * x + 0.0) * this.indentX);
                    yTemp = (float) ((2 * y + 0.0) * this.indentY);
                }
            }
        }
//        Log.i(TAG+".onDraw", String.format("(yy, xx): [%s][%s]%n", yy, xx));Log.i(TAG+".onDraw", String.format("(h, w): [%s][%s]%n", height, width));Log.i(TAG+".onDraw", String.format("(iY, iX): [%s][%s]%n", indentY, indentX));Log.i(TAG+".onDraw", String.format("(Temp): [%s][%s]\n", yTemp, xTemp));Log.i(TAG+".onDraw", String.format("(yx+Temp): [%s][%s]\n", yy + yTemp, xx + xTemp));Log.i(TAG+".onDraw", String.format("(yx+Temp*index): [%s][%s]\n", yy + yTemp * indentY, xx + xTemp * indentX));
        drawMaze(canvas, this.maze, xx /* * xTemp*/, yy /* * yTemp*/);
//        drawCircle(canvas, xx + xTemp, yy + yTemp);
        drawPlayer(canvas, xx + xTemp/* *4*/, yy + yTemp/* *4*/);
    }

    public void drawMaze(Canvas canvas, Maze maze, float dX, float dY) {
        maze.setDrawValues(/*getContext(),*/ bitmaps, this.cellsX, this.cellsY, this.width, this.height);
        Log.i(TAG, "input: " + this.input + ", output: " + this.output + ", actual: " + this.actual);
        maze.drawMaze(canvas, dX + indentX, dY + indentY);
//        Toast.makeText(getContext(), actual.toString(), Toast.LENGTH_SHORT).show();
    }

    public void drawPlayer(Canvas canvas, float dX, float dY) {
        RectF drawRect = new RectF();
        drawRect.set(0, 0, (float) indentX * 2, (float) indentY * 2);
        drawRect.offsetTo(dX - indentX, dY - indentY);
        canvas.drawBitmap(bitmaps[10], null, drawRect, null);
    }

    public void drawCircle(Canvas canvas, float dX, float dY) {
        android.graphics.Paint paint = new android.graphics.Paint();
        paint.setColor(Color.CYAN);
        paint.setStyle(android.graphics.Paint.Style.FILL);
        canvas.drawCircle(dX, dY, Math.min(indentX, indentY), paint);
    }

    public boolean up() {
        if (!this.hasGanado && this.actual != null) {
            Point neighbor = maze.getNeighbor(this.actual, Movements.UP);
            Log.i(TAG, String.format("actual: %s --> neighbor: %s", actual, neighbor));
            if (neighbor != null && !neighbor.isErrorChar() && !neighbor.isBarrier()) {
                this.yy -= this.indentY;
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
        if (!this.hasGanado && this.actual != null) {
            Point neighbor = maze.getNeighbor(this.actual, Movements.DOWN);
            Log.i(TAG, String.format("actual: %s --> neighbor: %s", actual, neighbor));
            if (neighbor != null && !neighbor.isErrorChar() && !neighbor.isBarrier()) {
                this.yy += this.indentY;
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
        if (!this.hasGanado && this.actual != null) {
            Point neighbor = maze.getNeighbor(this.actual, Movements.LEFT);
            Log.i(TAG, String.format("actual: %s --> neighbor: %s", actual, neighbor));
            if (neighbor != null && !neighbor.isErrorChar() && !neighbor.isBarrier()) {
                this.xx -= this.indentX;
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
        if (!this.hasGanado && this.actual != null) {
            Point neighbor = maze.getNeighbor(this.actual, Movements.RIGHT);
            Log.i(TAG, String.format("actual: %s --> neighbor: %s", actual, neighbor));
            if (neighbor != null && !neighbor.isErrorChar() && !neighbor.isBarrier()) {
                this.xx += this.indentX;
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
        if (this.actual.equals(this.output)) this.hasGanado = true;
    }

    public boolean hasGanado() {
        return this.hasGanado;
    }

    protected void muestraMensaje(String txt) {
        Toast.makeText(getContext(), txt, Toast.LENGTH_SHORT).show();
    }
//    protected void muestraMensaje(int resId) { Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show(); }
}
