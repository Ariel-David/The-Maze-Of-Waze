package gameClient;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import Server.Game_Server;
import Server.game_service;

import dataStructure.DGraph;
import dataStructure.graph;
import elements.Edge;

import elements.edge_data;
import elements.fruit;
import elements.node_data;
import elements.robot;

import utils.Point3D;
import utils.StdDraw;

public class MyGameGUI{
	private static DGraph graph;
	static game_service game;
	static double Epsilon = 0.000001;
	private static KML_Logger kml;
	private static int scenario_num;
	private Thread t;

	public static void main(String[] args) {
		MyGameGUI ggg = new MyGameGUI();
		try {
			ggg.initMyGui();
		} 
		catch (JSONException e) {
			System.exit(0);
		}
	}

	private void paint() {
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

	/**
	 * Initialise the Graph_GUI from given graph.
	 * @param g - the given graph.
	 */
	private void init(graph g) {
		MyGameGUI.graph = (DGraph) g;
	}

	/**
	 * this function initialise the game, according the choosing of type of the game -  manual or auto  
	 * @throws JSONException
	 */
	private void initMyGui() throws JSONException {
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
			Object level = JOptionPane.showInputDialog(null, "Choose level", "Message",
					JOptionPane.INFORMATION_MESSAGE, null, levels, levels[0]);
			try {
				scenario_num = Integer.parseInt(level.toString());
				game = Game_Server.getServer(scenario_num);
				graph.initGraph(game);
				init(graph);
				setScale();
				StdDraw.picture(getXmin()+0.00180, getYmin()-0.0003, "data\\ba.png", 0.099,0.050);	
				drawEdges();
				drawVertex();
				drawKey();
				drawDirection();
				drawFruitsManual();
				drawEdgesWeight();
				JOptionPane.showMessageDialog(null, "Please put " +getRobotNumber()+" robots");
				ManualGame.drawRobotManual(graph,game);
				JOptionPane.showMessageDialog(null, "Please choose the player with the keyboard and click on the number "
						+ "of the robot you want to play with");
				game.startGame();
				while(game.isRunning()) {
					printScore(game);
					ManualGame.moveRobotsManual(-1,game,graph);
					StdDraw.clear();
					StdDraw.enableDoubleBuffering();
					updateGraphManual();
					StdDraw.show();
				}
				game.stopGame();
				while(!game.isRunning()) {
					int scoreInt = 0;
					String results = game.toString();
					StdDraw.setPenColor(Color.black);
					StdDraw.setFont(new Font("Ariel", Font.BOLD, 100));
					StdDraw.clear();
					StdDraw.picture(getXmin()+0.00180, getYmin()-0.0003, "data\\ba.png", 0.099,0.050);	
					StdDraw.enableDoubleBuffering();
					StdDraw.text(getXmin()+0.00180, getYmin()+0.0030, "                 Game Over!");
					JSONObject score = new JSONObject(results);
					JSONObject ttt = score.getJSONObject("GameServer");
					scoreInt = ttt.getInt("grade");
					String scoreStr = "Your Score: " + scoreInt;
					StdDraw.setFont(new Font("Ariel", Font.BOLD, 50));
					StdDraw.text(getXmin()+0.007, getYmin(), scoreStr);
					StdDraw.show();
					//	kml.saveToFile(""+scenario_num,results);
				}
			}
			catch (Exception e) {
				System.exit(0);
			}
		}

		/** Auto game **/
		if(gameType == type[1]) {
			Object level = JOptionPane.showInputDialog(null, "Choose level", "Message",
					JOptionPane.INFORMATION_MESSAGE, null, levels, levels[0]);
			try {
				scenario_num = Integer.parseInt(level.toString());
				kml = new KML_Logger(scenario_num);
				game = Game_Server.getServer(scenario_num);
				JOptionPane.showMessageDialog(null, "This game inculde " +getRobotNumber()+" robots");
				paint();
				fruit [] arr = AutoGame.sortByValue(graph.fruits);
				AutoGame.putRobot(arr,game,graph);
				game.startGame();
				while(game.isRunning()) {
					AutoGame.moveRobotsAuto(game,graph);
					printScore(game);
					StdDraw.clear();
					StdDraw.enableDoubleBuffering();
					updateGraphAuto();
					StdDraw.show();
				}
				kml.KML_Stop();
				game.stopGame();
				while(!game.isRunning()) {
					int scoreInt = 0;
					String results = game.toString();
					StdDraw.setPenColor(Color.black);
					StdDraw.setFont(new Font("Ariel", Font.BOLD, 100));
					StdDraw.clear();
					StdDraw.picture(getXmin()+0.00180, getYmin()-0.0003, "data\\ba.png", 0.099,0.050);	
					StdDraw.enableDoubleBuffering();
					StdDraw.text(getXmin()+0.00180, getYmin()+0.0030, "                 Game Over!");
					JSONObject score = new JSONObject(results);
					JSONObject ttt = score.getJSONObject("GameServer");
					scoreInt = ttt.getInt("grade");
					String scoreStr = "Your Score: " + scoreInt;
					StdDraw.setFont(new Font("Ariel", Font.BOLD, 50));
					StdDraw.text(getXmin()+0.007, getYmin(), scoreStr);
					StdDraw.show();
				}
			}
			catch (Exception e) {
				System.exit(0);
			}

		}
	}

	/**
	 * Draw the graph according to this methods the Graph from a string.
	 */
	private void drawGraph() {
		setScale();
		drawEdges();
		drawVertex();
		drawKey();
		drawDirection();
		drawFruits();
		drawEdgesWeight();
	}

	/**
	 * updating the graph after the changes of moving the robots and the new location of the
	 * fruits. 
	 * the new location is taken from the server game.
	 * drawing the fruits and the robots
	 * @throws JSONException
	 */
	public void updateGraphAuto() throws JSONException {
		StdDraw.picture(getXmin()+0.00180, getYmin()-0.0003, "data\\ba.png", 0.099,0.050);	
		drawEdges();
		drawVertex();
		drawKey();
		drawDirection();
		drawEdgesWeight();
		drawFruits();
		printScore(game);
		String fruitList = game.getFruits().toString();
		String robotList = game.getRobots().toString();

		/*Initialise fruits from json*/
		graph.fruits.clear();
		JSONArray f = new JSONArray(fruitList);
		int index = 0;
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
			game.addRobot(ro.getSrc());
			String[] photo = new String[3];
			photo[0] = "data\\monkey.png";
			photo[1] = "data\\monkey1.png";
			photo[2] = "data\\monkey2.png";
			kml.Place_Mark("data\\monkey.png",ro.getPos().toString());
			StdDraw.picture(ro.getPos().x(), ro.getPos().y(),photo[index],0.003,0.0015);
			index++;
		}
	}

	/**
	 * updating the graph after the changes of moving the robots and the new location of the
	 * fruits. 
	 * the new location is taken from the server game.
	 * drawing the fruits and the robots
	 * @throws JSONException
	 */
	public void updateGraphManual() throws JSONException {
		StdDraw.picture(getXmin()+0.00180, getYmin()-0.0003, "data\\ba.png", 0.099,0.050);	
		drawEdges();
		drawVertex();
		drawKey();
		drawDirection();
		drawEdgesWeight();
		drawFruitsManual();
		printScore(game);
		String fruitList = game.getFruits().toString();
		String robotList = game.getRobots().toString();

		/*Initialise fruits from json*/
		graph.fruits.clear();
		JSONArray f = new JSONArray(fruitList);
		int index = 0;
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
			game.addRobot(ro.getSrc());
			String[] photo = new String[3];
			photo[0] = "data\\monkey.png";
			photo[1] = "data\\monkey1.png";
			photo[2] = "data\\monkey2.png";
			StdDraw.picture(ro.getPos().x(), ro.getPos().y(),photo[index],0.003,0.0015);
			index++;
		}
	}

	/**
	 * return the x value of the minimum vertex in the graph
	 * @return
	 */
	private double getXmin() {
		double x_min = Double.MAX_VALUE;
		Iterator<node_data> iter = MyGameGUI.graph.getV().iterator();
		while(iter.hasNext()) {
			node_data currentNode = iter.next();
			if(currentNode.getLocation().x() < x_min) {
				x_min = currentNode.getLocation().x();
			}
		}
		return x_min;
	}

	/**
	 * return the x value of the maximum vertex in the graph
	 * @return
	 */
	private double getXmax() {
		double x_max = Double.MIN_VALUE;
		Iterator<node_data> iter = MyGameGUI.graph.getV().iterator();
		while(iter.hasNext()) {
			node_data currentNode = iter.next();
			if(currentNode.getLocation().x() > x_max) {
				x_max = currentNode.getLocation().x();
			}
		}
		return x_max;
	}

	/**
	 * return the y value of the minimum vertex in the graph
	 * @return
	 */
	private double getYmin() {
		double y_min = Double.MAX_VALUE;
		Iterator<node_data> iter = MyGameGUI.graph.getV().iterator();
		while(iter.hasNext()) {
			node_data currentNode = iter.next();
			if(currentNode.getLocation().y() < y_min) {
				y_min =  currentNode.getLocation().y();
			}
		}
		return y_min;
	}

	/**
	 * return the y value of the maximum vertex in the graph
	 * @return
	 */
	private double getYmax() {
		double y_max = Double.MIN_VALUE;
		Iterator<node_data> iter = MyGameGUI.graph.getV().iterator();
		while(iter.hasNext()) {
			node_data currentNode = iter.next();
			if(currentNode.getLocation().y() > y_max) {
				y_max = currentNode.getLocation().y();
			}
		}
		return y_max;
	}

	/**
	 * setting the scale of the canvas
	 * set the size of the canvas
	 */
	private void setScale() {
		StdDraw.setCanvasSize(1250 , 650); 
		StdDraw.picture(getXmin()+0.00180, getYmin()-0.0003, "data\\ba.png", 1.1,1.0);	
		StdDraw.setXscale(getXmin()-0.001,getXmax()+0.001);
		StdDraw.setYscale(getYmin()-0.001,getYmax()+0.001);
	}

	/**
	 * drawing the vertexes on the graph
	 */
	private void drawVertex() {
		StdDraw.setPenColor(Color.black);
		StdDraw.setPenRadius(0.017);
		Iterator<node_data> iter = MyGameGUI.graph.getV().iterator();
		while(iter.hasNext()) {
			node_data currentNode = iter.next();
			double x = currentNode.getLocation().x();
			double y = currentNode.getLocation().y();
			StdDraw.point(x, y);
		}
	}

	/**
	 * drawing the key values on the graph
	 */
	private void drawKey() {
		StdDraw.setPenColor(Color.black);
		StdDraw.setPenRadius(0.30);
		Iterator<node_data> iter = MyGameGUI.graph.getV().iterator();
		while(iter.hasNext()) {
			StdDraw.setFont(new Font("Ariel", Font.BOLD, 18));
			node_data currentNode = iter.next();
			StdDraw.text(currentNode.getLocation().x()-0.00050, currentNode.getLocation().y(),""+currentNode.getKey());;
		}
	}

	/**
	 * drawing the edges on the graph
	 */
	private void drawEdges() {
		StdDraw.setPenColor(Color.orange);
		StdDraw.setPenRadius(0.006);
		Iterator<node_data> iterNodes = MyGameGUI.graph.getV().iterator();
		while(iterNodes.hasNext()){
			node_data currentNode = iterNodes.next();
			Iterator<edge_data> iterEdges = MyGameGUI.graph.getE(currentNode.getKey()).iterator();
			while(iterEdges.hasNext()){
				edge_data currentEdge = iterEdges.next();
				StdDraw.line(graph.getNode(currentEdge.getSrc()).getLocation().x(), graph.getNode(currentEdge.getSrc()).getLocation().y(),graph.getNode(currentEdge.getDest()).getLocation().x(), graph.getNode(currentEdge.getDest()).getLocation().y());	
			}
		}
	}

	/**
	 * drawing the directions values on the graph
	 */
	private void drawDirection() {
		Iterator<node_data> iterNodes = MyGameGUI.graph.getV().iterator();
		while(iterNodes.hasNext()){
			node_data currentNode = iterNodes.next();
			Iterator<edge_data> iterEdges = MyGameGUI.graph.getE(currentNode.getKey()).iterator();
			while(iterEdges.hasNext()){
				edge_data currentEdge = iterEdges.next();
				StdDraw.setPenRadius(0.016);
				StdDraw.setPenColor(StdDraw.GREEN);
				StdDraw.point((currentNode.getLocation().x()+graph.getNode(currentEdge.getDest()).getLocation().x()*3)/4, (currentNode.getLocation().y()+graph.getNode(currentEdge.getDest()).getLocation().y()*3)/4);
			}
		}
	}

	/**
	 * drawing the weight of the edges on the graph
	 */
	private void drawEdgesWeight() {
		StdDraw.setFont(new Font("Ariel", 2, 14));
		StdDraw.setPenColor(Color.BLUE.darker());
		Iterator<node_data> iterNodes = MyGameGUI.graph.getV().iterator();
		while(iterNodes.hasNext()){
			node_data currentNode = iterNodes.next();
			Iterator<edge_data> iterEdges = MyGameGUI.graph.getE(currentNode.getKey()).iterator();
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

	/**
	 * drawing the fruits on the graph on the auto game.
	 */
	private void drawFruits() {
		Iterator<fruit> iter = MyGameGUI.graph.fruits.iterator();
		while(iter.hasNext()) {
			fruit currentFruit = iter.next();
			double x = currentFruit.getPos().x();
			double y = currentFruit.getPos().y();
			if(currentFruit.type  == 1) {
				kml.Place_Mark("fruit_1",currentFruit.getPos().toString());
				StdDraw.picture(x, y, "data\\apple.png", 0.001, 0.001);
			}
			else if (currentFruit.type == -1){
				kml.Place_Mark("fruit_-1",currentFruit.getPos().toString());
				StdDraw.picture(x, y, "data\\banana.png", 0.00100, 0.00070);	
			}
			else {
				throw new RuntimeException("valid type of fruit");
			}
		}
	}

	/**
	 * drawing the fruits on the graph on the manual game
	 */
	private void drawFruitsManual() {
		Iterator<fruit> iter = MyGameGUI.graph.fruits.iterator();
		while(iter.hasNext()) {
			fruit currentFruit = iter.next();
			double x = currentFruit.getPos().x();
			double y = currentFruit.getPos().y();
			if(currentFruit.type  == 1) {
				StdDraw.picture(x, y, "data\\apple.png", 0.001, 0.001);
			}
			else if (currentFruit.type == -1){
				StdDraw.picture(x, y, "data\\banana.png", 0.00100, 0.00070);	
			}
			else {
				throw new RuntimeException("valid type of fruit");
			}
		}
	}

	/**
	 * return the number of the robot in the game
	 * @return
	 * @throws JSONException
	 */
	public static int getRobotNumber() throws JSONException {
		String info = game.toString();
		JSONObject line;
		line = new JSONObject(info);
		JSONObject ttt = line.getJSONObject("GameServer");
		int rs = ttt.getInt("robots");
		return rs;
	}

	/**
	 * this function finding the edge according to a specific fruit
	 * return the edge
	 * @param f
	 * @return
	 */
	public static edge_data findEdge(fruit f) {
		edge_data e = new Edge();
		for (node_data currentNode : graph.getV()) {
			if (graph.getE(currentNode.getKey()) != null) {
				Iterator<edge_data>iter = graph.getE(currentNode.getKey()).iterator();
				while (iter.hasNext()) {
					e = iter.next();
					node_data dest = graph.getNode(e.getDest());
					node_data src = graph.getNode(e.getSrc());
					double srcToFruit = distance(src.getLocation(), f.getPos());
					double fruitToDest = distance(f.getPos(), dest.getLocation());
					double dis = distance(src.getLocation(), dest.getLocation());
					if (srcToFruit + fruitToDest <= dis + Epsilon) {
						if (f.getType() == -1 && src.getKey() > dest.getKey()) {
							return e;
						} 
						else if (f.getType() == 1 && src.getKey() < dest.getKey()) {
							return e;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * this function calculate the distance between src and dest that given
	 * @param src
	 * @param dest
	 * @return
	 */
	private static double distance(Point3D src, Point3D dest) {
		double ans = 0;
		double x1 = src.x();
		double x2 = dest.x();
		double y1 = src.y();
		double y2 = dest.y();
		ans = Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2);
		ans = Math.sqrt(ans);
		return ans;
	}

	/**
	 * finding the shortest path between src and dest and return the sum of the sum of the path
	 * @param src
	 * @param dest
	 * @return
	 */
	public static double shortestPathDist(int src, int dest) {
		String s = "";
		if(src == dest) {
			return 0;
		}
		for(node_data vertex : graph.getV()) {
			vertex.setWeight(Double.POSITIVE_INFINITY);
			vertex.setTag(0);
		}
		graph.getNode(src).setWeight(0);;
		shortestPathDistHelper(src,dest,s);
		return graph.getNode(dest).getWeight();
	}

	public static void shortestPathDistHelper(int src, int dest, String s) {
		if(graph.getNode(src).getTag() == 1 && graph.getNode(src) == graph.getNode(dest)) {
			return;
		}
		for (edge_data edges : graph.getE(src)) {
			double newSum = edges.getWeight() + graph.getNode(edges.getSrc()).getWeight();
			double currentSum = graph.getNode(edges.getDest()).getWeight();
			if(newSum < currentSum) {
				graph.getNode(edges.getDest()).setWeight(newSum);
				graph.getNode(edges.getDest()).setInfo(s + "->" +src);
				graph.getNode(src).setTag(1);
				shortestPathDistHelper(edges.getDest(), dest , s + "->" +src);
			}
		}
	}

	/**
	 * return a list of nodes of the shorter path between src and dest 
	 * @param src
	 * @param dest
	 * @return
	 */
	public static List<node_data> shortestPath(int src, int dest) {		
		List<node_data> visited = new ArrayList<>();
		for (node_data node_data : graph.getV()) {
			node_data.setInfo("");
		}
		if(shortestPathDist(src, dest) == Double.POSITIVE_INFINITY) {
			return null;
		}

		if(src == dest) {
			visited.add(graph.getNode(src));
			return visited;
		}

		String str = graph.getNode(dest).getInfo();
		str = str.substring(2);
		String [] splitArray = str.split("->");
		for(int i=0; i<splitArray.length; i++) {
			visited.add(graph.getNode(Integer.parseInt(splitArray[i])));
		}
		visited.add(graph.getNode(dest));
		return visited;
	}

	/**
	 * this function print the score and the time on the screen 
	 * @param game
	 */
	public void printScore(game_service game) {
		String results = game.toString();
		long t = game.timeToEnd();
		try {
			int scoreInt=0;
			JSONObject score = new JSONObject(results);
			JSONObject ttt = score.getJSONObject("GameServer");
			scoreInt = ttt.getInt("grade");

			String countDown = "Time Left: " + t/1000+"." + t%1000;
			String scoreStr = "Your Score: " + scoreInt;
			StdDraw.setPenColor(Color.BLUE);
			StdDraw.setFont(new Font("Ariel", Font.BOLD, 22));
			StdDraw.text(getXmin()+0.00180, getYmin()-0.0003, countDown);
			StdDraw.text(getXmin()+0.0060, getYmin()-0.0003, scoreStr);
		}
		catch (Exception e) {
			System.out.println("Failed to print score");
		}
	}

	public static DGraph getGraph() {
		return graph;
	}

	public static void setGraph(DGraph graph) {
		MyGameGUI.graph = graph;
	}

	public static game_service getGame() {
		return game;
	}

	public static void setGame(game_service game) {
		MyGameGUI.game = game;
	}

	public static double getEpsilon() {
		return Epsilon;
	}

	public static void setEpsilon(double epsilon) {
		Epsilon = epsilon;
	}

	public KML_Logger getKml() {
		return MyGameGUI.kml;
	}

	public void setKml(KML_Logger kml) {
		MyGameGUI.kml = kml;
	}

	public Thread getT() {
		return t;
	}

	public void setT(Thread t) {
		this.t = t;
	}
}


