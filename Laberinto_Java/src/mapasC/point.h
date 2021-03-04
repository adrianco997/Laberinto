/*
 * File:   point.h
 * Authors: Adrian Caballero Orasio, Frederik Mayer
 * Created on 7 de febrero de 2018, 09:28
 */
#ifndef POINT_H
#define POINT_H

#include <stdio.h>
#include "types.h"

typedef struct _Point Point;

/* Constantes publicas que definen los tipos de puntos que se permiten en un mapa */
#define ERRORCHAR 'E'
#define INPUT     'i'
#define VISITED   'V'
#define OUTPUT    'o'
#define BARRIER   '+'
#define SPACE     ' '

/* Inicializa un punto, reservando memoria y devolviendo el punto inicializado si lo ha hecho correctamente o NULL si no */
Point* point_ini(int x, int y, char s);
/* Libera la memoria dinamica reservada para un punto */
void point_destroy(Point *p);

/* Devuelve la coordenada X de un punto dado, o -1 si se produce algun error */
int point_getCoordinateX(const Point *p);
/* Devuelve la coordenada Y de un punto dado, o -1 si se produce algun error */
int point_getCoordinateY(const Point *p);
/* Devuelve el simbolo de un punto dado, o ERRORCHAR si se produce algun error */
char point_getSymbol(const Point *p);
/* Devuelve el punto padre de un punto dado, o NULL si se produce algun error */
Point *point_getParent(const Point *p);

/* Funciones derivadas que indican el tipo de punto que tenemos */
Bool point_isInput(Point *p);
Bool point_isOutput(Point *p);
Bool point_isBarrier(Point *p);
Bool point_isSpace(Point *p);

/* Modifica la coordenda X de un punto dado, devuelve ERROR si se produce algun error */
Status point_setCoordinateX(Point *p, const int x);
/* Modifica la coordenda Y de un punto dado, devuelve ERROR si se produce algun error */
Status point_setCoordinateY(Point *p, const int y);
/* Modifica el simbolo de un punto dado, devuelve ERROR si se produce algun error */
Status point_setSymbol(Point *p, const char s);
/* Modifica el punto padre de un punto dado, o ERROR si se produce algun error */
Status point_setParent(Point *p, Point *parent);

/* Devuelve TRUE si los dos puntos pasados como argumentos son iguales (revisando todos sus campos). Devuelve FALSE en otro caso. */
Bool point_equals(const Point *p1, const Point *p2);
/* Copia los datos de un punto a otro devolviendo el punto copiado (incluyendo la reserva de la memoria necesaria) si todo ha ido bien, o NULL en otro caso */
Point *point_copy(const Point *p);
/* Imprime en un fichero dado los datos de un punto con el siguiente formato:[(x,y): symbol]. Por ejemplo, un punto con simbolo â€œ*â€�, con coordenada X 3 e Y 7 se representara como [(3, 7): *]. Ademas, devolvera el numero de caracteres que se han escrito con exito (mirar documentacion de fprintf) */
int point_print(FILE *file, const Point *p);

#endif /* POINT_H */
