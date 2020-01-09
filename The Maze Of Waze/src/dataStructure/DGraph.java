package dataStructure;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import elements.Edge;
import elements.Node;
import elements.edge_data;
import elements.fruit;
import elements.node_data;
import elements.robot;



public class DGraph implements graph, Serializable{
	public Map<Integer, node_data> graph = new HashMap<Integer,node_data>();
	public Map<Integer, robot> robots = new HashMap<Integer,robot>();
	public Map<Integer, fruit> fruits = new HashMap<Integer,fruit>();
	public int countFruits = 0;
	public int countNode = 0;
	public int countRobots = 0;
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
		robots.put(r.getId(),r); 
		countRobots++;
		ModeCount++;
	}
	
	public void addFruit(fruit f) {
		fruits.put(f.getType(),f); 
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

}
