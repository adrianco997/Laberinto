/*
 * File:   map.h
 * Author: Adrian Caballero Orasio, Frederik Mayer
 * Created on 7 de febrero de 2018, 09:28
 */
#include <stdlib.h>
#include <string.h>
#include "map.h"
#define MAX 4096
struct _Map {
	int ncols, nrows;
	Point *coord[MAX][MAX];
};
Map *map_ini(){
	Map *pl = NULL;
	int i ,j;
	pl = (Map *) malloc(sizeof(Map));
	if (!pl) return NULL;
	for (i=0; i<MAX; i++){for (j=0; j<MAX; j++){pl->coord[i][j] = NULL;}}
	pl->nrows = -1;
	pl->ncols = -1;
	return pl;
}
void map_destroy(Map *pl){
	int i ,j;
	if (!pl) return;
	for (i=0; i<MAX; i++){for (j=0; j<MAX; j++){if (pl->coord[i][j]){point_destroy(pl->coord[i][j]);}}}
	free(pl);
}
int map_getNrows(const Map *pl){
	if (!pl) return -1;
	return pl->nrows;
}
int map_getNcols(const Map *pl){
	if (!pl) return -1;
	return pl->ncols;
}
Point *map_getInput(const Map *pl){
	int x = 0, y = 0, nrows, ncols;
	if (!pl) return NULL;
	nrows = map_getNrows(pl);
	ncols = map_getNcols(pl);
	for (x=0; x<ncols; x++){for (y=0; y<nrows; y++){if (map_getPoint(pl, x, y)){if (point_isInput(map_getPoint(pl, x, y)) == TRUE) return map_getPoint(pl, x, y);}}}
	return NULL;
}
Point *map_getOutput(const Map *pl){
	int x = 0, y = 0, nrows, ncols;
	if (!pl) return NULL;
	nrows = map_getNrows(pl);
	ncols = map_getNcols(pl);
	for (x=0; x<ncols; x++){for (y=0; y<nrows; y++){if (map_getPoint(pl, x, y)){if (point_isOutput(map_getPoint(pl, x, y)) == TRUE) return map_getPoint(pl, x, y);}}}
	return NULL;
}
Point *map_getPoint(const Map *pl, int x, int y){
	if (!pl) return NULL;
	if(x<0 || x>map_getNcols(pl) || y<0 || y>map_getNrows(pl)) return NULL;
	return pl->coord[x][y];
}
Point *map_getNeighborPoint(const Map *pl, const Point *p, const Move mov){
	int x, y, maxX, maxY;
	if (!pl || !p || mov>4 || mov<0) return NULL;
	x = point_getCoordinateX(p);
	y = point_getCoordinateY(p);
	maxX = map_getNcols(pl) - 1;
	maxY = map_getNrows(pl) - 1;
	/*printf("map_getNeighborPoint called with arguments:\n");printf("Starting point:\n");point_print(stdout,p);printf("Movement:%d\n", mov);*/
	if (mov == RIGHT){if(x < maxX){if((pl->coord[x+1][y])) {x++;} else {printf("coord[%d+1][%d] no existe\n", x, y);}} else {printf("coord[%d+1][%d] x es mayor que Nrows=%d\n", x, y, maxX);}}
	else if(mov == UP){if(y > 0){if((pl->coord[x][y-1])) {y--;} else {printf("coord[%d][%d-1] no existe\n", x, y);}} else {printf("coord[%d][%d-1] y es menor que 0\n", x, y);}}
	else if(mov == LEFT){if(x > 0){if((pl->coord[x-1][y])) {x--;} else {printf("coord[%d-1][%d] no existe\n", x, y);}} else {printf("coord[%d-1][%d] x es menor que 0\n", x, y);}}
	else if(mov == DOWN){if(y < maxY){if((pl->coord[x][y+1])) {y++;} else {printf("coord[%d][%d+1] no existe\n", x, y);}} else {printf("coord[%d][%d+1] y es mayor que Ncols=%d\n", x, y, maxY);}}
	/* s = point_getSymbol(map_getPoint(pl, x, y));
	np = point_ini(x, y, s);
	return np; */
	return map_getPoint(pl, x, y);
}
Status map_setSize(Map *pl, int nrow, int ncol){
	if (!pl || nrow<=0 || ncol<=0) return ERROR;
	pl->nrows = nrow;
	pl->ncols = ncol;
	return OK;
}
Status map_setPoint(Map *pl, const Point *p){
	int x, y;
	if (!pl || !p) {
		fprintf(stderr, "error en map_setPoint\n");
		return ERROR;
	}
	x = point_getCoordinateX(p);
	y = point_getCoordinateY(p);
	if (x>map_getNcols(pl) || x<0 || y>map_getNrows(pl) || y<0) return ERROR;
	if (point_getSymbol(p) == INPUT && map_getInput(pl)) point_setSymbol(map_getInput(pl), SPACE);
	if (point_getSymbol(p) == OUTPUT && map_getOutput(pl)) point_setSymbol(map_getOutput(pl), SPACE);
	if(map_getPoint(pl, x, y)){  point_destroy(map_getPoint(pl, x, y));}
	pl->coord[x][y] = point_copy(p);
	return OK;
}
int map_print(FILE *file, const Map *pl){
	int cuenta = 0, i, j, ymax, xmax;
	if (!file || !pl) return -1;
	ymax = map_getNrows(pl);
	xmax = map_getNcols(pl);
	for (i=0; i<ymax; i++) {
		for (j=0; j<xmax; j++) {
			fprintf(file, "%c", point_getSymbol(map_getPoint(pl, j, i)));
			cuenta++;
		}
		fprintf(file, "\n");
	}
	return cuenta;
}
