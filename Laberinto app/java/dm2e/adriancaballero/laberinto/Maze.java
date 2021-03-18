package dm2e.adriancaballero.laberinto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import androidx.annotation.RawRes;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

/**
 * @author Adrian Caballeo Orasio
 */
public class Maze {
    private final String TAG = getClass().getName();
    private float screenWidth, screenHeight;
    private RectF drawRect = new RectF();
    private Bitmap[] bitmaps;
    private Point[][] coord;
    private Context context;
    private int nX, nY;
//    private Bitmap[] bitmaps = new Bitmap[]{FileChars.SPACE.getBitmap(this.context), FileChars.BARRIER.getBitmap(this.context), FileChars.INPUT.getBitmap(this.context), FileChars.OUTPUT.getBitmap(this.context), Movements.UP.getBitmap(this.context), Movements.DOWN.getBitmap(this.context), Movements.LEFT.getBitmap(this.context), Movements.RIGHT.getBitmap(this.context), Movements.VISITED.getBitmap(this.context), Movements.ACTUAL.getBitmap(this.context), FileChars.ERRORCHAR.getBitmap(this.context),};

    /**Inicializa un mapa, reservando memoria y devolviendo el mapa inicializado si lo ha hecho correctamente o NULL si no
     * Initialize a new maze.
     * @param contexto The context of the app
     * @param coord The wall data array. Each true value in the array represents a wall and each false represents a gap*/
    public Maze(Context contexto, Point[][] coord, int nX, int nY) { this.context = contexto;this.coord = coord;this.nX = nX;this.nY = nY; }
    public Maze(Context contexto) { this(contexto,new Point[Utils.MAX][Utils.MAX], -1, -1);for (int y = 0; y < Utils.MAX; y++) { for (int x = 0; x < Utils.MAX; x++) { this.coord[y][x] = null; } } }
    public Maze() { this(null/*, new Point[Utils.MAX][Utils.MAX], -1, -1*/); }



    ///** Libera la memoria dinamica reservada para un mapa */void map_destroy(Map *pl);
    /**Devuelve el numero de filas de un mapa dado, o -1 si se produce algun error*/
    public int getNcols() { return this.nX; }
    /**Devuelve el numero de columnas de un mapa dado, o -1 si se produce algun error*/
    public int getNrows() { return this.nY; }
    /**Devuelve el punto de entrada en un mapa dado, o NULL si se produce algun error o no existe un punto de ese tipo*/
    public Point getInput() { for (int y = 0; y < getNrows(); y++) { for (int x = 0; x < getNcols(); x++) { if (getPoint(x, y) != null && getPoint(x, y).isInput()) { return getPoint(x, y); } } }return null; }
    /**Devuelve el punto de salida en un mapa dado, o NULL si se produce algun error o no existe un punto de ese tipo*/
    public Point getOutput() {
        for (int y = 0; y < getNrows(); y++) {
            for (int x = 0; x < getNcols(); x++) {
                if (getPoint(x, y) != null && getPoint(x, y).isOutput()) {
                    return getPoint(x, y);
                }
            }
        }
        return null;
    }
    /**Devuelve el punto situado en (x,y), o NULL si se produce algun error.
     * Get the type of the cell. x and y values are not coordinates!
     * @param x The x index of the cell
     * @param y The y index of the cell
     * @return The cell type*/
    public Point getPoint(int x, int y) { if (x < 0 || x > getNcols() || y < 0 || y > getNrows()) { return null; }return this.coord[y][x]; }
    /**Devuelve el punto resultante al realizar un movimiento en un mapa a partir de un punto inicial, o NULL si se produce algun error*/
    public Point getNeighbor(Point p, Movements strat) {
        if (p == null || strat != Movements.RIGHT && strat != Movements.UP && strat != Movements.LEFT && strat != Movements.DOWN) { Log.e(TAG, "ERROR getNeighbor: " + p+", "+strat);return null; }
        int x = p.getX(), y = p.getY();
        switch (strat) {
            case UP:    { if (y > 0         ) { if ((getPoint(x, y - 1) != null)) { y--; } else { Log.e("Maze.getNeighbor",String.format("coord[%d--][%d] no existe\n", x, y)); } } else { Log.e("Maze.getNeighbor",String.format("coord[%d--][%d] y es menor que nX=0 \n", x, y)); }break; }
            case DOWN:  { if (y < getNrows()) { if ((getPoint(x, y + 1) != null)) { y++; } else { Log.e("Maze.getNeighbor",String.format("coord[%d++][%d] no existe\n", x, y)); } } else { Log.e("Maze.getNeighbor",String.format("coord[%d++][%d] y es mayor que nY=%d\n", x, y, this.coord.length)); }break; }
            case LEFT:  { if (x > 0         ) { if ((getPoint(x - 1, y) != null)) { x--; } else { Log.e("Maze.getNeighbor",String.format("coord[%d][%d--] no existe\n", x, y)); } } else { Log.e("Maze.getNeighbor",String.format("coord[%d][%d--] x es menor que nX=0 \n", x, y)); }break; }
            case RIGHT: { if (x < getNcols()) { if ((getPoint(x + 1, y) != null)) { x++; } else { Log.e("Maze.getNeighbor",String.format("coord[%d][%d++] no existe\n", x, y)); } } else { Log.e("Maze.getNeighbor",String.format("coord[%d][%d++] x es mayor que nX=%d\n", x, y, this.coord[y].length)); }break; }
        }
        Log.i(TAG, getPoint(x, y).toString());
        return getPoint(x, y);//return (!getPoint(x,y).isBarrier()) ? getPoint(x,y) : p;
    }
    public Point getNeighbor(int x, int y, Movements strat) { return getNeighbor(getPoint(x,y),strat); }
    public Point[][] getCoord() { return this.coord; }
    public float getCellWidth() { return drawRect.width(); }
    public float getCellHeight() { return drawRect.height(); }

    /**Indica el tamaño de un mapa, devuelve NULL si se produce algun error*/
    public boolean setSize(int nX, int nY) {
        if (nX <= 0 || nX > Utils.MAX || nY <= 0 || nY > Utils.MAX) { return false; }
        setNcols(nX);
        setNrows(nY);
        return true;
    }
    /**Añade un punto a un mapa dado reservando nueva memoria (de ahi que elargumento sea declarado como const), o modifica el punto si ya se encuentra.Debe comprobar si el punto es de tipo Output o Input para guardarlo comocorresponda. Devuelve OK si tod o ha ido correctamente (se ha podidoincluir/actualizar el punto).*/
    public boolean setPoint(Point p) {
        if (p == null) { Log.e("Maze.java","error en setPoint 1");return false; }
        int x = p.getX(), y = p.getY();
        if (x < 0 || x > getNcols() || y < 0 || y > getNrows()) { Log.e("Maze.java","error en setPoint 2");return false; }
        if (p.isInput() && getInput() != null) { getInput().setSymbol(FileChars.SPACE.c); }
        else if (p.isOutput() && getOutput() != null) { getOutput().setSymbol(FileChars.SPACE.c); }
        this.coord[y][x] = p;
        return true;
    }
    public boolean setPoint(int x, int y, char symbol) { return setPoint(new Point(x, y, symbol)); }
    public void setNcols(int nX) { this.nX = nX; }
    public void setNrows(int nY) { this.nY = nY; }
    public void setCoord(Point[][] coord) { this.coord = coord; }

    //    /**Imprime en un fichero dado los datos de un mapa. Ademas, devolvera el numero de caracteres que se han escrito con exito (mirar documentacion de fprintf)*/int map_print(FILE *file, const Map *pl);
//    public int length() { String txt = String.format("%s", toString());return txt.toCharArray().length; }
    @Override public String toString() { StringBuilder txt = new StringBuilder();for (int y = 0; y < getNrows(); txt.append("\n"), y++) { for (int x = 0; x < getNcols(); x++) { try { txt.append(String.format("%c", getPoint(x, y).getSymbol())); } catch (Exception e) { Log.e(TAG, String.format("[%2d, %2d] en [%2d, %2d)", y, x, getNcols(),getNrows())); } } }return txt.toString(); }
    public String toCoordString() { StringBuilder txt = new StringBuilder();for (int y = 0; y < getNrows(); txt.append("\n"), y++) { for (int x = 0; x < getNcols(); x++) { try { txt.append(String.format("%s", getPoint(x, y))); } catch (Exception e) { txt.append(String.format("[(%2d, %2d) %s]", y, x, FileChars.ERRORCHAR.c)); } } }return txt.toString(); }

    public boolean read(@RawRes int pf) {
        try {
            List<String> lineas = new ArrayList<>();
            InputStream is = this.context.getApplicationContext().getResources().openRawResource(pf);
            Scanner sc = new Scanner(is);
            if (sc.hasNextLine()) { while (sc.hasNextLine()) { lineas.add(sc.nextLine()); } }
            if (lineas.isEmpty()) { throw new Exception("Error in Maze.read.Files.readAllLines()"); }
            /* asignamos dimension al laberinto */
            String[] nXY = lineas.get(0).split(" ");
            int nI = Integer.parseInt(nXY[0]), nJ = Integer.parseInt(nXY[1]);
//            this.mazeMap = new Point[nI][nJ];
            if (!setSize(nJ, nI)) { throw new Exception("Error in read.setSize()"); }
//            Log.i(TAG, "[y][x]:\t" + nI + "," + nJ + "\t" + lineas.size() + "," + lineas.get(1).length() + "\t" + getNrows() + "," + getNcols());
            /* leemos el fichero linea a linea */
            for (int y = 0; y < nI; y++) {
                char[] buff = lineas.get(y + 1).toCharArray();
                for (int x = 0; x < nJ; x++) {
                    setPoint(x, y, buff[x]);
//                    Log.i(TAG, getPoint(x, y)+"\t");
                    if (getPoint(x, y) == null) { throw new Exception(
                            String.format("Error al introducir [%3d][%3d]: "+getPoint(x, y)+".",y,x)); }
                }
//                Log.i(TAG, "\n");
            }
//            Log.i(TAG, to);
            sc.close();is.close();return true;
        } catch (Exception e) { e.printStackTrace(); }return false;
    }


//    public boolean read0(@RawRes int pf) { try { List<String> lineas = new ArrayList<>();InputStream is = this.context.getApplicationContext().getResources().openRawResource(pf);Scanner sc = new Scanner(is);if (sc.hasNextLine()) { while (sc.hasNextLine()) { lineas.add(sc.nextLine()); } }sc.close();is.close();if (lineas.isEmpty()) { throw new Exception("Error in Maze.read.Files.readAllLines()"); }/* creamos punto que se utiliza como buffer */Point temp = new Point();if (temp == null) { throw new Exception("Error in read.temp = new Point()");}/* asignamos dimensiones al laberinto */String[] nXY = lineas.get(0).split(" ");int nI = Integer.parseInt(nXY[0]), nJ = Integer.parseInt(nXY[1]);if (!setSize(nJ, nI)) { throw new Exception("Error in read.setSize()"); }/* leemos el fichero linea a linea */for (int x = 0; x < nJ; x++) { char[] buff = lineas.get(x + 1).toCharArray();for (int y = 0; y < nI; y++) { char c = buff[y];/* ajustamos los atributos del punto leido (falta añadir control de errores) */if (!temp.setX(x)) { Log.e(TAG, "Error in read.temp.setX(" + x + ")"); }if (!temp.setY(y)) { Log.e(TAG, "Error in read.temp.setY(" + x + ")"); }if (!temp.setSymbol(c)) { Log.e(TAG, "Error in read.temp.setSymbol(" + c + ")"); }/* insertamos el punto en el laberinto (falta añadir control de errores) */if (!setPoint(temp)) { Log.e(TAG, "Error in read.temp.setPoint(" + temp + ")"); } } }return true; } catch (Exception e) {/*Log.e(TAG,e.toString());*/e.printStackTrace(); return false;} }

    /**Pasa los datos de un mapa a una pila, Null en caso de error*/
    public Stack<Point> toStack() { Stack<Point> stack = new Stack<>();for (int y = 0; y < getNrows(); y++) { for (int x = 0; x < getNcols(); x++) { if (stack.size() == getNrows() * getNcols()) { Log.e(TAG, "Stack is too small for maze.");return null; }Point cp = getPoint(x, y);if (stack.push(cp) == null) { Log.e(TAG, "Error while adding point to stack.");return null; } } }return stack; }
    /**Pasa los datos de un mapa a una cola, Null en caso de error.*/
    public Queue<Point> toQueue() { Queue<Point> queue = new LinkedList<>();for (int y = 0; y < getNrows(); y++) { for (int x = 0; x < getNcols(); x++) { if (queue.size() == getNrows() * getNcols()) { Log.e(TAG, "Queue is too small for maze.");return null; }Point cp = getPoint(x, y);if (!queue.add(cp)) { Log.e(TAG, "Error while adding point to queue.");return null; } } }return queue; }

    /**Realiza el recorrido completo de un mapa en busca del OUTPUT desde el INPUT, usa una pila.*/
    public int deepSearchStack(Point input, Movements[] strat) { Stack<Point> stack = new Stack<>();stack.push(input);while (!stack.isEmpty()) { Point cp = stack.pop();if (!cp.isVisited()) { if (!getPoint(cp.getX(), cp.getY()).setSymbol(Movements.VISITED.c)) { return -1; }for (int i = strat.length - 1; i >= 0; i--) {/* stack is LIFO-> movements with higher preference have to be checked last */Point neighbor = getNeighbor(cp, strat[i]);if (neighbor != null) { if (neighbor.isOutput()) { neighbor.setParent(getPoint(cp.getX(), cp.getY()));return pathPaint(neighbor); }if (neighbor.isSpace()) { neighbor.setParent(getPoint(cp.getX(), cp.getY()));if (stack.push(neighbor) == null) { Log.e(TAG, "Error in stack_push.");return -1; } } } } } }return -1; }



    /**Realiza el recorrido completo de un mapa en busca del OUTPUT desde el INPUT, usa una cola.*/
    public int breadthSearchQueue(Point input, Movements[] strat) {
        Queue<Point> queue = new LinkedList<>();
        queue.add(input);
        while (!queue.isEmpty()) {
            Point cp = queue.remove();
            if (!cp.isVisited()) {
                if (!cp.isInput()) {
                    if (!getPoint(cp.getX(), cp.getY()).setSymbol(Movements.VISITED.c)) {
                        return -1;
                    }
                }
                for (Movements movements : strat) {/* queue is FIFO-> movements with higher preference have to be checked first */
                    Point neighbor = getNeighbor(cp, movements);
                    if (neighbor != null) {
                        if (neighbor.isOutput()) {
                            neighbor.setParent(getPoint(cp.getX(), cp.getY()));
                            return pathPaint(neighbor);
                        }
                        if (neighbor.isSpace()) {
                            neighbor.setParent(getPoint(cp.getX(), cp.getY()));
                            if (!queue.add(neighbor)) {
                                Log.e(TAG, "Error in queue_insert.\n");
                                return -1;
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }

    public Point deepSearchRec(Point input, Movements[] strat, boolean compare) {
        if (compare) {
            Log.i(TAG,"Current maze:\n" + toString());
        }
        if (input.isOutput()) {
            return input;
        }
        input.setSymbol(Movements.VISITED.c);
        for (Movements movements : strat) {
            Point neighbor = getNeighbor(input, movements);
            if (neighbor != null) {
                if (!neighbor.isVisited() && !neighbor.isActual() && !neighbor.isBarrier()) {
                    neighbor.setParent(getPoint(input.getX(), input.getY()));
                    if (!neighbor.isOutput()) {
                        neighbor.setSymbol(Movements.ACTUAL.c);
                    }
                    Point auxpoint = deepSearchRec(neighbor, strat, compare);
                    if (auxpoint != null) {/*if(auxpoint.getParent().equals(input)){if(compare==true){System.out.println("Solution!");paintPath();}}*/
                        return auxpoint;
                    }
                }
            }
        }
        return null;
    }
    public int pathPaint(Point output) { int pathlength = 0;Point cp = output;if (cp == null) { return -1; }while (cp.getParent() != null) { if (cp.getParent().getX() > cp.getX()) { cp.getParent().setSymbol(Movements.UP.c); } else if (cp.getParent().getX() < cp.getX()) { cp.getParent().setSymbol(Movements.DOWN.c); } else if (cp.getParent().getY() > cp.getY()) { cp.getParent().setSymbol(Movements.LEFT.c); } else if (cp.getParent().getY() < cp.getY()) { cp.getParent().setSymbol(Movements.RIGHT.c); } else { cp.getParent().setSymbol(Movements.ACTUAL.c); }cp = cp.getParent();pathlength++; }cp.setSymbol(FileChars.INPUT.c);output.setSymbol(FileChars.OUTPUT.c);return pathlength; }
    public void paintPath() { Point cp = getOutput();if (cp == null) { return; }while (cp.getParent() != null) { System.out.print(cp + " <-- ");cp = cp.getParent(); }cp.setSymbol(FileChars.INPUT.c);System.out.println(cp); }
    public void startMazeText() { Scanner scan = new Scanner(System.in);Point input = getInput(), output = getOutput(), actual = input;int moves = 0;char move;System.out.println("START");while (!actual.equals(output)) { System.out.println(Utils.formatL("", '-', 108));System.out.println(toString());System.out.printf("Moves: %3d              w%nIntroduce movimiento: a s d: ", moves);move = scan.next().charAt(0);try { Point neighbor;switch (move) { case 'a': { neighbor = getNeighbor(actual, Movements.LEFT);break; } case 'd': { neighbor = getNeighbor(actual, Movements.RIGHT);break; } case 'w': { neighbor = getNeighbor(actual, Movements.UP);break; } case 's': { neighbor = getNeighbor(actual, Movements.DOWN);break; } default: { throw new IllegalArgumentException("Unexpected value: " + move); } }if (!neighbor.isOutput()) { if (!actual.isInput() && !actual.isOutput()) { actual.setSymbol(FileChars.SPACE.c); }if (!neighbor.isInput()) { neighbor.setSymbol(Movements.ACTUAL.c); } } else { System.out.println("isOutput");actual.setSymbol(FileChars.SPACE.c); }moves++;/* if(temp.equals(actual)==false){moves++;} */actual = neighbor; } catch (Exception e) { Log.e(TAG, String.valueOf(e)); } }System.out.println(Utils.formatL("", '-', 108));System.out.println(toString());System.out.println("YOU WIN!! in " + moves + " MOVESMENTS");scan.close(); }







    /**@param bitmaps         The desired bitmaps for the floors and walls
     * @param xPointsOnScreen How many cells are visible on the screen on the x axis
     * @param yPointsOnScreen How many cells are visible on the screen on the y axis
     * @param screenWidth     The screen width
     * @param screenHeight    The screen height
     */
    public void setDrawValues(/*Context context,*/ Bitmap[] bitmaps, float xPointsOnScreen, float yPointsOnScreen, float screenWidth, float screenHeight) {
        this.context = context/*.getContext()*/;
        this.bitmaps = bitmaps;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        drawRect.set(0, 0, screenWidth / xPointsOnScreen, screenHeight / yPointsOnScreen);
        System.out.printf("Maze:\t(sh,sw)[%s][%s]", screenHeight, screenWidth);
        System.out.printf("Maze:\t(ycs,xcs)[%s][%s]", yPointsOnScreen, xPointsOnScreen);
        System.out.printf("Maze:\t(sh/ycs,sw/xcs)[%s][%s]%n", screenHeight / yPointsOnScreen, screenWidth / xPointsOnScreen);
    }







    /**
     * Draws the maze. View coordinates should have positive values.
     *
     * @param canvas Canvas for the drawing
     * @param viewX  The x coordinate of the view
     * @param viewY  The y coordinate of the view
     */
    public void drawMaze(Canvas canvas, float viewX, float viewY) {
        int tileX = 0;
        int tileY = 0;
        float xCoord = -viewX;
        float yCoord = -viewY;

//        this.bitmaps=bitmaps;
//        System.out.printf("drawMaze:\n");
        while (tileY < getNrows()/*coord.length*/ && yCoord <= this.screenHeight) {
            tileX = 0;
            xCoord = -viewX;
//            System.out.printf("\ndrawMaze:\t");
            while (tileX < getNcols()/*coord[(int) tileY].length*/ && xCoord <= this.screenWidth) {
                int num = getBitmapN((int) tileX, (int) tileY);;
                Bitmap bitmap = getBitmap((int) tileX, (int) tileY);/*bitmaps[coord[tileY][tileX]];*/
                /*bitmaps[coord[tileYY][tileYX]];*/
//                System.out.printf("[%3d][%3d]: %2d\t", tileY, tileX, num);

//                Bitmap bitmap = bitmaps[num];
                if (bitmap != null) {
                    if (xCoord + drawRect.width() >= 0 && yCoord + drawRect.height() >= 0) {
                        /*if (coord[(int) tileY][(int) tileX].isInput()) {System.out.printf("i==[%s][%s]%n", yCoord, xCoord);}*/
                        drawRect.offsetTo(xCoord, yCoord);
                        canvas.drawBitmap(bitmap, null, drawRect, null);
                    }
                }
                tileX++;
                xCoord += drawRect.width();
            }
//            System.out.printf("\n");
            tileY++;
            yCoord += drawRect.height();
        }
//        float yCoord=-viewY;for(int tileY=0;tileY<coord.length&&yCoord<=this.screenHeight;tileY++,yCoord+=drawRect.height()){float xCoord=-viewX;for(int tileX=0;tileX<coord[(int)tileY].length&&xCoord<=this.screenWidth;tileX++,xCoord+=drawRect.width()){Bitmap bitmap=getBitmap((int)tileX,(int)tileY);/*bitmaps[coord[tileY][tileX]];*/if(bitmap!=null){if(xCoord+drawRect.width()>=0&&yCoord+drawRect.height()>=0){if(coord[(int)tileY][(int)tileX].isInput()){System.out.printf("i==[%s][%s]%n",yCoord,xCoord);}drawRect.offsetTo(xCoord,yCoord);canvas.drawBitmap(bitmap,null,drawRect,null);}}}}
    }

    public Bitmap getBitmap(int x, int y) {
        char c = getPoint(x, y).getSymbol();
        FileChars fc = FileChars.valueOf(c);
        Movements mc = Movements.valueOf(c);
        int num = -1;
        if (fc != null) switch (fc) {
            case SPACE:{num=0;break;}case BARRIER:{num=1;break;}case INPUT:{num=2;break;}
            case OUTPUT:{num=3;break;}case ERRORCHAR:{num=4;break;}
        }
        if (mc != null) switch (mc) {
            case UP:{num=5;break;}case DOWN:{num=6;break;}case LEFT:{num=7;break;}
            case RIGHT:{num=8;break;}case VISITED:{num=9;break;}case ACTUAL:{num=10;break;}
        }
        return bitmaps[num];
    }
    public int getBitmapN(int x, int y) {
        char c = getPoint(x, y).getSymbol();
        FileChars fc = FileChars.valueOf(c);
        Movements mc = Movements.valueOf(c);
        if (fc != null) switch (fc) { case SPACE:{return 0;}case BARRIER:{return 1; }case INPUT:{return 2; } case OUTPUT:{return 3;}case ERRORCHAR:{return 4;} }
        if (mc != null) switch (mc) { case UP:{return 5;}case DOWN:{return 6;}case LEFT:{return 7;} case RIGHT:{return 8;}case VISITED:{return 9;}case ACTUAL:{return 10;} }
        return -1;
    }

}