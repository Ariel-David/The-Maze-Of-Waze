package gameClient;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jdi.event.ExceptionEvent;

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
	private static game_service game;
	static double Epsilon = 0.000001;
	private static KML_Logger kml;
	private static int scenario_num;
	public static int userID = -1;
	public static final String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser="student";
	public static final String jdbcUserPassword="OOP2020student";
	private static Thread t;
	private static int maxLevelUser;
	public static Integer[][] matLevel = new Integer[11][4];
	public static int sleep;
	public static ArrayList<Integer> stage = new ArrayList<>();

	public static void main(String[] args) {
		for(int i=0; i<matLevel.length; i++) {
			for(int j=0; j<matLevel[i].length; j++) {
				matLevel[i][j] = 0;
			}
		}
		matLevel[0][0] = 0;
		matLevel[1][0] = 1;
		matLevel[2][0] = 3;
		matLevel[3][0] = 5;
		matLevel[4][0] = 9;
		matLevel[5][0] = 11;
		matLevel[6][0] = 13;
		matLevel[7][0] = 16;
		matLevel[8][0] = 19;
		matLevel[9][0] = 20;
		matLevel[10][0] = 23;
		matLevel[0][3] = 290;
		matLevel[1][3] = 580;
		matLevel[2][3] = 580;
		matLevel[3][3] = 500;
		matLevel[4][3] = 580;
		matLevel[5][3] = 580;
		matLevel[6][3] = 580;
		matLevel[7][3] = 290;
		matLevel[8][3] = 580;
		matLevel[9][3] = 290;
		matLevel[10][3] = 1140;
		MyGameGUI ggg = new MyGameGUI();
		ggg.initMyGui();
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
	private void initMyGui(){
		drawGraph();
		try {
			String[] arr = {"0","1","3","5","9","11","13","16","19","20","23"};
			scenario_num = 0;
			Object idOb = JOptionPane.showInputDialog("Please Enter your Id");
			userID = Integer.parseInt(idOb.toString());				
			Game_Server.login(userID);
			game = Game_Server.getServer(scenario_num);
			setMaxLevelUser(game);
			String[] type = new String[2];
			type[0] = "Manual Game";
			type[1] = "Auto Game";
			/** choosing the type of the game **/
			Object gameType = JOptionPane.showInputDialog(null, "Choose a game mode", "Message",
					JOptionPane.INFORMATION_MESSAGE, null, type, type[0]);


			/** Manual game **/
			if(gameType == type[0]) {
				Object level = JOptionPane.showInputDialog(null, "Choose level", "Message",
						JOptionPane.INFORMATION_MESSAGE, null,arr, arr[0]);
				try {
					scenario_num = Integer.parseInt(level.toString());
					game = Game_Server.getServer(scenario_num);
					graph.initGraph(game);
					init(graph);
					JOptionPane.showMessageDialog(null, "Please choose the player with the keyboard and click on the number "
							+ "of the robot you want to play with");
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
					setMaxLevelUser(game);
					StdDraw.setPenColor(Color.black);
					StdDraw.setFont(new Font("Ariel", Font.BOLD, 100));
					StdDraw.clear();
					StdDraw.picture(getXmin()+0.00180, getYmin()-0.0003, "data\\ba.png", 0.099,0.050);	
					StdDraw.enableDoubleBuffering();
					StdDraw.text(getXmin()+0.00180, getYmin()+0.0030, "                 Game Over!");
					String scoreStr = "Your Score: " + getGrade(game);
					StdDraw.setFont(new Font("Ariel", Font.BOLD, 50));
					StdDraw.text(getXmin()+0.007, getYmin(), scoreStr);
					StdDraw.text(getXmin()+0.009, getYmin()+0.001, "Number of moves: "+printMoves(game));
					if(isGood(printMoves(game), scenario_num, getGrade(game))) {
						StdDraw.picture(getXmin()+0.01, getYmin()+0.002, "data\\pass.png",0.001,0.002);
						initTable(userID);
					}
					else {
						StdDraw.picture(getXmin()+0.01, getYmin()+0.002, "data\\\\notPass.png",0.001,0.002);
					}
					StdDraw.show();
				}
				catch (Exception e) {
					System.out.println("faild");
				}
			}

			/** Auto game **/
			if(gameType == type[1]) {
				Object level = JOptionPane.showInputDialog(null, "Choose level", "Message",
						JOptionPane.INFORMATION_MESSAGE, null, arr, arr[0]);
				try {
					scenario_num = Integer.parseInt(level.toString());
					kml = new KML_Logger(scenario_num);
					game = Game_Server.getServer(scenario_num);
					JOptionPane.showMessageDialog(null, "This game inculde " +getRobotNumber()+" robots");
					paint();
					AutoGame.putRobot(game,graph);
					game.startGame();
					ThreadMove();
					while(game.isRunning()) {
						AutoGame.moveRobotsAuto1(game,graph,1);
						AutoGame.moveRobotsAuto(game,graph,0);
						printScore(game);
						StdDraw.clear();
						StdDraw.enableDoubleBuffering();
						updateGraphAuto();
						StdDraw.show();
					}
					kml.KML_Stop();
					game.stopGame();
					setMaxLevelUser(game);
					StdDraw.setPenColor(Color.black);
					StdDraw.setFont(new Font("Ariel", Font.BOLD, 100));
					StdDraw.clear();
					StdDraw.picture(getXmin()+0.00180, getYmin()-0.0003, "data\\ba.png", 0.099,0.050);	
					StdDraw.enableDoubleBuffering();
					StdDraw.text(getXmin()+0.00180, getYmin()+0.0030, "                 Game Over!");
					String scoreStr = "Your Score: " + getGrade(game);
					StdDraw.setFont(new Font("Ariel", Font.BOLD, 50));
					StdDraw.text(getXmin()+0.007, getYmin(), scoreStr);
					StdDraw.text(getXmin()+0.009, getYmin()+0.001, "Number of moves: "+printMoves(game));
					if(isGood(printMoves(game), scenario_num, getGrade(game))) {
						StdDraw.picture(getXmin()+0.01, getYmin()+0.002, "data\\pass.png",0.001,0.002);
						initTable(userID);
					}
					else {
						StdDraw.picture(getXmin()+0.01, getYmin()+0.002, "data\\\\notPass.png",0.001,0.002);
					}
					StdDraw.show();
				}
				catch (Exception e) {
					System.err.println("faild");				}

			}
		}
		catch (Exception e) {
			// TODO: handle exception
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
	public static double distance(Point3D src, Point3D dest) {
		double ans = 0;
		double x1 = src.x();
		double x2 = dest.x();
		double y1 = src.y();
		double y2 = dest.y();
		ans = Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2);
		ans = Math.sqrt(ans);
		return ans;
	}

	//*******************************Drawing Methods******************************************//
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

	public int printMoves(game_service game) {
		int ans = 0;
		String results = game.toString();
		try {
			int movesInt=0;
			JSONObject moves = new JSONObject(results);
			JSONObject ttt = moves.getJSONObject("GameServer");
			movesInt = ttt.getInt("moves");
			ans = movesInt;
		}
		catch (Exception e) {
			System.out.println("Failed to print moves");
		}
		return ans;
	}

	//*****************************Getting And Setting Methods*******************************//

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

	public static int getGameLevel(game_service game) {
		String results = game.toString();
		int level = -1;
		try {
			JSONObject levelOb = new JSONObject(results);
			JSONObject ttt = levelOb.getJSONObject("GameServer");
			level = ttt.getInt("game_level");
		}
		catch (Exception e) {
			System.out.println("Failed to get level");
		}
		return level;
	}

	public static int getMaxUserLevel(game_service game) {
		String results = game.toString();
		int maxLevel = -1;
		try {
			JSONObject levelOb = new JSONObject(results);
			JSONObject ttt = levelOb.getJSONObject("GameServer");
			maxLevel = ttt.getInt("max_user_level");
		}
		catch (Exception e) {
			System.out.println("Failed to get max level");
		}
		return maxLevel;
	}

	public static int getGrade(game_service game) {
		String results = game.toString();
		int grade = -1;
		try {
			JSONObject g = new JSONObject(results);
			JSONObject ttt = g.getJSONObject("GameServer");
			grade = ttt.getInt("grade");
		}
		catch (Exception e) {
			System.out.println("Failed to get level");
		}
		return grade;
	}

	public static int getNumbersOfGames() {
		if(userID == -1) {
			System.out.println("Worng id");
		}
		int ans = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);

			while(resultSet.next()) {
				int  id = resultSet.getInt("UserID");
				if(id == userID) {	
					ans++;
				}
			}
			resultSet.close();
			statement.close();		
			connection.close();		
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}

	public static int getBestScore(int level,int id) {
		userID = id;
		if(userID == -1) {
			System.out.println("Worng id");
		}
		int ans = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs where userID="+userID;
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);

			while(resultSet.next()) {
				int currentLev = resultSet.getInt("levelID");
				int currentMoves = resultSet.getInt("moves");;
				int currentScore = resultSet.getInt("score");;

				if(currentLev == level ) {
					if(isGood(currentMoves, level, currentScore)) {
						int temp = currentScore;
						if(ans < temp ) {
							ans = temp;
						}
					}
				}
			}
			resultSet.close();
			statement.close();		
			connection.close();		
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}

	public static boolean isGood(int moves,int level, int score) {
		boolean flag = false;
		if(level == 0) {
			if(moves <= 290 && score >= 125 ) {
				flag =  true;
			}
		}
		else if(level == 1) {
			if(moves <= 580 && score >= 436 ) {
				flag =  true;
			}
		}
		else if(level == 3) {
			if(moves <= 580 && score >= 713 ) {
				flag = true;
			}
		}
		else if(level == 5) {
			if(moves <= 500 && score >= 570) {
				flag = true;
			}
		}
		else if(level == 9) {
			if(moves <= 580 && score >= 480) {
				flag = true;
			}
		}
		else if(level == 11) {
			if(moves <= 580 && score >= 1050 ) {
				flag = true;
			}
		}
		else if(level == 13) {
			if(moves <= 580 && score >= 310 ) {
				flag = true;
			}
		}
		else if(level == 16) {
			if(moves <= 290 && score >= 235 ) {
				flag = true;
			}
		}
		else if(level == 19) {
			if(moves <= 580 && score >= 250 ) {
				flag = true;
			}
		}
		else if(level == 20) {
			if(moves <= 290 && score >= 200) {
				flag = true;
			}
		}
		else {
			if(moves <= 1140 && score >= 1000 ) {
				flag = true;
			}
		}
		return flag;
	}

	public synchronized void ThreadMove(){
		t = new Thread(new Runnable() {
			@Override
			public synchronized void run() {
				while(game.isRunning())
				{
					if(game.isRunning())
					{
						game.move();
					}
					try {
						Thread.sleep(107);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				t.interrupt();
			}
		});
		t.start();
	}

	public static void initTable(int id) {
		id = userID;
		for(int i=0; i<matLevel.length; i++) {
			int level = matLevel[i][0];
			int ans = 0;
			int currentLev = 0;
			int currentMoves = 0;
			int currentScore = 0;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = 
						DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
				Statement statement = connection.createStatement();
				String allCustomersQuery = "SELECT * FROM Logs where userID="+id;
				ResultSet resultSet = statement.executeQuery(allCustomersQuery);
				if(stage.size() != 11) {
					stage.add(0);
					stage.add(1);
					stage.add(3);
					stage.add(5);
					stage.add(9);
					stage.add(11);
					stage.add(13);
					stage.add(16);
					stage.add(19);
					stage.add(20);
					stage.add(23);
				}

				while(resultSet.next()) {
					currentLev = resultSet.getInt("levelID");
					currentMoves = resultSet.getInt("moves");;
					currentScore = resultSet.getInt("score");;

					if(currentLev == level ) {
						if(isGood(currentMoves, level, currentScore)) {
							int temp = currentScore;
							if(ans < temp ) {
								ans = temp;
								matLevel[i][1] = ans;
								matLevel[i][2] = currentMoves;
							}
						}
					}
				}
				resultSet.close();
				statement.close();		
				connection.close();		
			}

			catch (SQLException sqle) {
				System.out.println("SQLException: " + sqle.getMessage());
				System.out.println("Vendor Error: " + sqle.getErrorCode());
			}

			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
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

	public static int getUserID() {
		return userID;
	}

	public static void setUserID(int userID) {
		MyGameGUI.userID = userID;
	}

	public static int getMaxLevelUser() {
		return maxLevelUser;
	}

	public static void setMaxLevelUser(game_service game) {
		int ans = 0;
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("max_user_level");
			ans = rs;	
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		MyGameGUI.maxLevelUser = ans;
	}

	public static void ratingTable() {
		int length = String.valueOf(userID).length();
		if(length == 9) {
			int ans = 0;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = 
						DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
				Statement statement = connection.createStatement();
				String allCustomersQuery = "SELECT * FROM Logs order by UserId,levelID";
				ResultSet resultSet = statement.executeQuery(allCustomersQuery);
				int ind = 0;
				while(resultSet.next()) {
					System.out.println(ind+") Id: " + resultSet.getInt("UserID")+", level: "+resultSet.getInt("levelID")+", score: "+resultSet.getInt("score")+", moves: "+resultSet.getInt("moves")+", time: "+resultSet.getDate("time"));
					ind++;
					//					int currentId = resultSet.getInt("UserID");
					//					int currentLev = resultSet.getInt("levelID");
					//					int currentMoves = resultSet.getInt("moves");;
					//					int currentScore = resultSet.getInt("score");;

					//					if(currentLev == level ) {
					//						if(isGood(currentMoves, level, currentScore)) {
					//							int temp = currentScore;
					//							if(ans < temp ) {
					//								ans = temp;
					//							}
					//						}
					//					}
				}
				resultSet.close();
				statement.close();		
				connection.close();		
			}

			catch (SQLException sqle) {
				System.out.println("SQLException: " + sqle.getMessage());
				System.out.println("Vendor Error: " + sqle.getErrorCode());
			}

			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}

