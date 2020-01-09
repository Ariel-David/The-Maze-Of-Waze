package elements;

import java.io.Serializable;


public class Edge implements edge_data,Serializable {
	int src;
	int dest;
	double Weight;
	String info;
	int tag;
	
	/**
	 * Default constructor.
	 */
	public Edge() {
		this.src = 0;
		this.dest = 0;
		this.tag = 0;
		this.info = "";
		this.Weight = 0;
	}
	/**
	 * Full constructor.
	 * @param src - The id of the source node of this edge.
	 * @param dest - The id of the destination node of this edge.
	 * @param tag - temporal data (aka color: e,g, white, gray, black).
	 * @param weigth - the weight of this edge (positive value).
	 * @param info -the remark (meta data) associated with this edge.
	 */
	public Edge(int src, int dest, double weight, String info , int tag) {
		this.src = src;
		this.dest = dest;
		this.tag = tag;
		this.info = info;
		this.Weight = weight;
	}
	/**
	 * Copy constructor.
	 * @param other - a copied version of this edge.
	 */
	public Edge(Edge e) {
		this.src = e.src;
		this.dest = e.dest;
		this.tag = e.tag;
		this.info = e.info;
		this.Weight = e.Weight;
	}
	
	public Edge(int src, int dest, double weight) {
		this.src = src;
		this.dest = dest;
		this.Weight = weight;
	}

	@Override
	public int getSrc() {
		return this.src;
	}

	@Override
	public int getDest() {
		return this.dest;
	}

	@Override
	public double getWeight() {
		return this.Weight;
	}

	@Override
	public String getInfo() {
		return this.info;
	}

	@Override
	public void setInfo(String s) {
		this.info = s;
	}

	@Override
	public int getTag() {
		return this.tag;
	}

	@Override
	public void setTag(int t) {
		this.tag = t;
	}

}
