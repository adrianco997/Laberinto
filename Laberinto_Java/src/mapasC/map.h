/*
 * File:   map.h
 * Author: Adrian Caballero Orasio, Frederik Mayer
 * Created on 7 de febrero de 2018, 09:28
 */
#ifndef MAP_H
#define	MAP_H

#include <stdio.h>
#include "types.h"
#include "point.h"

typedef struct _Map Map;

/* Inicializa un mapa, reservando memoria y devolviendo el mapa inicializado si lo ha hecho correctamente o NULL si no */
Map *map_ini();
/* Libera la memoria dinamica reservada para un mapa */
void map_destroy(Map *pl);

/* Devuelve el numero de filas de un mapa dado, o -1 si se produce algun error */
int map_getNrows(const Map *pl);
/* Devuelve el numero de columnas de un mapa dado, o -1 si se produce algun error */
int map_getNcols(const Map *pl);
/* Devuelve el punto de entrada en un mapa dado, o NULL si se produce algun error o no existe un punto de ese tipo */
Point *map_getInput(const Map *pl);
/* Devuelve el punto de salida en un mapa dado, o NULL si se produce algun error o no existe un punto de ese tipo */
Point *map_getOutput(const Map *pl);
/* Devuelve el punto situado en (x,y), o NULL si se produce algun error */
Point *map_getPoint(const Map *pl, int x, int y);
/* Devuelve el punto resultante al realizar un movimiento en un mapa a partir de un punto inicial, o NULL si se produce algun error */
Point *map_getNeighborPoint(const Map *pl, const Point *p, const Move mov);

/* Indica el tamaño de un mapa, devuelve NULL si se produce algun error */
Status map_setSize(Map *pl, int nrow, int ncol);
/* Añade un punto a un mapa dado reservando nueva memoria (de ahi que el argumento sea declarado como const), o modifica el punto si ya se encuentra. Debe comprobar si el punto es de tipo Output o Input para guardarlo como corresponda. Devuelve OK si todo ha ido correctamente (se ha podido incluir/actualizar el punto). */
Status map_setPoint(Map *pl, const Point *p);

/* Imprime en un fichero dado los datos de un mapa. Ademas, devolvera el numero de caracteres que se han escrito con Ã©xito (mirar documentaciÃ³n de fprintf) */
int map_print(FILE *file, const Map *pl);


#endif	/* MAP_H */
