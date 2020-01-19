package gameClient;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import Server.game_service;
import dataStructure.DGraph;
import elements.Edge;
import elements.Node;
import elements.edge_data;
import elements.fruit;
import elements.node_data;
import elements.robot;
import utils.StdDraw;

public class AutoGame {
	static game_service game;
	static double Epsilon = 0.000001;

	/**
	 * this function put the robot on the graph according to where fruits are
	 * @param arr
	 * @param game
	 * @param graph
	 */
	public static void putRobot(fruit [] arr,game_service game,DGraph graph) {
		try {
			edge_data e = new Edge();
			node_data n = new Node();
			int num = MyGameGUI.getRobotNumber();
			for(int i=0; i<num; i++) {
				e = MyGameGUI.findEdge(arr[i]);
				n = graph.getNode(e.getSrc());
				robot r = new robot(i, n.getKey(), -1,1, 0.0, n.getLocation());
				StdDraw.picture(n.getLocation().x(), n.getLocation().y(),"data\\monkey.png",0.00131,0.000122);
				graph.addRobot(r);
				game.addRobot(r.getSrc());
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * this algorithm moving each of the robot after finding the worth way to a specific fruit
	 * in order to find the worth way i used the shorterPathDist() function.
	 * @param game
	 * @param graph
	 */
	public static void moveRobotsAuto(game_service game,DGraph graph) {
		for(int i=0; i<graph.robots.size(); i++) {
			if(graph.robots.get(i).getDest() == -1) {
				List<node_data> path = new ArrayList<node_data>();
				int dest = 0;
				int src = graph.robots.get(i).getSrc();
				edge_data e = new Edge();
				double wayCost = Double.MAX_VALUE;
				for(int j=0; j<graph.fruits.size(); j++) {
					e = MyGameGUI.findEdge(graph.fruits.get(j));
					double temp = MyGameGUI.shortestPathDist(src, e.getSrc());
					if(temp < wayCost) {
						wayCost = temp;
						dest = e.getSrc();
						path = MyGameGUI.shortestPath(src, dest);
						path.add(graph.getNode(e.getDest()));
						path.remove(0);
					}
				}
				dest = path.get(0).getKey();
				if(dest == graph.robots.get(i).getSrc()) {
				}
				game.chooseNextEdge(graph.robots.get(i).getId(),dest);
			}
		}
		game.move();

	}

	/**
	 * this function is sorting the fruit list according to values
	 * this help to put in the start of the game the robot on the vertex that are
	 * close to fruits that have a big value
	 * @param list
	 * @return
	 */
	public static fruit[] sortByValue(List<fruit> list) {
		fruit [] arr = new fruit[list.size()];
		for (int i=0; i<arr.length; i++) {
			arr[i] = list.get(i);
		}
		bubbleSort(arr);
		return arr;
	}

	public static void bubbleSort(fruit arr[]){ 
		int n = arr.length; 
		for (int i = 0; i < n-1; i++) { 
			for (int j = 0; j < n-i-1; j++) {
				if (arr[j].getValue() > arr[j+1].getValue()) {  
					fruit temp = arr[j]; 
					arr[j] = arr[j+1]; 
					arr[j+1] = temp; 
				} 
			}
		}
	}
}
