/*
 * File: point.c
 * Authors: Adrian Caballero Orasio, Frederik Mayer
 * Created on 7 de febrero de 2018, 09:28
 */

#include <stdlib.h>
#include <string.h>
#include "point.h"

struct _Point {
    int x, y;
    char symbol;
    Point *parent;
};

Point *point_ini(int x, int y, char s) {
    Point *p = NULL;

    p = (Point *) malloc(sizeof(Point));
    if(!p) return NULL;
    p->x = x;
    p->y = y;
    p->symbol = s;
    p->parent = NULL;
    return p;
}
void point_destroy(Point *p) {
    if (p) free(p);
}

int point_getCoordinateX(const Point *p) {
    if (!p) return -1;
    return p->x;
}
int point_getCoordinateY(const Point *p) {
    if (!p) return -1;
    return p->y;
}
char point_getSymbol(const Point *p) {
    if (!p) return ERRORCHAR;
    return p->symbol;
}
Point *point_getParent(const Point *p) {
    if (!p) return NULL;
    return p->parent;
}

Status point_setCoordinateX(Point *p, const int x) {
    if (!p || x<0) return ERROR;
    p->x = x;
    return OK;
}
Status point_setCoordinateY(Point *p, const int y) {
    if (!p || y<0) return ERROR;
    p->y = y;
    return OK;
}
Status point_setSymbol(Point *p, const char s) {
    if (!p || sizeof(s) != 1) return ERROR;
    p->symbol = s;
    return OK;
}
Status point_setParent(Point *p, Point *parent) {
    if (!p || !parent) return ERROR;
    p->parent = parent;
    return OK;
}

Bool point_isInput(Point *p) {
    if (p->symbol != INPUT) return FALSE;
    return TRUE;
}
Bool point_isOutput(Point *p) {
    if (p->symbol != OUTPUT) return FALSE;
    return TRUE;
}
Bool point_isBarrier(Point *p) {
    if (p->symbol != BARRIER) return FALSE;
    return TRUE;
}
Bool point_isSpace(Point *p) {
    if (p->symbol != SPACE) return FALSE;
    return TRUE;
}

Bool point_equals(const Point *p1, const Point *p2) {
    if (!p1 || !p2 || (point_getCoordinateX(p1) != point_getCoordinateX(p2) )|| (point_getCoordinateY(p1) != point_getCoordinateY(p2)) || (point_getSymbol(p1) != point_getSymbol(p2))) return FALSE;
    return TRUE;
}
Point *point_copy(const Point *p) {
    Point *cp = NULL, *parent = NULL;
    int x, y;
    char s;

    if (!p) return NULL;
    x = point_getCoordinateX(p);
    y = point_getCoordinateY(p);
    s = point_getSymbol(p);
    parent = point_getParent(p);
    cp = point_ini(x, y, s);
    if (p) point_setParent(cp, parent);
    return cp;
}

int point_print(FILE *file, const Point *p) {
    int x, y, cuenta = 0;
    char s;

    if (!file || !p) return -1;
    x = point_getCoordinateX(p);
    y = point_getCoordinateY(p);
    s = point_getSymbol(p);
    fprintf(file, "[(%d, %d): %c]\n", x, y, s);
    do{
        x = x / 10;
        cuenta++;
    }while(x != 0);
    do{
        y = y / 10;
        cuenta++;
    }while(y != 0);
    cuenta = cuenta + 8;
    /* fprintf(file, "With parent:");
    if (point_getParent(p)) cuenta += point_print(file, point_getParent(p));
    else fprintf(file, "None.\n"); */
    return cuenta;
}
