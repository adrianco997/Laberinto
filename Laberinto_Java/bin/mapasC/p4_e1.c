/*
 * File: p4_e1.c
 * Authors: Adrian Caballero Orasio, Frederik Mayer
 * Created on 21 de marzo de 2018, 09:28
 */

#include "types.h"
#include "map_solver.h"

int main(int argc, char *argv[]) {
   const Move strat[4][4]={{UP, DOWN, LEFT, RIGHT},
                          {RIGHT, UP, DOWN, LEFT},
                          {LEFT, RIGHT, UP, DOWN},
                          {DOWN, LEFT, RIGHT, UP}};

   const Move strat1[4]={RIGHT, LEFT, UP, DOWN};
   int pathlength=0, i=0;

   if (argc < 2) {
       printf("Falta el fichero de datos.\n");
       return -1;
   }

   fprintf(stdout, "->Recursivo:\n");
   pathlength=mapsolver_recursive(argv[1], strat1, FALSE);

   if (pathlength==-1) fprintf(stdout, "SALIDA NO ENCONTRADA.\n");
   else fprintf(stdout, "SALIDA ENCONTRADA, longitud del camino con algoritmo recursivo y estrategia %d%d%d%d: %d unidades.\n", strat[i][0], strat[i][1], strat[i][2], strat[i][3], pathlength);
   printf ( "----------------------------------------------------------------------------------------------------------\n"
            "-------------------Part 1 of exercise 1 completed. Comparison of the 3 search algorithms:-----------------\n"
            "----------------------------------------------------------------------------------------------------------\n");

   mapsolver_run(argv[1], strat, 4);
   return 0;
 }
