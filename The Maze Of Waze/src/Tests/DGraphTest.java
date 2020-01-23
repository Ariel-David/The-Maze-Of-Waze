package Tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dataStructure.DGraph;
import dataStructure.graph;
import elements.Edge;
import elements.Node;
import utils.Point3D;

public class DGraphTest {

	@Test
	public void testAddNode() {
		DGraph g = new DGraph();
		g.addNode(new Node());
		DGraph g1  = new DGraph();
		assertNotEquals(g1.countNode, g.countNode);
	}

	@Test
	public void testConnect() {
		DGraph g = new DGraph();
		Node n1 = new Node(5,0,0,new Point3D(3,4),"");
		Node n2 = new Node(8,0,0,new Point3D(1,4),"");
		g.addNode(n1);
		g.addNode(n2);
		Edge e = new Edge(5, 8, 7);
		g.connect(n1.getKey(), n2.getKey(), 7);
		assertEquals(e.getDest(),n1.edges.get(e.getDest()).getDest());
	}

	@Test
	public void testRemoveNode() {
		DGraph g1 = new DGraph();
		DGraph g2 = new DGraph();
		Node n1 = new Node(5,0,0,new Point3D(3,4),"");
		Node n2 = new Node(8,0,0,new Point3D(1,4),"");
		g1.addNode(n1);
		g1.addNode(n2);
		g2.addNode(n1);
		g2.addNode(n2);
		g2.removeNode(n1.getKey());
		assertNotEquals(g1.countNode, g2.countNode);
	}

	@Test
	public void testRemoveEdge() {
		DGraph g1 = new DGraph();
		DGraph g2 = new DGraph();
		Node n1 = new Node(5,0,0,new Point3D(3,4),"");
		Node n2 = new Node(8,0,0,new Point3D(1,4),"");
		Node n3 = new Node(5,0,0,new Point3D(3,4),"");
		Node n4 = new Node(8,0,0,new Point3D(1,4),"");
		g1.addNode(n1);
		g1.addNode(n2);
		
		g2.addNode(n3);
		g2.addNode(n4);
		
		g1.connect(n1.getKey(), n2.getKey(), 7);
		g2.connect(n3.getKey(), n4.getKey(), 7);
		
		g2.removeEdge(n3.getKey(), n4.getKey());
		assertNotEquals(g1.countEdge, g2.countEdge);
	}

	@Test
	public void testCrazy() {
		boolean flag = false;
		DGraph c = DGraph.createCrazy();
		int s = c.getV().size();
		if(1000000 == s) {
			flag = true;
		}
		assertTrue(flag);
	}

	
}
