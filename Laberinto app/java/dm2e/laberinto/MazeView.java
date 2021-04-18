package dm2e.laberinto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by BunyamiN on 25-5-2015.
 * https://stackoverflow.com/questions/20747138/android-drawing-a-maze-to-canvas-with-smooth-character-movement/
 *
 * @author Adrian Caballeo Orasio
 */
public class MazeView extends androidx.appcompat.widget.AppCompatImageView {
    private final String TAG = getClass().getName();
    private int iniWidth, iniHeight, width, height, cellsX, cellsY;
    private Point actual = null;
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
            this.cellsX = 5;
        }/*invalidate();*/
        return this.cellsX;
    }

    public int addCellsY() {
        if (cellsY < 10) {
            this.cellsY += 1;
        } else {
            this.cellsY = 5;
        }/*invalidate();*/
        return this.cellsY;
    }

    public int getCellsX() {
        return this.cellsX;
    }

    public int getCellsY() {
        return this.cellsY;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public void setCellsX(int cellsX) {
        this.cellsX = cellsX;
    }

    public void setCellsY(int cellsY) {
        this.cellsY = cellsY;
    }

    public void setActual(Point actual) {
        this.actual = actual;
    }

    public void restart() {
        Maze.map_destroy(this.maze);
        this.maze = null;
        this.actual = null;
        this.width = this.iniWidth;
        this.height = this.iniHeight;
    }

    @Override
    @SuppressLint("DrawAllocation")
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.maze != null && this.actual != null) {
            drawMaze(canvas, this.maze, this.actual, (float) this.width / this.cellsX / 2, (float) this.height / this.cellsY / 2);
        }
    }

    public void drawMaze(Canvas canvas, Maze maze, Point actual, float dX, float dY) {
        maze.setDrawValues(bitmaps, cellsX, cellsY, width, height);
        maze.drawMaze(canvas, dX, dY, actual);
    }
    //public void drawPlayer(Canvas canvas, float dX, float dY) { RectF drawRect = new RectF();drawRect.set(0, 0, (float) width / cellsX, (float) height / cellsY);drawRect.offsetTo(dX, dY);canvas.drawBitmap(bitmaps[10], null, drawRect, null); }
    //public void drawCircle(Canvas canvas, float dX, float dY) { android.graphics.Paint paint = new android.graphics.Paint();paint.setColor(Color.CYAN);paint.setStyle(android.graphics.Paint.Style.FILL);canvas.drawCircle(dX, dY, Math.min(width / cellsX / 2, height / cellsY / 2), paint); }

    //protected void muestraMensaje(String txt) { Toast.makeText(getContext(), txt, Toast.LENGTH_SHORT).show(); }
    //protected void muestraMensaje(String txt, Object ...args) { Toast.makeText(getContext(), String.format(txt,args), Toast.LENGTH_SHORT).show(); }
    //protected void muestraMensaje(int resId) { Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show(); }
}
