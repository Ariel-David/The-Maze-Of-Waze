package gameClient;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalTime;
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
import de.micromata.opengis.kml.v_2_2_0.Kml;
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
	private Thread t;


	public static void main(String[] args) {
		MyGameGUI ggg = new MyGameGUI();
		try {
			ggg.initMyGui();
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			int scenario_num = Integer.parseInt(level.toString());
			game = Game_Server.getServer(scenario_num);
			graph.initGraph(game);
			init(graph);
//			kml = new KML_Logger(this);
//			kml.BuildGraph();
			setScale();
			StdDraw.picture(getXmin()+0.00180, getYmin()-0.0003, "data\\ba.png", 0.099,0.050);	
			drawEdges();
			drawVertex();
			printKey();
			drawDirection();
			drawFruits();
			drawEdgesWeight();
			//paint();
			JOptionPane.showMessageDialog(null, "Please put " +getRobotNumber()+" robots");
			ManualGame.drawRobotManual(graph,game);
			ThreadKML();
			game.startGame();
			while(game.isRunning()) {
				printScore(game);
				ManualGame.moveRobotsManual(-1,game,graph);
				StdDraw.clear();
				StdDraw.enableDoubleBuffering();
				updateGraph();
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

		/** Auto game **/
		if(gameType == type[1]) {
			Object level = JOptionPane.showInputDialog(null, "Choose level", "Message",
					JOptionPane.INFORMATION_MESSAGE, null, levels, levels[0]);
			int scenario_num = Integer.parseInt(level.toString());
			game = Game_Server.getServer(scenario_num);
			JOptionPane.showMessageDialog(null, "This game inculde " +getRobotNumber()+" robots");
			paint();
			fruit [] arr = AutoGame.sortByValue(graph.fruits);
			AutoGame.putRobot(arr,game,graph);
			ThreadKML();
			game.startGame();
			while(game.isRunning()) {
				AutoGame.moveRobotsAuto(game,graph);
				printScore(game);
				StdDraw.clear();
				StdDraw.enableDoubleBuffering();
				updateGraph();
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
			///	kml.saveToFile(""+scenario_num,results);
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
		printKey();
		drawDirection();
		drawFruits();
		drawEdgesWeight();
	}

	public void updateGraph() throws JSONException {
		StdDraw.picture(getXmin()+0.00180, getYmin()-0.0003, "data\\ba.png", 0.099,0.050);	
		drawEdges();
		drawVertex();
		printKey();
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
			StdDraw.picture(ro.getPos().x(), ro.getPos().y(),photo[index],0.003,0.0015);
			index++;
		}
	}

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

	private void setScale() {
		StdDraw.setCanvasSize(1250 , 650); 
		StdDraw.picture(getXmin()+0.00180, getYmin()-0.0003, "data\\ba.png", 1.1,1.0);	
		StdDraw.setXscale(getXmin()-0.001,getXmax()+0.001);
		StdDraw.setYscale(getYmin()-0.001,getYmax()+0.001);
	}

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

	private void printKey() {
		StdDraw.setPenColor(Color.black);
		StdDraw.setPenRadius(0.30);
		Iterator<node_data> iter = MyGameGUI.graph.getV().iterator();
		while(iter.hasNext()) {
			StdDraw.setFont(new Font("Ariel", Font.BOLD, 18));
			node_data currentNode = iter.next();
			StdDraw.text(currentNode.getLocation().x()-0.00050, currentNode.getLocation().y(),""+currentNode.getKey());;
		}
	}

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

	private void drawFruits() {
		Iterator<fruit> iter = MyGameGUI.graph.fruits.iterator();
		while(iter.hasNext()) {
			fruit currentNode = iter.next();
			double x = currentNode.getPos().x();
			double y = currentNode.getPos().y();
			if(currentNode.type  == 1) {
				StdDraw.picture(x, y, "data\\apple.png", 0.001, 0.001);
			}
			else if (currentNode.type == -1){
				StdDraw.picture(x, y, "data\\banana.png", 0.00100, 0.00070);	
			}
			else {
				throw new RuntimeException("valid type of fruit");
			}
		}
	}

	public static int getRobotNumber() throws JSONException {
		String info = game.toString();
		JSONObject line;
		line = new JSONObject(info);
		JSONObject ttt = line.getJSONObject("GameServer");
		int rs = ttt.getInt("robots");
		return rs;
	}


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

	public void ThreadKML(){
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				while(game.isRunning()){
					if(graph!=null){
						try {
							long timeToSleep = 100;
							Thread.sleep(timeToSleep);
							String Starttime  = java.time.LocalDate.now()+"T"+java.time.LocalTime.now();
							LocalTime test = LocalTime.now();
							test= test.plusNanos(timeToSleep*1000000);
							String endTime = java.time.LocalDate.now()+"T"+test;

//							kml.setFruits(Starttime,endTime);
//							kml.setBots(Starttime,endTime);
						}

						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				t.interrupt();
			}
		}
				);
		t.start();
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
		return this.kml;
	}

	public void setKml(KML_Logger kml) {
		this.kml = kml;
	}

	public Thread getT() {
		return t;
	}

	public void setT(Thread t) {
		this.t = t;
	}
}


