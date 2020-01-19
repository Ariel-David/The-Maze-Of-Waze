package gameClient;

import java.awt.event.KeyEvent;

import org.json.JSONException;

import Server.game_service;
import dataStructure.DGraph;
import elements.Node;
import elements.edge_data;
import elements.node_data;
import elements.robot;
import utils.Point3D;
import utils.StdDraw;

public class ManualGame {
	private static DGraph graph;
	static game_service game;
	static double Epsilon = 0.0001;
	private static int key = -1;
	
	public static void drawRobotManual(DGraph graph, game_service game) throws JSONException {
		int i = 0;
		int num = MyGameGUI.getRobotNumber();
		while(i < num) {
			if(StdDraw.isMousePressed() == true) {
				StdDraw.isMousePressed = false;
				for (node_data currentNode : graph.getV()) {
					if(((StdDraw.mouseX()-Epsilon) <= currentNode.getLocation().x()) && 
							((StdDraw.mouseX()+Epsilon) >= currentNode.getLocation().x())&& 
							((StdDraw.mouseY()-Epsilon) <= currentNode.getLocation().y()) &&
							((StdDraw.mouseY()+Epsilon) >= currentNode.getLocation().y())) {
						Point3D p = new Point3D(currentNode.getLocation().x(),currentNode.getLocation().y(),0);
						robot r = new robot(i,currentNode.getKey(), -1,1, 0.0, p);
						StdDraw.picture(p.x(), p.y(),"data\\monkey.png",0.00100,0.00080);
						graph.addRobot(r);
						game.addRobot(currentNode.getKey());
						i++;
					}
				}
			}
		}
	}
	
	public static void moveRobotsManual(int next,game_service game, DGraph graph) throws JSONException {
		int index = select();
		if (index!=-1) {
			if(index < graph.robots.size()) {
				int dest = nextNodeMenual(graph,graph.robots.get(index).getSrc(),game);
				game.chooseNextEdge(graph.robots.get(index).getId(),dest);
			}
			else {
				index = -1;
			}
		}
		game.move();
	}
	
	public static int nextNodeMenual(DGraph graph, int src,game_service game) throws JSONException {
		int dest = -1;
		if(StdDraw.isMousePressed()) {
			StdDraw.isMousePressed = false;
			double x = StdDraw.mouseX();
			double y = StdDraw.mouseY();
			for (edge_data currentEdge : ((Node)graph.getNode(src)).edges.values()) {
				if(((x-Epsilon) <= graph.getNode(currentEdge.getDest()).getLocation().x()) && 
						((x+Epsilon) >= graph.getNode(currentEdge.getDest()).getLocation().x())&& 
						((y-Epsilon) <= graph.getNode(currentEdge.getDest()).getLocation().y()) &&
						((y+Epsilon) >= graph.getNode(currentEdge.getDest()).getLocation().y())) {
					dest = graph.getNode(currentEdge.getDest()).getKey();
				}
			}
		}
		return dest;
	}
	
	public static int select() {
		if(StdDraw.isKeyPressed(KeyEvent.VK_0)) {
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
}

