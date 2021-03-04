/*
 * File: npoint.c
 * Authors: Adrian Caballero Orasio, Frederik Mayer
 * Created on 25 de marzo de 2018, 09:00
 */
#include <string.h>
#include <math.h>
#include "npoint.h"
/* En npoint.c */
struct _NPoint {
	char *name;
	float *coord;
	int dim;
};
NPoint *npoint_ini(int dim) {
	NPoint *p = NULL;
	p = (NPoint *) malloc(sizeof(NPoint));
	if(!p) return NULL;
	p->coord= (float *) malloc(sizeof(float) * dim);
	p->name[0] = '\0';
	return p;
}
void npoint_free(NPoint *p) {
	if (p) {
		free(p->coord);
		p->coord = NULL;
		free(p);
		p = NULL;
	}
}
Status npoint_getCoordinate(const NPoint *p, const int dim, float *v) {
	if (!p) return ERROR;
	v = &p->coord[dim];
	return OK;
}
const char *npoint_getName(const NPoint *p) {
	if (!p) return '\0';
	return p->name;
}
int npoint_getDimensions(const NPoint *p) {
	if (!p) return -1;
	return p->dim;
}
Status npoint_setCoordinate(NPoint *p, const int dim, const float v){
	if (!p || dim < 0) return ERROR;
	p->coord[dim] = v;
	return OK;
}
Status npoint_setName(NPoint *p, const char *name){
	if (!p || !name) return ERROR;
	p->name = strcpy(p->name, name);
	return OK;
}
/*Status npoint_setDimensions(NPoint *p, const int dim) {
	if (!p || dim < 0) return ERROR;
	p->coord = (float *) realloc(p->coord, sizeof(float) * dim);
	p->dim = dim;
	return OK;
}*/
NPoint *npoint_copy(const NPoint *src) {
	NPoint *copy = NULL;
	int i = 0;
	if (!src) return NULL;
	copy = npoint_ini(src->dim);
	if(!copy) return NULL;
	for (i = 0; i < src->dim; i++) {npoint_setCoordinate(copy, i, src->coord[i]);}
	npoint_setName(copy, src->name);
	return copy;
}
int npoint_print(FILE *pf, const NPoint *p) {
	int dim = 0, i = 0, cuenta = 0;
	float coord;
	char *name = NULL;
	if (!pf || !p) return -1;
	dim = npoint_getDimensions(p);
	npoint_getCoordinate(p, 0, &coord);
	fprintf(pf, "[(%f", coord);
	cuenta++;
	for (i = 1; i < dim; i++, cuenta++) {
		npoint_getCoordinate(p, i, &coord);
		fprintf(pf, ", %f", coord);
	}
	name = strcpy(name, p->name);
	fprintf(pf, "): %s]\n", name);
	cuenta++;
	return cuenta;
}
/* Compara p1 con p2 devolviendo un número negativo, cero o positivo según si p1 es menor, igual o mayor que p2 */
/* ¿Hay que compara dimensiones, las coordenadas, la distancia a (0,0,...,0) o ...? */
int npoint_cmp(const NPoint *p1, const NPoint *p2) {
	float aux = 0, valor1 = 0, valor2 = 0;
	int i = 0;
	if (!p1 || !p2) return -2;
	if (p1->dim > p2->dim) return 1;
	if (p1->dim == p2->dim) return 0;
	if (p1->dim < p2->dim) return -1;
	return -2;
	/****/
	if (!p1 || !p2) return -2;
	for (i = 0; i < p1->dim; i++) {
		npoint_getCoordinate(p1, i, &aux);
		valor1 = valor1 + aux*aux;
		aux = 0;
	}
	valor1 = sqrt(valor1);
	for (i = 0; i < p2->dim; i++) {
		npoint_getCoordinate(p2, i, &aux);
		valor2 = valor2 + aux*aux;
		aux = 0;
	}
	valor2 = sqrt(valor2);
	if (valor1 > valor2) return 1;
	if (valor1 == valor2) return 0;
	if (valor1 < valor2) return -1;
	return -2;
}
