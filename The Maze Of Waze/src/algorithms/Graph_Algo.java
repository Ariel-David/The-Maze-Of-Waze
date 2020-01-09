package algorithms;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import dataStructure.DGraph;
import dataStructure.graph;
import elements.Edge;
import elements.Node;
import elements.edge_data;
import elements.node_data;
import utils.StdDraw;
/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author 
 *
 */
public class Graph_Algo implements graph_algorithms,Serializable{
	public graph graph;

	/**
	 * Default constructor
	 */
	public Graph_Algo() {
		this.graph = new DGraph();
	}

	/**
	 * copy constructor
	 * @param g.
	 */
	public Graph_Algo(graph g) {
		this.graph = g;
	}


	@Override
	public void init(graph g) {
		this.graph = (DGraph) g;
	}

	@Override
	public void init(String file_name) {
		try {    
			FileInputStream file = new FileInputStream(file_name); 
			ObjectInputStream in = new ObjectInputStream(file);  
			graph = (graph) in.readObject(); 
			in.close(); 
			file.close();   
			System.out.println("Object has been deserialized"); 

		} 

		catch(IOException ex) 
		{ 
			System.out.println("IOException is caught"); 
		} 

		catch(ClassNotFoundException ex) 
		{ 
			System.out.println("ClassNotFoundException is caught"); 
		} 		
	}

	@Override
	public void save(String file_name) {
		try {    
			FileOutputStream file = new FileOutputStream(file_name); 
			ObjectOutputStream out = new ObjectOutputStream(file); 

			out.writeObject(graph);
			out.close(); 
			file.close();  
			System.out.println("Object has been serialized"); 
		}   
		catch(IOException ex)  { 
			System.out.println("IOException is caught"); 
		}
	}


	@Override
	public boolean isConnected() {
		if(this.graph.nodeSize() <= 1) {
			return true;
		}
		boolean flag = false;
		Stack<node_data> s = new Stack<node_data>();
		Iterator<node_data> iterNode = graph.getV().iterator();
		while(iterNode.hasNext()) {
			for (node_data nodes : graph.getV()) {
				nodes.setTag(0);
			}
			node_data currentNode = iterNode.next();
			s.add(currentNode);
			currentNode.setTag(1);
			if(neiburs(currentNode,s).isEmpty()) {
				flag = true;
			}
			else {
				flag = false;
			}
		}
		return flag;
	}

	private Stack<node_data> neiburs(node_data currentNode, Stack<node_data> s) {
		Iterator<edge_data> iterEdge = graph.getE(currentNode.getKey()).iterator();
		if(s.isEmpty()) {
			return s;
		}
		else {
			while(iterEdge.hasNext()) {
				edge_data e = iterEdge.next();
				if(graph.getNode(e.getDest()).getTag() == 0 ) {
					s.push(graph.getNode(e.getDest()));
					graph.getNode(e.getDest()).setTag(1);
					neiburs(graph.getNode(e.getDest()), s);
				}
			}
			s.pop();
		}
		return s;
	}

	@Override
	public double shortestPathDist(int src, int dest) {
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

	private void shortestPathDistHelper(int src, int dest, String s) {
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

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		List<node_data> visited = new ArrayList<>();
		if(shortestPathDist(src, dest) == Double.POSITIVE_INFINITY) {
			return null;
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

	@Override
	public List<node_data> TSP(List<Integer> targets) {
		List<node_data> Nodes = new ArrayList<node_data>();
		List<node_data> temp = new ArrayList<node_data>();
		List<node_data> ans = new ArrayList<node_data>();

		for (Integer key : targets) {
			if(graph.getNode(key)==null) {
				return null;
			}
			Nodes.add(graph.getNode(key));
		}

		for(int i=0; i<Nodes.size()-1; i++) {
			List<node_data> list = new ArrayList<node_data>();

			list = (ArrayList<node_data>) shortestPath(Nodes.get(i).getKey(), Nodes.get(i+1).getKey());

			if(list==null) {
				return null;
			}
			for(int j=0; j<list.size(); j++) {
				temp.add(list.get(j));
			}
		}
		if(temp.size() % 2 == 0) {
			for(int i=0; i<temp.size(); i++) {
				if(i == temp.size()-1) {
					if(temp.get(i-1) != temp.get(i)) {
						ans.add(temp.get(i));
					}
				}
				else if(temp.get(i) == temp.get(i+1)) {
					ans.add(temp.get(i));
					i++;
				}
				else {
					ans.add(temp.get(i));
				}
			}
		}
		else {
			for(int i=0; i<temp.size()-1; i++) {
				if(i == temp.size()-2) {
					if(temp.get(i) == temp.get(i+1)) {
						ans.add(temp.get(i));
						i++;
					}
					else {
						ans.add(temp.get(i));
						ans.add(temp.get(i+1));
					}
				}

				if(temp.get(i) == temp.get(i+1)) {
					ans.add(temp.get(i));
					i++;
				}
				else {
					ans.add(temp.get(i));
				}
			}
		}
		return ans;	
	}

	@Override
	public graph copy() {
		Graph_Algo ga = new Graph_Algo();
		this.save("temp.txt");
		ga.init("temp.txt");
		return ga.graph;
	}
}
