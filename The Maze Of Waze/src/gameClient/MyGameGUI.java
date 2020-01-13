package gameClient;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.graph;
import elements.edge_data;
import elements.fruit;
import elements.node_data;
import elements.robot;
import gui.Graph_GUI;
import oop_dataStructure.OOP_DGraph;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
import utils.Point3D;
import utils.StdDraw;

public class MyGameGUI{
	private DGraph graph;
	game_service game;
	double Epsilon = 0.001;

	public static void main(String[] args) throws JSONException {
		MyGameGUI ggg = new MyGameGUI();
		ggg.initMyGui();
	}
	public void paint() {
		graph.initGraph(game);
		init(graph);
		drawGraph();
	}


	/**
	 * Default constructor
	 */
	public MyGameGUI() {
		graph = new DGraph();
		StdDraw.setGui(this);
	}

	public DGraph getGraph(){
		return graph;
	}

	/**
	 * A copy constructor
	 * @param g - the given graph.
	 */
	public MyGameGUI(graph g){
		this.graph = (DGraph) g;
		StdDraw.setGui(this);
	}

	/**
	 * Initialize the Graph_GUI from given graph.
	 * @param g - the given graph.
	 */
	public void init(graph g) {
		this.graph = (DGraph) g;
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
		drawFruits();
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
		StdDraw.setCanvasSize(1250 , 650); 
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
		StdDraw.setPenColor(Color.black);
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

	public void drawFruits() {
		Iterator<fruit> iter = this.graph.fruits.iterator();
		while(iter.hasNext()) {
			fruit currentNode = iter.next();
			double x = currentNode.getPos().x();
			double y = currentNode.getPos().y();
			if(currentNode.type  == 1) {
				StdDraw.picture(x, y, "apple.png", 0.001, 0.001);
			}
			else if (currentNode.type == -1){
				StdDraw.picture(x, y, "banana.png", 0.001, 0.001);	
			}
			else {
				throw new RuntimeException("valid type of fruit");
			}
		}
	}
	public int getRobotNumber(game_service game) throws JSONException {
		String info = game.toString();
		JSONObject line;
		line = new JSONObject(info);
		JSONObject ttt = line.getJSONObject("GameServer");
		int rs = ttt.getInt("robots");
		return rs;
	}

	public void initMyGui() throws JSONException {
		drawGraph();
		String[] levels = new String[24];
		for(int i=0; i<levels.length; i++) {
			levels[i] = ""+i;
		}

		Object currentNum = JOptionPane.showInputDialog(null, "Choose a level", "Message",
				JOptionPane.INFORMATION_MESSAGE, null, levels, levels[0]);
		int scenario_num = Integer.parseInt(currentNum.toString());
		game = Game_Server.getServer(scenario_num);
		JOptionPane.showMessageDialog(null, "Please put " +getRobotNumber(game)+" robots");
		paint();
		int count = 0;
		int done = 0;
		while(count != getRobotNumber(game)) {
			done = drawRobots(count);
			count = done;
		}
	}
	public int drawRobots(int count) throws JSONException {	
			if(StdDraw.isMousePressed() == true) {
				double x = StdDraw.mouseX();
				double y = StdDraw.mouseY();
				if(check(x,y) == true) {
					StdDraw.setPenColor(Color.pink);
					StdDraw.setPenRadius(0.04);
					StdDraw.point(x, y);
					count++;
				}
		}
			return count;
	}

	public boolean check(double x, double y) {
		boolean flag = false;
		Iterator<node_data> iter = graph.getV().iterator();
		node_data currentNode = iter.next();
		while(flag == false) {
			if((x-Epsilon) <= currentNode.getLocation().x() && (x+Epsilon) >= currentNode.getLocation().x()&& (y-Epsilon) <= currentNode.getLocation().y() && (y+Epsilon) >= currentNode.getLocation().y()); {
				flag = true;
			}
		}
		return flag;
	}

}
