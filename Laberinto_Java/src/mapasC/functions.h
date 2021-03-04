/*
 * File: functions.h
 * Authors: Adrian Caballero Orasio, Frederik Mayer
 * Created on 21 de febrero de 2018, 09:00
 */

#ifndef FUNCTIONS_H
#define FUNCTIONS_H

#include <stdio.h>
#include <stdlib.h>
#include "types.h"
#include "point.h"

/* Destruir puntero a int */
void destroy_intp_function(void *e);
/* Copiar puntero a int */
void *copy_intp_function(const void *e);
/* Imprimir contenido de puntero a int */
int print_intp_function(FILE *f, const void *e);
/* Devuelve -1 si *i1<*i2, 0 si son iguales y +1 si *i1>*i2*/
int cmp_intp_function(const void* i1, const void* i2);

/* Destruir puntero a punto */
void destroy_point_function(void *e);
/* Copiar puntero a punto */
void *copy_point_function(const void *e);
/* Imprimir contenido de puntero a punto */
int print_point_function(FILE *f, const void *e);

#endif /* FUNCTIONS_H */
