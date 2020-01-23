package gameClient;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.graph;
import elements.Edge;
import elements.Fruit_Comperator;
import elements.Node;
import elements.edge_data;
import elements.fruit;
import elements.node_data;
import elements.robot;
import utils.StdDraw;

public class AutoGame {
	static game_service game;
	static double Epsilon = 0.000001;
	static Fruit_Comperator c = new Fruit_Comperator();

	/**
	 * this function put the robot on the graph according to where fruits are
	 * @param arr
	 * @param game
	 * @param graph
	 */
	public static void putRobot(game_service game,DGraph graph) {
		try {
			//graph.fruits.sort(c);
			edge_data e = new Edge();
			node_data n = new Node();
			int num = MyGameGUI.getRobotNumber();
			for(int i=0; i<num; i++) {
				e = MyGameGUI.findEdge(graph.fruits.get(i));
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
		graph.fruits.sort(c);
		fruit f = new fruit();
		for(int i=0; i<graph.robots.size(); i++) {
			if(graph.robots.get(i).getDest() == -1) {
				List<node_data> path = new ArrayList<node_data>();
				int dest = 0;
				int src = graph.robots.get(i).getSrc();
				edge_data e = new Edge();
				double wayCost = Double.MAX_VALUE;
				for(int j=0; j<graph.fruits.size(); j++) {
					f = graph.fruits.get(j);
					e = MyGameGUI.findEdge(f);
					double temp = shortestPathDist(game,graph,src, e.getSrc());
					if(temp < wayCost) {
						wayCost = temp;
						dest = e.getSrc();
						path = shortestPath(game,graph,src, dest);
						path.add(graph.getNode(e.getDest()));
						path.remove(0);
						//graph.fruits.remove(f);
					}
				}
				dest = path.get(0).getKey();
				//			double disFromFruit = MyGameGUI.distance(graph.robots.get(i).getPos(), f.pos);
				//			if(disFromFruit < 0.006) {
				//				MyGameGUI.sleep = 40;
				//			}
				//			else {
				//				MyGameGUI.sleep = 70;
				//			}
				game.chooseNextEdge(graph.robots.get(i).getId(),dest);
			}
		}
	}

	public static void moveRobotsAuto1(game_service game,DGraph graph,int index) {
		List<fruit> lis;
		List<fruit> lisTemp = rightZone(graph.fruits);
		if(lisTemp.isEmpty()) {
			lis = graph.fruits;
		}
		else {
			lis = rightZone(graph.fruits);	
		}
		if(graph.robots.get(index).getDest() == -1) {
			List<node_data> path = new ArrayList<node_data>();
			fruit f = new fruit();
			int dest = 0;
			int src = graph.robots.get(index).getSrc();
			edge_data e = new Edge();
			double wayCost = Double.MAX_VALUE;
			for(int j=0; j<lis.size(); j++) {
				f = lis.get(j);
				e = MyGameGUI.findEdge(f);
				double temp = shortestPathDist(game,graph,src, e.getSrc());
				if(temp < wayCost) {
					wayCost = temp;
					dest = e.getSrc();
					path = shortestPath(game,graph,src, dest);
					path.add(graph.getNode(e.getDest()));
					path.remove(0);
					//	graph.fruits.remove(f);
				}
			}
			dest = path.get(0).getKey();
			double disFromFruit = MyGameGUI.distance(graph.robots.get(index).getPos(), f.pos);
			if(disFromFruit < 0.006) {
				MyGameGUI.setSleep(40);
			}
			else {
				MyGameGUI.setSleep(70);
			}
			game.chooseNextEdge(graph.robots.get(index).getId(),dest);
		}
	}

	public static void moveRobotsAuto2(game_service game,DGraph graph,int index) {
		List<fruit> lis;
		List<fruit> lisTemp = leftZone(graph.fruits);
		if(lisTemp.isEmpty()) {
			lis = graph.fruits;
		}
		else {
			lis = leftZone(graph.fruits);	
		}
		if(graph.robots.get(index).getDest() == -1) {
			List<node_data> path = new ArrayList<node_data>();
			fruit f = new fruit();
			int dest = 0;
			int src = graph.robots.get(index).getSrc();
			edge_data e = new Edge();
			double wayCost = Double.MAX_VALUE;
			for(int j=0; j<lis.size(); j++) {
				f = lis.get(j);
				e = MyGameGUI.findEdge(f);
				double temp = shortestPathDist(game,graph,src, e.getSrc());
				if(temp < wayCost) {
					wayCost = temp;
					dest = e.getSrc();
					path = shortestPath(game,graph,src, dest);
					path.add(graph.getNode(e.getDest()));
					path.remove(0);
					//	graph.fruits.remove(f);
				}
			}
			dest = path.get(0).getKey();
			double disFromFruit = MyGameGUI.distance(graph.robots.get(index).getPos(), f.pos);
			if(disFromFruit < 0.006) {
				MyGameGUI.setSleep(40);
			}
			else {
				MyGameGUI.setSleep(70);
			}
			game.chooseNextEdge(graph.robots.get(index).getId(),dest);
		}
	}


	private static List<fruit> leftZone(ArrayList<fruit> list){
		ArrayList<fruit> left = new ArrayList<fruit>();
		for(fruit f: list) {
			if(f.getPos().x()<MyGameGUI.getGraph().getNode(7).getLocation().x()) 
				left.add(f);	

		}
		return left;
	}

	private static List<fruit> rightZone(ArrayList<fruit> list){
		ArrayList<fruit> right = new ArrayList<fruit>();
		for(fruit f: list) {
			if(f.getPos().x()>=MyGameGUI.getGraph().getNode(7).getLocation().x()) 
				right.add(f);

		}
		return right;
	}

	/**
	 * finding the shortest path between src and dest and return the sum of the sum of the path
	 * @param src
	 * @param dest
	 * @return
	 */
	public static double shortestPathDist(game_service game, DGraph graph,int src, int dest) {
		String s = "";
		if(src == dest) {
			return 0;
		}
		for(node_data vertex : graph.getV()) {
			vertex.setWeight(Double.POSITIVE_INFINITY);
			vertex.setTag(0);
		}
		graph.getNode(src).setWeight(0);;
		shortestPathDistHelper(game,graph,src,dest,s);
		return graph.getNode(dest).getWeight();
	}

	public static void shortestPathDistHelper(game_service game, DGraph graph,int src, int dest, String s) {
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
				shortestPathDistHelper(game,graph,edges.getDest(), dest , s + "->" +src);
			}
		}
	}

	/**
	 * return a list of nodes of the shorter path between src and dest 
	 * @param src
	 * @param dest
	 * @return
	 */
	public static List<node_data> shortestPath(game_service game, DGraph graph, int src, int dest) {		
		List<node_data> visited = new ArrayList<>();
		for (node_data node_data : graph.getV()) {
			node_data.setInfo("");
		}

		if(AutoGame.shortestPathDist(game,graph,src, dest) == Double.POSITIVE_INFINITY) {
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
}