/*
 * File: map_solver.c
 * Authors: Adrian Caballero Orasio, Frederik Mayer
 * Created on 21 de marzo de 2018, 09:28
 */
#include "map_solver.h"
/*privates*/
Status map_read (FILE *pf, Map *pl);
Status map_to_stack(Map* map, Stack* stack);
Status map_to_queue(Map *map, Queue *queue);
int paint_path(Map* map, Point* output);
int deep_search_stack (Map* map, Point* input, const Move strat[4]);
int breadth_search_queue(Map *map, Point *input, const Move strat[4]);
Point* deep_search_rec(Map *map, Point *input, const Move strat[4], Bool compare);
void mapsolver_run(const char* map_file, const Move strat[][4], const int num_strategies){
	int pathlength=0;
	for (int i=0; i<num_strategies; i++){
		fprintf(stdout, "ESTRATEGIA %d%d%d%d\n", strat[i][0], strat[i][1], strat[i][2], strat[i][3]);
		fprintf(stdout, "Con pila:-------------------------------------------------------------------------------------------------\n");
		pathlength=mapsolver_stack(map_file, strat[i]);
		if (pathlength==-1){fprintf(stdout, "SALIDA NO ENCONTRADA.\n");}
		else{fprintf(stdout, "SALIDA ENCONTRADA, longitud del camino con pila y estrategia %d%d%d%d: %d unidades.\n",strat[i][0], strat[i][1], strat[i][2], strat[i][3], pathlength);}
		printf("----------------------------------------------------------------------------------------------------------\n");
		fprintf(stdout, "Con cola:-------------------------------------------------------------------------------------------------\n");
		pathlength=mapsolver_queue(map_file, strat[i]);
		if (pathlength==-1){fprintf(stdout, "SALIDA NO ENCONTRADA.\n");}
		else{fprintf(stdout, "SALIDA ENCONTRADA, longitud del camino con cola y estrategia %d%d%d%d: %d unidades.\n",strat[i][0], strat[i][1], strat[i][2], strat[i][3], pathlength);}
		printf("----------------------------------------------------------------------------------------------------------\n");
		fprintf(stdout, "Recursivo:------------------------------------------------------------------------------------------------\n");
		pathlength=mapsolver_recursive(map_file, strat[i], TRUE);
		if (pathlength==-1){fprintf(stdout, "SALIDA NO ENCONTRADA.\n");}
		else{fprintf(stdout, "SALIDA ENCONTRADA, longitud del camino con algoritmo recursivo y estrategia %d%d%d%d: %d unidades.\n",strat[i][0], strat[i][1], strat[i][2], strat[i][3], pathlength);}
		printf("----------------------------------------------------------------------------------------------------------\n");
	}
}
int mapsolver_stack(const char* map_file, const Move strat[4]){
	FILE *file;
	Map *map=NULL;
	int pathlength=0;
	map=map_ini();
	file = fopen(map_file,"r");
	if(!file||!map){return -1;}
	if (map_read(file, map) == ERROR) {
		fprintf(stderr, "Error en map_read.\n");
		return -1;
	}
	printf("Map:\n");
	map_print(stdout,map);
	printf("Input:");
	point_print(stdout,map_getInput(map));
	pathlength = deep_search_stack(map, map_getInput(map), strat);
	if(pathlength!=-1){fprintf(stdout, "Output found, length of path:%d\n", pathlength);}
	else{fprintf(stdout, "No path found.\n");}
	fprintf(stdout, "Final map:\n");
	map_print(stdout, map);
	map_destroy(map);
	fclose(file);
	return pathlength;
}
int mapsolver_queue(const char* map_file, const Move strat[4]){
	FILE *file;
	Map *map=NULL;
	int pathlength=0;
	map=map_ini();
	file = fopen(map_file,"r");
	if(!file||!map){return -1;}
	if (map_read(file, map) == ERROR) {
		fprintf(stderr, "Error en map_read.\n");
		return -1;
	}
	printf("Map:\n");
	map_print(stdout,map);
	printf("Input:");
	point_print(stdout,map_getInput(map));
	pathlength = breadth_search_queue(map, map_getInput(map), strat);
	if(pathlength!=-1){fprintf(stdout, "Output found, length of path:%d\n", pathlength);}
	else{fprintf(stdout, "No path found.\n");}
	fprintf(stdout, "Final map:\n");	map_print(stdout, map);
	map_destroy(map);
	fclose(file);
	return pathlength;
}
int mapsolver_recursive(const char* map_file, const Move strat[4], Bool compare){
	FILE *file;
	Map *map=NULL;
	Point* out=NULL;
	int pathlength=0;
	map=map_ini();
	file = fopen(map_file,"r");
	if(!file||!map){return -1;}
	if (map_read(file, map) == ERROR) {
		fprintf(stderr, "Error en map_read.\n");
		return -1;
	}
	printf("Map:\n");
	map_print(stdout,map);
	printf("Input:");
	point_print(stdout,map_getInput(map));
	out=deep_search_rec(map, map_getInput(map), strat, compare);
	pathlength=paint_path(map,out);
	printf("Output is in point:\n");
	point_print(stdout, out);
	/*if(pathlength!=-1){fprintf(stdout, "Output found, length of path:%d\n", pathlength);}
	else{fprintf(stdout, "No path found.\n");}*/
	fprintf(stdout, "Final map:\n");
	map_print(stdout, map);
	map_destroy(map);
	fclose(file);
	return pathlength;
}
Status map_read (FILE *pf, Map *pl) {
	char buff[MAX];
	char caracter;
	int x, y, ncols, nrows;/* int indice; */
	Point *temp=NULL;
	Status st = ERROR;
	if (pl==NULL || pf==NULL) return ERROR;
	/*creamos punto que se utiliza como buffer*/
	temp = point_ini(0, 0, ERRORCHAR);
	if (temp==NULL) return ERROR;
	/* asignamos dimensión al mapa */
	fgets(buff, MAX, pf);
	sscanf(buff, "%d %d", &nrows, &ncols);
	st = map_setSize (pl, nrows, ncols);
	if (st == ERROR) return ERROR;
	/* leemos el fichero linea a linea */
	for(y=0; y < nrows; y++) {
		fgets(buff, MAX, pf);
		for (x=0; x < ncols; x++) {
			caracter=buff[x];
			/* ajustamos los atributos del punto leído (falta añadir control de errores) */
			if (point_setCoordinateX(temp, x)==ERROR) printf("Error in map_read.");
			if (point_setCoordinateY(temp, y)==ERROR) printf("Error in map_read.");
			if (point_setSymbol(temp, caracter)==ERROR) printf("Error in map_read.");
			/* insertamos el punto en el mapa (falta añadir control de errores) */
			if (map_setPoint(pl, temp)==ERROR) printf("Error in map_read.");
		}
	}
	/* libera recursos */
	point_destroy(temp);
	/* no cerramos el fichero ya que lo han abierto fuera */
	return OK;
}
Status map_to_stack(Map* map, Stack* stack){
	int x=0,y=0;
	Point* current_point=NULL;
	for (y=0; y<map_getNrows(map); y++){
		for (x=0; x<map_getNcols(map); x++){
			if (stack_isFull(stack)){
				fprintf(stderr, "Stack is too small for map.\n");
				return ERROR;
			}
			current_point=point_copy(map_getPoint(map, x, y));
			if(stack_push(stack, (void*) current_point)==ERROR){
				fprintf(stderr, "Error while adding point to stack.\n");
				return ERROR;
			}
			point_destroy(current_point);
			current_point=NULL;
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
		/*point_print(stdout, point_getParent(current_point));
		printf("is parent of ");
		point_print(stdout, current_point);*/
		if(point_getCoordinateX(point_getParent(current_point))>point_getCoordinateX(current_point)){point_setSymbol(point_getParent(current_point), '<');}
		else if(point_getCoordinateX(point_getParent(current_point))<point_getCoordinateX(current_point)){point_setSymbol(point_getParent(current_point), '>');}
		else if(point_getCoordinateY(point_getParent(current_point))>point_getCoordinateY(current_point)){point_setSymbol(point_getParent(current_point), '^');}
		else if(point_getCoordinateY(point_getParent(current_point))<point_getCoordinateY(current_point)){point_setSymbol(point_getParent(current_point), 'v');}
		else{point_setSymbol(point_getParent(current_point), 'O');}
		current_point=point_getParent(current_point);
		pathlength++;
	}
	point_setSymbol(current_point, 'i');
	point_setSymbol(output, 'o');
	return pathlength;
}
int deep_search_stack (Map* map, Point* input, const Move strat[4]){
	Stack* stack=NULL;
	Point* currentpoint=NULL;
	Point* neighbor=NULL;
	int m=0;
	stack=stack_ini(destroy_point_function, copy_point_function, print_point_function);
	stack_push(stack, (void*) input);
	while (stack_isEmpty(stack)==FALSE){
		currentpoint=(Point*) stack_pop(stack);
		if (point_getSymbol(currentpoint)!=VISITED){
			/*if(point_setSymbol(currentpoint, VISITED)==ERROR){return -1;}
			if(map_setPoint(map, currentpoint)==ERROR){return -1;}*/
			if(point_setSymbol(map_getPoint(map, point_getCoordinateX(currentpoint),point_getCoordinateY(currentpoint)),VISITED)==ERROR){return -1;}
			/*printf("Strategy:%d,%d,%d,%d\n",strat[0], strat[1],strat[2],strat[3]);*/
			for (m=3; m>=0; m--){ /*stack is LIFO-> movements with higher preference have to be checked last*/
				neighbor=map_getNeighborPoint(map, currentpoint, strat[m]);
				if(neighbor){
					if (point_isOutput(neighbor)==TRUE){
						point_setParent(neighbor, map_getPoint(map, point_getCoordinateX(currentpoint), point_getCoordinateY(currentpoint)));
						point_destroy(currentpoint);
						stack_destroy(stack);
						return paint_path(map, neighbor);
					}
					if (point_isSpace(neighbor)==TRUE){
						point_setParent(neighbor, map_getPoint(map,point_getCoordinateX(currentpoint), point_getCoordinateY(currentpoint)));
						if(stack_push(stack, (void*) neighbor)==ERROR){
							fprintf(stderr,"Error in stack_push.\n");
							return -1;
						}
					}
					neighbor=NULL;
				}
			}
		}
		point_destroy(currentpoint);
		currentpoint=NULL;
	}
	stack_destroy(stack);
	return -1;
}
int breadth_search_queue(Map *map, Point *input, const Move strat[4]) {
	Point *currentpoint = NULL, *neighbor = NULL;
	Queue *queue = NULL;
	int m = 0;
	queue=queue_ini(destroy_point_function, copy_point_function, print_point_function);
	queue_insert(queue, (void*) input);
	while (queue_isEmpty(queue) == FALSE) {
		currentpoint = (Point*) queue_extract(queue);
		if (point_getSymbol(currentpoint) != VISITED) {
			/*if(point_setSymbol(currentpoint, VISITED) == ERROR) return -1;
			if(map_setPoint(map, currentpoint) == ERROR) return -1;*/
			if(point_setSymbol(map_getPoint(map, point_getCoordinateX(currentpoint),point_getCoordinateY(currentpoint)),VISITED)==ERROR){return -1;}
			for (m=0; m<4; m++) {
				/*queue is FIFO-> movements with higher preference have to be checked first*/
				neighbor = map_getNeighborPoint(map, currentpoint, strat[m]);
				if(neighbor) {
					if (point_isOutput(neighbor) == TRUE) {
						point_setParent(neighbor, map_getPoint(map, point_getCoordinateX(currentpoint), point_getCoordinateY(currentpoint)));
						point_destroy(currentpoint);
						queue_destroy(queue);
						return paint_path(map, neighbor);
					}
					if (point_isSpace(neighbor) == TRUE) {
						point_setParent(neighbor, map_getPoint(map, point_getCoordinateX(currentpoint), point_getCoordinateY(currentpoint)));
						if(queue_insert(queue, (void*) neighbor) == ERROR) {
							fprintf(stderr,"Error in queue_insert.\n");
							return -1;
						}
					}
					neighbor = NULL;
				}
			}
		}
		point_destroy(currentpoint);
		currentpoint = NULL;
	}
	queue_destroy(queue);
	return -1;
}
Point* deep_search_rec(Map *map, Point *input, const Move strat[4], Bool compare){
	int i=0;
	Point* neighbor;
	Point* auxpoint;
	if(compare==FALSE){
		printf("Current map:\n");
		map_print(stdout,map);
	}
	if (point_isOutput(input)==TRUE){return input;}
	if (input==NULL){return NULL;}
	point_setSymbol(input, VISITED);
	for (i=0; i<4; i++){
		neighbor=map_getNeighborPoint(map, input, strat[i]);
		if (neighbor!=NULL){
			if (point_getSymbol(neighbor) != VISITED && point_getSymbol(neighbor) != VISITING && point_getSymbol(neighbor) != BARRIER) {
				point_setParent(neighbor, map_getPoint(map, point_getCoordinateX(input), point_getCoordinateY(input)));
				if (point_isOutput(neighbor)==FALSE){point_setSymbol(neighbor, VISITING);}
				auxpoint=deep_search_rec(map,neighbor,strat, compare);
				if(auxpoint!=NULL){
					if (compare==FALSE){fprintf(stdout, "Solution!\n");}
					return auxpoint;
				}
			}
		}
	}
	return NULL;
}
