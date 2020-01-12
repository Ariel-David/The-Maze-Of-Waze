package gameClient;

import org.json.JSONArray;
import org.json.JSONObject;
import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import elements.Node;
import elements.fruit;
import elements.robot;
import gui.Graph_GUI;
import oop_dataStructure.OOP_DGraph;
import utils.Point3D;

public class MyGameGUI {
	public static Graph_GUI gui = new Graph_GUI();
	public DGraph dg = new DGraph();
	
	public static void main(String[] a) {
		initGraph();
	}
	public static void initGraph() {
		int scenario_num = 20;
		game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
		String g = game.getGraph();
		String fruitList = game.getFruits().toString();
		JSONObject line1;
		JSONObject line;
		String info = game.toString();
		
		try {
			int src_node = 0; 
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("robots");
			for(int a = 0;a<rs;a++) {
				game.addRobot(src_node+a);
			}
			String robotList = game.getRobots().toString();
			/*Initialise the nodes*/
			line1 = new JSONObject(g);
			JSONArray nodes = line1.getJSONArray("Nodes");
			JSONArray edges = line1.getJSONArray("Edges");
			for(int i=0; i<nodes.length(); i++) {
				JSONObject current = nodes.getJSONObject(i);
				int key = current.getInt("id");
				Object pos = current.get("pos");
				Point3D p = new Point3D(pos.toString());
				Node n = new Node(key, p);
				gui.getGraph().addNode(n);
			}
			/*Initialise the edges*/
			for(int i=0; i<edges.length(); i++) {
				JSONObject current = edges.getJSONObject(i);
				int src = current.getInt("src");
				int dest = current.getInt("dest");
				double w = current.getDouble("w");
				gui.getGraph().connect(src, dest, w);
			}
			
			/*Initialise the robots*/
			JSONArray r = new JSONArray(robotList);
			for(int i=0; i<r.length(); i++) {
				JSONObject current = r.getJSONObject(i);
				JSONObject current2 = current.getJSONObject("Robot");
				int id = current2.getInt("id");
				double value = current2.getDouble("value");
				int src = current2.getInt("src");
				int dest = current2.getInt("dest");
				double speed = current2.getDouble("speed");
				Object pos = current2.get("pos");
				Point3D p = new Point3D(pos.toString());
				robot ro = new robot(id, src, dest, speed, value, p);
				gui.getGraph().addRobot(ro);
			}
			
			/*Initialise the fruits*/
			JSONArray f = new JSONArray(fruitList);
			for(int i=0; i<f.length(); i++) {
				JSONObject current = f.getJSONObject(i);
				JSONObject current2 = current.getJSONObject("Fruit");
				int type = current2.getInt("type");
				double value = current2.getDouble("value");
				Object pos = current2.get("pos");
				Point3D p = new Point3D(pos.toString());
				fruit fu = new fruit(type, value, p);
				gui.getGraph().addFruit(fu);
			}
			gui.drawGraph();
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
}
