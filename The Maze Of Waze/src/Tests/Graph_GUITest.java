package Tests;


import org.junit.jupiter.api.Test;

import algorithms.Graph_Algo;
import elements.Node;
import gui.Graph_GUI;
import utils.Point3D;

class Graph_GUITest {
	@Test
	void testDrawGraph() {
		Graph_Algo ga = new Graph_Algo();
		ga.graph.addNode(new Node(0,0,0,new Point3D(0, 0),""));
		ga.graph.addNode(new Node(1,0,0,new Point3D(-20,-10), ""));
		ga.graph.addNode(new Node(2,0,0,new Point3D(15,30), ""));
		ga.graph.addNode(new Node(3,0,0,new Point3D(-30,10), ""));
		ga.graph.addNode(new Node(4,0,0,new Point3D(0,-20), ""));
		ga.graph.addNode(new Node(5,0,0,new Point3D(17, -10),""));

		ga.graph.connect(0,1, 1);
		ga.graph.connect(3,0, 1.5);
		ga.graph.connect(0,2, 0);
		ga.graph.connect(1,0, 2);
		ga.graph.connect(0,4, 2);
		ga.graph.connect(4,3, 3);
		ga.graph.connect(2,5, 1.2);
		ga.graph.connect(5,1, 2.5);

		Graph_GUI gui = new Graph_GUI(ga.graph);
		gui.drawGraph();

	}
}
