package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import algorithms.Graph_Algo;
import algorithms.graph_algorithms;
import dataStructure.DGraph;
import dataStructure.graph;
import elements.Edge;
import elements.edge_data;
import elements.node_data;
import utils.Range;
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
	public void init(DGraph gra) {
		this.graph = gra;
		this.algoGraph.graph = gra;
	}

	/**
	 * Initialise the Graph from a string.
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
		drawEdgesWeight();
	}
	private double scale(double data, double r_min, double r_max, double t_min, double t_max){	
		double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
		return res;
	}
	
	public void setScale() {
		int x_min = 0;
		int x_max = 0;
		int y_min = 0;
		int y_max = 0;

		Iterator<node_data> iter = this.graph.getV().iterator();
		while(iter.hasNext()) {
			node_data currentNode = iter.next();
			if(currentNode.getLocation().x() < x_min) {
				x_min = (int) currentNode.getLocation().x();
			}
			if(currentNode.getLocation().x() > x_max) {
				x_max = (int) currentNode.getLocation().x();
			}
			if(currentNode.getLocation().y() < y_min) {
				y_min = (int) currentNode.getLocation().y();
			}
			if(currentNode.getLocation().y() > y_max) {
				y_max = (int) currentNode.getLocation().y();
			}
		}
		StdDraw.setCanvasSize(Math.abs(x_min+x_max) +500 , Math.abs(y_min+y_max)+500);
		StdDraw.setXscale(x_min-10,x_max+10);
		StdDraw.setYscale(y_min-10,y_max+10);
	}

	public void drawVertex() {
		StdDraw.setPenColor(Color.BLACK);
		StdDraw.setPenRadius(0.017);
		Iterator<node_data> iter = this.graph.getV().iterator();
		while(iter.hasNext()) {
			node_data currentNode = iter.next();
			StdDraw.point(currentNode.getLocation().x(), currentNode.getLocation().y());
		}
	}

	public void printKey() {
		StdDraw.setPenColor(Color.red);
		StdDraw.setPenRadius(0.8);
		Iterator<node_data> iter = this.graph.getV().iterator();
		while(iter.hasNext()) {
			StdDraw.setFont(new Font("Ariel", Font.BOLD, 19));
			node_data currentNode = iter.next();
			StdDraw.text(currentNode.getLocation().x()-1, currentNode.getLocation().y()-5,""+currentNode.getKey());;
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
				StdDraw.setPenRadius(0.020);
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
}


