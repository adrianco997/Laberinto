/*
 * File: utils.h
 * Authors: Adrian Caballero Orasio, Frederik Mayer
 * Created on 7 de febrero de 2018, 09:28
 */

#ifndef UTILS_H
#define UTILS_H

#include <stdio.h>
#include "point.h"
#include "map.h"
#include "stack_fp.h"
#include "queue.h"
#include "functions.h"
#include "types.h"

/* Lee los datos de un archivo necesarios de un archivo para crear un mapa */
Status map_read(FILE *pf, Map *pl);
/* Pasa los datos de un mapa a una pila, Null en caso de error */
Status map_to_stack(Map *map, Stack *stack);
/* Pasa los datos de un mapa a una cola, Null en caso de error */
Status map_to_queue(Map *map, Queue *queue);
/* Realiza el recorrido completo de un mapa en busca del OUTPUT desde el INPUT, usa una pila.*/
Point *deep_search_stack(Map *map, Point *point);
/* Realiza el recorrido completo de un mapa en busca del OUTPUT desde el INPUT, usa una cola.*/
Point *breadth_search_queue(Map *map, Point *point);

#endif /* UTILS_H */
