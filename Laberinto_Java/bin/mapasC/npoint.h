/*
 * File: npoint.h
 * Authors: Adrian Caballero Orasio, Frederik Mayer
 * Created on 25 de marzo de 2018, 09:00
 */

#ifndef NPOINT_H
#define NPOINT_H

#include <stdio.h>
#include <stdlib.h>
#include "types.h"

typedef struct _NPoint NPoint;

/* Reserva memoria para un punto que tenga dim dimensiones */
NPoint *npoint_ini(int dim);
/* Libera el punto */
void npoint_free(NPoint *p);

/* Obtiene la coordenada dim-ésima de p y la almacena en v (que se recibe como puntero para que su contenido se pueda modificar dentro de la función), devolviendo OK si todo va bien */
Status npoint_getCoordinate(const NPoint *p, const int dim, float *v);
/* Devuelve el nombre almacenado en p */
const char *npoint_getName(const NPoint *p);
/* Devuelve el número de dimensiones de p */
int npoint_getDimensions(const NPoint *p);

/* Asigna el valor v a la coordenada dim-ésima al punto p */
Status npoint_setCoordinate(NPoint *p, const int dim, const float v);
/* Asigna el nombre name al punto p, teniendo en cuenta que el punto podría no tener memoria en nombre o podría tener memoria insuficiente o extra */
Status npoint_setName(NPoint *p, const char *name);
/* Copia un punto reservando memoria para dicho punto y todos sus atributos, copiando sus valores */
NPoint *npoint_copy(const NPoint *src);
/* Imprime un punto p en el fichero pf, devolviendo el número de caracteres impresos */
int npoint_print(FILE *pf, const NPoint *p);
/* Compara p1 con p2 devolviendo un número negativo, cero o positivo según si p1 es menor, igual o mayor que p2 */
int npoint_cmp(const NPoint *p1, const NPoint *p2);

#endif /* NPOINT_H */
