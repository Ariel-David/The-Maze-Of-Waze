package gameClient;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.graph;
import elements.Node;
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
	private int key = -1;
	private DGraph graph;
	game_service game;
	double Epsilon = 0.0001;

	public static void main(String[] args) {

		MyGameGUI ggg = new MyGameGUI();
		try {
			ggg.initMyGui();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

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
		drawEdgesWeight();
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
				StdDraw.setFont(new Font("Ariel", Font.ROMAN_BASELINE, 15));
				StdDraw.setPenColor(Color.gray);
				double x1 = (currentNode.getLocation().x()+graph.getNode(currentEdge.getDest()).getLocation().x()*3)/4;
				double x2 = (currentNode.getLocation().y()+graph.getNode(currentEdge.getDest()).getLocation().y()*3)/4;
				String weight = String.format("%.2f", currentEdge.getWeight());
				StdDraw.text(x1, x2, ""+weight);

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
				StdDraw.picture(x, y, "banana.png", 0.00070, 0.00070);	
			}
			else {
				throw new RuntimeException("valid type of fruit");
			}
		}
	}
	public int getRobotNumber() throws JSONException {
		String info = game.toString();
		JSONObject line;
		line = new JSONObject(info);
		JSONObject ttt = line.getJSONObject("GameServer");
		int rs = ttt.getInt("robots");
		return rs;
	}

	public void initMyGui() throws JSONException {
		drawGraph();
		String[] type = new String[2];
		type[0] = "Manual Game";
		type[1] = "Auto Game";

		/** choosing the type of the game **/
		Object gameType = JOptionPane.showInputDialog(null, "Choose a game mode", "Message",
				JOptionPane.INFORMATION_MESSAGE, null, type, type[0]);
		String[] levels = new String[24];
		for(int i=0; i<levels.length; i++) {
			levels[i] = ""+i;
		}

		/** Manual game **/
		if(gameType == type[0]) {
			/** choosing the level **/
			Object level = JOptionPane.showInputDialog(null, "Choose level", "Message",
					JOptionPane.INFORMATION_MESSAGE, null, levels, levels[0]);
			int scenario_num = Integer.parseInt(level.toString());
			game = Game_Server.getServer(scenario_num);
			JOptionPane.showMessageDialog(null, "Please put " +getRobotNumber()+" robots");
			paint();
			drawRobotManual();
			game.startGame();
			while(game.isRunning()) {
				moveRobots(-1);
				StdDraw.clear();
				updateGraph();
				StdDraw.enableDoubleBuffering();
				StdDraw.show();
			}
		}

		/** Auto game **/
		else {
			/** choosing the level **/	
			Object level = JOptionPane.showInputDialog(null, "Choose level", "Message",
					JOptionPane.INFORMATION_MESSAGE, null, levels, levels[0]);
		}

	}

	public void drawRobotManual() throws JSONException {
		int i = 0;
		int num = getRobotNumber();
		while(i < num) {
			if(StdDraw.isMousePressed() == true) {
				StdDraw.isMousePressed = false;
				for (node_data currentNode : graph.getV()) {
					if(((StdDraw.mouseX()-Epsilon) <= currentNode.getLocation().x()) && 
							((StdDraw.mouseX()+Epsilon) >= currentNode.getLocation().x())&& 
							((StdDraw.mouseY()-Epsilon) <= currentNode.getLocation().y()) &&
							((StdDraw.mouseY()+Epsilon) >= currentNode.getLocation().y())) {
						Point3D p = new Point3D(currentNode.getLocation().x(),currentNode.getLocation().y(),0);
						StdDraw.picture(p.x(), p.y(),"data\\robot.png",0.00070,0.00050);
						robot r = new robot(i,currentNode.getKey(), -1, 1, 0.0, p);
						graph.addRobot(r);
						game.addRobot(currentNode.getKey());
						i++;
					}
				}
			}
		}
	}

	public void moveRobots(int next) throws JSONException {
		int index = select();
		System.out.println(StdDraw.isKeyPressed(KeyEvent.VK_0));
		if (index!=-1) {
			if(index < graph.robots.size()) {
				int dest = nextNodeMenual(graph,graph.robots.get(index).getSrc());
				System.out.println("dest : "+dest);
				game.chooseNextEdge(graph.robots.get(index).getId(),dest);
			}
			else {
				index = -1;
			}
		}
		game.move();
	}

	public int select() {
		if(StdDraw.isKeyPressed(KeyEvent.VK_0)) {
			System.out.println(StdDraw.isKeyPressed(KeyEvent.VK_0));
			key=0;
		}
		else if(StdDraw.isKeyPressed(KeyEvent.VK_1)) {
			key=1;
		}	
		else if(StdDraw.isKeyPressed(KeyEvent.VK_2)) {
			key=2;
		}
		return key;
	}

	public void updateGraph() throws JSONException {
		String fruitList = game.getFruits().toString();
		String robotList = game.getRobots().toString();

		/*Initialise fruits from json*/
		graph.fruits.clear();
		JSONArray f = new JSONArray(fruitList);
		for(int i=0; i<f.length(); i++) {
			JSONObject current = f.getJSONObject(i);
			JSONObject current2 = current.getJSONObject("Fruit");
			int type = current2.getInt("type");
			double value = current2.getDouble("value");
			Object pos = current2.get("pos");
			Point3D p = new Point3D(pos.toString());
			fruit fu = new fruit(type, value, p);
			graph.addFruit(fu);
		}
		/*Initialise robots from json*/
		graph.robots.clear();
		JSONArray r = new JSONArray(robotList);
		for(int j=0; j<r.length(); j++) {
			JSONObject line = r.getJSONObject(j);
			JSONObject robotline = line.getJSONObject("Robot");
			int id = robotline.getInt("id");
			double value1 = robotline.getDouble("value");
			int src = robotline.getInt("src");
			int dest = robotline.getInt("dest");
			int speed = robotline.getInt("speed");
			Object pos1 = robotline.get("pos");
			Point3D point = new Point3D(pos1.toString());
			robot ro = new robot(id, src, dest, speed, value1, point);
			graph.addRobot(ro);
			StdDraw.picture(ro.getPos().x(), ro.getPos().y(),"data\\robot.png",0.00070,0.00050);
		}
		drawEdges();
		drawVertex();
		printKey();
		drawDirection();
		drawEdgesWeight();
		drawFruits();
	}

	public int nextNodeMenual(DGraph g, int src) throws JSONException {
		int dest = -1;
		if(StdDraw.isMousePressed()) {
			StdDraw.isMousePressed = false;
			System.out.println("hi");
			double x = StdDraw.mouseX();
			double y = StdDraw.mouseY();
			int i = 0;
			//while(i < 1) {
			for (edge_data currentEdge : ((Node)graph.getNode(src)).edges.values()) {
				if(((x-Epsilon) <= graph.getNode(currentEdge.getDest()).getLocation().x()) && 
						((x+Epsilon) >= graph.getNode(currentEdge.getDest()).getLocation().x())&& 
						((y-Epsilon) <= graph.getNode(currentEdge.getDest()).getLocation().y()) &&
						((y+Epsilon) >= graph.getNode(currentEdge.getDest()).getLocation().y())) {
					dest = graph.getNode(currentEdge.getDest()).getKey();
					//i++;
					//}
				}
			}
		}
		return dest;
	}
}
