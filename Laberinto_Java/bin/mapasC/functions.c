/*
 * File: functions.c
 * Authors: Adrian Caballero Orasio, Frederik Mayer
 * Created on 21 de febrero de 2018, 09:00
 */
#include "functions.h"
void destroy_intp_function(void *e) {free((int *) e);}
void *copy_intp_function(const void *e) {
	int *dst = NULL;
	if (!e) return NULL;
	dst = (int *) malloc(sizeof(int));
	*(dst) = *((int *) e);
	return dst;
}
int print_intp_function(FILE *f, const void *e) {
	if (f && e) return fprintf(f, "[%d]\n", *((int *) e));
	return -1;
}
int cmp_intp_function(const void* i1, const void* i2){
	if (*(int*)i1>*(int*)i2) return 1;
	if (*(int*)i1==*(int*)i2) return 0;
	if (*(int*)i1<*(int*)i2) return -1;
	return -2;
}
void destroy_point_function(void *e) {point_destroy((Point *) e);}
void *copy_point_function(const void *e) {return point_copy((Point *) e);}
int print_point_function(FILE *f, const void *e) {return point_print(f, (Point *) e);}
