package elements;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import utils.Point3D;

public class Node implements node_data, Serializable{
	private int key;
	private double Weight;
	private int tag;
	private Point3D location;
	String info;	
	public	Map<Integer, edge_data> edges= new HashMap<Integer,edge_data>();
	public List<Node> ShortestPath = new LinkedList<>();
	
	/**
	 * Default constructor.
	 */
	public Node(){
		this.key = 0;
		this.Weight = 0;
		this.tag = 0;
		this.location = null;
		this.info = "";

	}
	/**
	 * Full constructor that set all the params.
	 * @param key - the key (id) associated with this node.
	 * @param location - the location (of applicable) of this node.
	 * @param Weight - the weight associated with this node.
	 * @param info - the remark (meta data) associated with this node.
	 * @param tag - Temporal data (aka color: e,g, white, gray, black).
	 */
	public Node(int key , double Weight , int tag, Point3D location,String info){
		this.key = key;
		this.Weight = Weight;
		this.tag = tag;
		this.location = location;
		this.info = info;
	}
	/**
	 * Copy constructor.
	 * @param other - a copied version of this node.
	 */
	public Node(Node n) {
		this.key = n.key;
		this.Weight = n.Weight;
		this.tag = n.tag;
		this.location = n.location;
		this.info = n.info;	
	}

	public Node(Integer key) {
		this.key = key;
		this.Weight = 0;
		this.tag = 0;
		this.location = null;
		this.info = "";
	}

	public Node(int key, Point3D p) {
		this.key = key;
		this.location = p;
	}

	public void setKey(int key) {
		this.key = key;
	}

	@Override
	public int getKey() {
		return this.key;
	}

	@Override
	public Point3D getLocation() {
		return this.location;
	}

	@Override
	public void setLocation(Point3D p) {
		this.location = new Point3D(p);
	}

	@Override
	public double getWeight() {
		return this.Weight;
	}

	@Override
	public void setWeight(double w) {
		this.Weight = w;

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
	
	/**
	 * An iterator that goes over the neighbours of specific node.
	 * @return the iterator.
	 */
	public Iterator<Node> iteretor() {
		return this.iteretor();
	}
	
	/**
	 * To print the key of the node
	 */
	public String keytoString() {
		return ""+this.getKey();
	}

}
