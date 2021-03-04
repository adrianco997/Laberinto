/*
 * File:   types.h
 * Author: Profesores de PROG2
 */

#ifndef TYPES_H
#define TYPES_H
/* Valores posibles Booleanos */
typedef enum {FALSE = 0, TRUE = 1} Bool;
/* Valores posibles sobre el estado de la funcion */
typedef enum {ERROR = 0, OK = 1} Status;
/* Movimientos posibles en un mapa */
typedef enum {RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3, STAY = 4} Move;

#endif /* TYPES_H */
