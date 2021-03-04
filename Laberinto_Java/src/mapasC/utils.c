/*
 * File: utils.c
 * Authors: Adrian Caballero Orasio, Frederik Mayer
 * Created on 7 de febrero de 2018, 09:28
 */

#include <stdlib.h>
#include <string.h>
#include "utils.h"

#define MAX 4096
#define VISITED 'V'

Status map_read(FILE *pf, Map *pl) {
    int x, y, ncols, nrows; /* int indice; */
    char buff[MAX], caracter;
    Point *temp = NULL;
    Status st = ERROR;

    if (!pl || !pf) return ERROR;

    /*creamos punto que se utiliza como buffer*/
    temp = point_ini(0, 0, ERRORCHAR);
    if (!temp) return ERROR;
    /* asignamos dimensión al mapa */
    fgets(buff, MAX, pf);
    sscanf(buff, "%d %d", &nrows, &ncols);
    st = map_setSize (pl, nrows, ncols);
    if (st == ERROR) return ERROR;
    /* leemos el fichero linea a linea */
    for(y=0; y<nrows; y++) {
        fgets(buff, MAX, pf);
        for (x=0; x<ncols; x++) {
            caracter=buff[x];
            /* ajustamos los atributos del punto leído (falta añadir control de errores) */
            if (point_setCoordinateX(temp, x) == ERROR) printf("Error in map_read.");
            if (point_setCoordinateY(temp, y) == ERROR) printf("Error in map_read.");
            if (point_setSymbol(temp, caracter) == ERROR) printf("Error in map_read.");
            /* insertamos el punto en el mapa (falta añadir control de errores) */
            if (map_setPoint(pl, temp) == ERROR) printf("Error in map_read.");
        }
    }
    /* libera recursos */
    point_destroy(temp);
    /* no cerramos el fichero ya que lo han abierto fuera */
    return OK;
}

Status map_to_stack(Map *map, Stack *stack) {
    Point *current_point = NULL;
    int x = 0, y = 0;

    for (y=0; y<map_getNrows(map); y++) {
        for (x=0; x<map_getNcols(map); x++) {
            if (stack_isFull(stack)) {
                fprintf(stderr, "Stack is too small for map.\n");
                return ERROR;
            }
            current_point=point_copy(map_getPoint(map, x, y));
            if(stack_push(stack, (void*) current_point) == ERROR) {
                fprintf(stderr, "Error while adding point to stack.\n");
                return ERROR;
            }
            point_destroy(current_point);
            current_point = NULL;
        }
    }
    return OK;
}

Status map_to_queue(Map *map, Queue *queue) {
    Point *current_point = NULL;
    int x = 0, y = 0;

    for (y=0; y<map_getNrows(map); y++) {
        for (x=0; x<map_getNcols(map); x++) {
            if (queue_isFull(queue)) {
                fprintf(stderr, "Queue is too small for map.\n");
                return ERROR;
            }
            current_point=point_copy(map_getPoint(map, x, y));
            if(queue_insert(queue, (void*) current_point) == ERROR) {
                fprintf(stderr, "Error while adding point to queue.\n");
                return ERROR;
            }
            point_destroy(current_point);
            current_point = NULL;
        }
    }
    return OK;
}

int paint_path(Map* map, Point* output){
  int pathlength=0;
  Point* current_point=output;
  while (point_getParent(current_point)){
    if(point_getCoordinateX(point_getParent(current_point))>point_getCoordinateX(current_point)){
      point_setSymbol(point_getParent(current_point), '<');
    }
    else if(point_getCoordinateX(point_getParent(current_point))<point_getCoordinateX(current_point)){
      point_setSymbol(point_getParent(current_point), '>');
    }
    else if(point_getCoordinateY(point_getParent(current_point))>point_getCoordinateY(current_point)){
      point_setSymbol(point_getParent(current_point), '^');
    }
    else if(point_getCoordinateY(point_getParent(current_point))<point_getCoordinateY(current_point)){
      point_setSymbol(point_getParent(current_point), 'v');
    }
    else{
      point_setSymbol(point_getParent(current_point), 'O');
    }

    current_point=point_getParent(current_point);
    pathlength++;
  }
  point_setSymbol(current_point, 'i');
  point_setSymbol(output, 'o');
  return pathlength;
}

Point *deep_search_stack(Map *map, Point *input) {
    Point *currentpoint = NULL, *neighbour = NULL;
    Stack *stack = NULL;
    Move m = 0;

    stack=stack_ini(destroy_point_function, copy_point_function, print_point_function);
    stack_push(stack, (void*) input);

    while (stack_isEmpty(stack) == FALSE) {
        currentpoint = (Point*) stack_pop(stack);
        if (point_getSymbol(currentpoint) != VISITED) {
            if(point_setSymbol(currentpoint, VISITED) == ERROR) return NULL;
            if(map_setPoint(map, currentpoint) == ERROR) return NULL;
            for (m=0; m<4; m++) {
                neighbour = map_getNeighborPoint(map, currentpoint, m);
                if(neighbour) {
                    if (point_isOutput(neighbour) == TRUE) {
                        point_setParent(neighbour, map_getPoint(map, point_getCoordinateX(currentpoint), point_getCoordinateY(currentpoint)));
                        paint_path(map, neighbour);
                        point_destroy(currentpoint);
                        stack_destroy(stack);
                        return neighbour;
                    }
                    if (point_isSpace(neighbour) == TRUE) {
                        point_setParent(neighbour, map_getPoint(map, point_getCoordinateX(currentpoint), point_getCoordinateY(currentpoint)));
                        if(stack_push(stack, (void*) neighbour) == ERROR) {
                            fprintf(stderr,"Error in stack_push.\n");
                            return NULL;
                        }
                    }
                    neighbour = NULL;
                }
            }
        }
        point_destroy(currentpoint);
        currentpoint = NULL;
    }
    stack_destroy(stack);
    return NULL;
}

Point *breadth_search_queue(Map *map, Point *input) {
    Point *currentpoint = NULL, *neighbour = NULL;
    Queue *queue = NULL;
    Move m = 0;

    queue=queue_ini(destroy_point_function, copy_point_function, print_point_function);
    queue_insert(queue, (void*) input);

    while (queue_isEmpty(queue) == FALSE) {
        currentpoint = (Point*) queue_extract(queue);
        if (point_getSymbol(currentpoint) != VISITED) {
            if(point_setSymbol(currentpoint, VISITED) == ERROR) return NULL;
            if(map_setPoint(map, currentpoint) == ERROR) return NULL;
            for (m=0; m<4; m++) {
                neighbour = map_getNeighborPoint(map, currentpoint, m);
                if(neighbour) {
                    if (point_isOutput(neighbour) == TRUE) {
                        point_setParent(neighbour, map_getPoint(map, point_getCoordinateX(currentpoint), point_getCoordinateY(currentpoint)));
                        paint_path(map, neighbour);
                        point_destroy(currentpoint);
                        queue_destroy(queue);
                        return neighbour;
                    }
                    if (point_isSpace(neighbour) == TRUE) {
                        point_setParent(neighbour, map_getPoint(map, point_getCoordinateX(currentpoint), point_getCoordinateY(currentpoint)));
                        if(queue_insert(queue, (void*) neighbour) == ERROR) {
                            fprintf(stderr,"Error in queue_insert.\n");
                            return NULL;
                        }
                    }
                    neighbour = NULL;
                }
            }
        }
        point_destroy(currentpoint);
        currentpoint = NULL;
    }
    queue_destroy(queue);
    return NULL;
}
