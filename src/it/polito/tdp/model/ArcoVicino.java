package it.polito.tdp.model;

public class ArcoVicino implements Comparable<ArcoVicino> {
	private int source;
	private int target;
	private double peso;
	
	public ArcoVicino(int source, int target, double peso) {
		this.source = source;
		this.target = target;
		this.peso = peso;
	}
	
	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + source;
		result = prime * result + target;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArcoVicino other = (ArcoVicino) obj;
		if (source != other.source)
			return false;
		if (target != other.target)
			return false;
		return true;
	}

	@Override
	public int compareTo(ArcoVicino o) {
		return (int)this.peso -(int)o.peso;
	}

	@Override
	public String toString() {
		return "distretto "+target+" distanza: "+peso;
	}
	
	

}
