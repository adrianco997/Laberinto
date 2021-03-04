/**
 * File: map_solver.h
 * Authors: Adrian Caballero Orasio, Frederik Mayer
 * Created on 21 de marzo de 2018, 09:28
 */

#ifndef MAP_SOLVER_H
#define MAP_SOLVER_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "types.h"
#include "point.h"
#include "map.h"
#include "stack_fp.h"
#include "functions.h"
#include "queue.h"

/*tamano maximo de un string*/
#define MAX 4096
/*simbolo para un espacio visitado*/
#define VISITED 'V'
/*simbolo para un espacio que el programa esta visitando*/
#define VISITING 'v'

/**llama a mapsolver_stack y mapsolver_queue con una matriz de movimientos*/
void mapsolver_run(const char* map_file, const Move strat[][4], const int num_strategies);
/**resolve un mapa usando una pila.*/
int mapsolver_stack(const char* map_file, const Move strat[4]);
/**resolve un mapa usando una cola.*/
int mapsolver_queue(const char* map_file, const Move strat[4]);
/**resolve un mapa usando un algoritmo recursivo.*/
int mapsolver_recursive(const char* map_file, const Move strat[4], Bool compare);

#endif
