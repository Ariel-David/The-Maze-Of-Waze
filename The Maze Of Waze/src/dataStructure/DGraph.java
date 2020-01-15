package dataStructure;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import elements.Edge;
import elements.Node;
import elements.edge_data;
import elements.fruit;
import elements.node_data;
import elements.robot;
import gui.Graph_GUI;
import utils.Point3D;



public class DGraph implements graph, Serializable{
	public Map<Integer, node_data> graph = new HashMap<Integer,node_data>();
	public ArrayList<robot> robots = new ArrayList<robot>();
	public ArrayList<fruit> fruits = new ArrayList<fruit>();
	public int countFruits = 0;
	public int countNode = 0;
	public int countEdge = 0;
	public int ModeCount = 0;

	@Override
	public node_data getNode(int key) {
		if(graph.get(key) == null) {
			return null;
		}
		return (node_data) this.graph.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		if((graph.get(src) == null) && (graph.get(dest) == null)){
			return null;
		}	
		return ((Node)graph.get(src)).edges.get(dest);
	}

	@Override
	public void addNode(node_data n) {
		if(graph.containsKey(n.getKey())) {
			throw new RuntimeException("This vertex with the same key is already exist");
		}
		graph.put(n.getKey(),n); 
		countNode++;
		ModeCount++;
	}
	
	public void addRobot(robot r) {
		robots.add(r);
		ModeCount++;
	}

	public void addFruit(fruit f) {
		fruits.add(f);
		countFruits++;
		ModeCount++;
	}

	@Override
	public void connect(int src, int dest, double w) {
		if(((Node)graph.get(src)).edges.containsKey(dest)) {
			throw new RuntimeException("This edge is already exist");
		}

		if((graph.get(src)==null) || (graph.get(dest)==null)) {
			throw new RuntimeException("Invalid input");
		}

		Edge e = new Edge(src, dest, w);
		((Node)graph.get(src)).edges.put(dest, e);
		countEdge++;
		ModeCount++;
	}

	@Override
	public Collection<node_data> getV() {
		return graph.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		return ((Node)graph.get(node_id)).edges.values();
	}

	@Override
	public node_data removeNode(int key) {
		if(!graph.containsKey(key)) {
			return null;
		}
		else {
			node_data n = new Node();
			Iterator<Integer> iter = graph.keySet().iterator();
			while(iter.hasNext()) {
				removeEdge(iter.next(), key);
			}
			graph.put(key, null);
			n = this.graph.remove(key);
			countNode--;
			ModeCount++;
			return n;
		}
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		if(((Node)graph.get(src)).edges.containsKey(dest)) {
			edge_data e = ((Node)graph.get(src)).edges.remove(dest);
			countEdge--;
			ModeCount++;
			return e;
		}
		else {
			return null;
		}	
	}

	@Override
	public int nodeSize() {
		return countNode;
	}

	@Override
	public int edgeSize() {
		return countEdge;
	}

	@Override
	public int getMC() {
		return ModeCount;
	}
	
	public void initGraph(game_service game) { 
		String g = game.getGraph();
		String fruitList = game.getFruits().toString();
		JSONObject line1;
		JSONObject line;
		String info = game.toString();

		try {
			int src_node = 0; 
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");

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
				this.addNode(n);
			}
			/*Initialise the edges*/
			for(int i=0; i<edges.length(); i++) {
				JSONObject current = edges.getJSONObject(i);
				int src = current.getInt("src");
				int dest = current.getInt("dest");
				double w = current.getDouble("w");
				this.connect(src, dest, w);
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
				this.addFruit(fu);
			}	
		}
		catch (Exception e) {
		}
	}

}
