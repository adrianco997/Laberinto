package mapasC;

import java.io.File;

public class NPoint {
	private char name;
	private float coord[];
	private int dim;

	public NPoint(int dim) {
		this.dim = dim;
		this.coord = new float[dim];/* (float *) malloc(sizeof(float) * dim) */
		this.name = '\0';
	}

	public static void free(NPoint p) {
		if (p != null) {
			p.coord = null;
			p = null;
		}
	}

	public float getCoordinate(int dim) {
		return this.coord[dim];
	}

	public char getName() {
		return this.name;
	}

	public int getDimensions() {
		return this.dim;
	}

	public void getDimensions(int dim) {
		this.dim = dim;
	}

	public void setCoordinate(int dim, float coord) {
		this.coord[dim] = coord;
	}

	public void setName(char name) {
		this.name = name;
	}

	public void setDimensions(int dim) {
		this.coord = new float[dim];
	}

	public NPoint copy() {
		NPoint copy = null;
		int i = 0;
		copy = new NPoint(this.dim);
		for (i = 0; i < this.dim; i++) {
			copy.setCoordinate(i, this.coord[i]);
		}
		copy.setName(this.name);
		return copy;
	}

//	public int print(File pf) {int dim = 0, i = 0, cuenta = 0;float coord;char *name = NULL;if (!pf || !p) return -1;dim = getDimensions(p);getCoordinate(p, 0, &coord);fprintf(pf, "[(%f", coord);cuenta++;for (i = 1; i < dim; i++, cuenta++) {getCoordinate(p, i, &coord);fprintf(pf, ", %f", coord);}name = strcpy(name, p->name);fprintf(pf, "): %s]\n", name);cuenta++;return cuenta;}

	public int print() {
		int dim = getDimensions(), cuenta = 0;
		float coord = getCoordinate(0);
		char name = this.name;
		System.out.printf("[(%f", coord);
		cuenta++;
		for (int i = 1; i < dim; i++, cuenta++) {
			coord = getCoordinate(i);
			System.out.printf(", %f", coord);
		}
		System.out.printf("): %s]\n", name);
		cuenta++;
		return cuenta;
	}

	public String printS() {
		int dim = getDimensions(), cuenta = 0;
		float coord = getCoordinate(0);
		char name = this.name;
		String txt = String.format("[(%f", coord);
		cuenta++;
		for (int i = 1; i < dim; i++, cuenta++) {
			coord = getCoordinate(i);
			txt += String.format(", %f", coord);
		}
		cuenta++;
		txt += String.format("): %s] %s\n", name, cuenta);
		return txt;
	}

	/*
	 * Compara p1 con p2 devolviendo un numero negativo, cero o positivo segun si
	 * p1 es menor, igual o mayor que p2
	 */
	/*
	 * ¿Hay que compara dimensiones, las coordenadas, la distancia a (0,0,...,0) o
	 * ...?
	 */
	public int cmp(NPoint p2) {
		float aux = 0, valor1 = 0, valor2 = 0;

		if (this == null || p2 == null){return -2;}
		else if (this.dim > p2.dim){return 1;}
		else if (this.dim == p2.dim){return 0;}
		else if (this.dim < p2.dim){return -1;}
//	    return -2;
		/****/
		if (this == null || p2 == null){return -2;}
		for (int i = 0; i < dim; i++) {
			aux = getCoordinate(i);
			valor1 = valor1 + aux * aux;
			aux = 0;
		}
		valor1 = (float) Math.sqrt(valor1);
		for (int i = 0; i < p2.dim; i++) {
			aux = p2.getCoordinate(i);
			valor2 = valor2 + aux * aux;
			aux = 0;
		}
		valor2 = (float) Math.sqrt(valor2);
		if (valor1 > valor2){return 1;}
		else if (valor1 == valor2){return 0;}
		else if (valor1 < valor2){return -1;}
		else {return -2;}
	}
}
