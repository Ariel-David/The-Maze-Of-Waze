package gui;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import java.util.Iterator;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import elements.edge_data;
import dataStructure.graph;
import elements.node_data;
import utils.StdDraw;

public class Graph_GUI implements Serializable{
	private DGraph graph;
	private Graph_Algo algoGraph;

	/**
	 * Default constructor
	 */
	public Graph_GUI() {
		algoGraph = new Graph_Algo();
		graph = new DGraph();
		StdDraw.setGui(this);
	}

	public Graph_Algo getAlgoGraph(){
		return algoGraph;
	}

	public DGraph getGraph(){
		return graph;
	}

	/**
	 * A copy constructor
	 * @param g - the given graph.
	 */
	public Graph_GUI(graph g){
		this.graph = (DGraph) g;
		algoGraph = new Graph_Algo();
		algoGraph.init(this.graph);
		StdDraw.setGui(this);
	}

	/**
	 * Initialize the Graph_GUI from given graph.
	 * @param gra - the given graph.
	 */
	public void init(graph gra) {
		this.graph = (DGraph) gra;
		this.algoGraph.graph = gra;
	}

	/**
	 * Initialize the Graph from a string.
	 * @param name - the given string, represent a saved graph.
	 */
	public void init(String name) {
		this.algoGraph.init(name);
		this.graph = (DGraph) algoGraph.graph;
		drawGraph();
	}

	/**
	 * Draw the graph according to this methods the Graph from a string.
	 */
	public void drawGraph() {
		setScale();
		drawEdges();
		drawVertex();
		printKey();
		drawDirection();
		//drawEdgesWeight();
	}
	
	public double getXmin() {
		double x_min = Double.MAX_VALUE;
		Iterator<node_data> iter = this.graph.getV().iterator();
		while(iter.hasNext()) {
			node_data currentNode = iter.next();
			if(currentNode.getLocation().x() < x_min) {
				x_min = currentNode.getLocation().x();
			}
		}
		return x_min;
	}
	public double getXmax() {
		double x_max = Double.MIN_VALUE;
		Iterator<node_data> iter = this.graph.getV().iterator();
		while(iter.hasNext()) {
			node_data currentNode = iter.next();
			if(currentNode.getLocation().x() > x_max) {
				x_max = currentNode.getLocation().x();
			}
		}
		return x_max;
	}

	public double getYmin() {
		double y_min = Double.MAX_VALUE;
		Iterator<node_data> iter = this.graph.getV().iterator();
		while(iter.hasNext()) {
			node_data currentNode = iter.next();
			if(currentNode.getLocation().y() < y_min) {
				y_min =  currentNode.getLocation().y();
			}
		}
		return y_min;
	}

	public double getYmax() {
		double y_max = Double.MIN_VALUE;
		Iterator<node_data> iter = this.graph.getV().iterator();
		while(iter.hasNext()) {
			node_data currentNode = iter.next();
			if(currentNode.getLocation().y() > y_max) {
				y_max = currentNode.getLocation().y();
			}
		}
		return y_max;
	}
	
	public void setScale() {
		StdDraw.setCanvasSize(1250 , 650); //(int)Math.abs(getXmin()+getXmax())+ // (int) Math.abs(getYmin()+getYmax())+
		StdDraw.setXscale(getXmin()-0.001,getXmax()+0.001);
		StdDraw.setYscale(getYmin()-0.001,getYmax()+0.001);
	}

	public void drawVertex() {
		StdDraw.setPenColor(Color.black);
		StdDraw.setPenRadius(0.017);
		Iterator<node_data> iter = this.graph.getV().iterator();
		while(iter.hasNext()) {
			node_data currentNode = iter.next();
			double x = currentNode.getLocation().x();
			double y = currentNode.getLocation().y();
			StdDraw.point(x, y);
		}
	}

	public void printKey() {
		StdDraw.setPenColor(Color.red);
		StdDraw.setPenRadius(0.30);
		Iterator<node_data> iter = this.graph.getV().iterator();
		while(iter.hasNext()) {
			StdDraw.setFont(new Font("Ariel", Font.BOLD, 18));
			node_data currentNode = iter.next();
			StdDraw.text(currentNode.getLocation().x()-0.00050, currentNode.getLocation().y(),""+currentNode.getKey());;
		}
	}
	
	public void drawEdges() {
		StdDraw.setPenColor(Color.orange);
		StdDraw.setPenRadius(0.003);
		Iterator<node_data> iterNodes = this.graph.getV().iterator();
		while(iterNodes.hasNext()){
			node_data currentNode = iterNodes.next();
			Iterator<edge_data> iterEdges = this.graph.getE(currentNode.getKey()).iterator();
			while(iterEdges.hasNext()){
				edge_data currentEdge = iterEdges.next();
				StdDraw.line(graph.getNode(currentEdge.getSrc()).getLocation().x(), graph.getNode(currentEdge.getSrc()).getLocation().y(),graph.getNode(currentEdge.getDest()).getLocation().x(), graph.getNode(currentEdge.getDest()).getLocation().y());	
			}
		}
	}
	
	public void drawDirection() {
		Iterator<node_data> iterNodes = this.graph.getV().iterator();
		while(iterNodes.hasNext()){
			node_data currentNode = iterNodes.next();
			Iterator<edge_data> iterEdges = this.graph.getE(currentNode.getKey()).iterator();
			while(iterEdges.hasNext()){
				edge_data currentEdge = iterEdges.next();
				StdDraw.setPenRadius(0.010);
				StdDraw.setPenColor(StdDraw.GREEN);
				StdDraw.point((currentNode.getLocation().x()+graph.getNode(currentEdge.getDest()).getLocation().x()*3)/4, (currentNode.getLocation().y()+graph.getNode(currentEdge.getDest()).getLocation().y()*3)/4);
			}
		}
	}
	
	public void drawEdgesWeight() {
		StdDraw.setFont(new Font("Ariel", 2, 14));
		StdDraw.setPenColor(Color.BLUE.darker());
		Iterator<node_data> iterNodes = this.graph.getV().iterator();
		while(iterNodes.hasNext()){
			node_data currentNode = iterNodes.next();
			Iterator<edge_data> iterEdges = this.graph.getE(currentNode.getKey()).iterator();
			while(iterEdges.hasNext()){
				edge_data currentEdge = iterEdges.next();
				StdDraw.setFont(new Font("Ariel", Font.BOLD, 15));
				StdDraw.setPenColor(Color.black);
				StdDraw.text((currentNode.getLocation().x()+graph.getNode(currentEdge.getDest()).getLocation().x()*3)/4, (currentNode.getLocation().y()+graph.getNode(currentEdge.getDest()).getLocation().y()*3)/4, ""+ currentEdge.getWeight());
			}
		}
	}
	
	private double scale(double data, double r_min, double r_max, double t_min, double t_max){	
		double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
		return res;
	}
}




