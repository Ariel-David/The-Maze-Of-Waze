package gameClient;

import java.util.ArrayList;
import java.util.List;

import Server.game_service;
import dataStructure.DGraph;
import dataStructure.graph;
import elements.Node;
import elements.fruits;
import elements.robots;
import utils.Point3D;

public class MyGameGUI {
	graph g = new DGraph();
	public List<robots> robotList = new ArrayList<robots>();
	public List<fruits> fruitList = new ArrayList<fruits>();

	public MyGameGUI(game_service game) {
		
		/*Initialise robots from the string */
		robotsFromString(game.getRobots());
		
		/*Initialise fruits from the string */
		furitsFromString(game.getFruits());
		
	}
	public void robotsFromString(List<String> list) {
		for(int i=0; i<list.size(); i++) {
			robots r = new robots();
			String s = list.get(i);
			String [] s1 = s.split(":");
			r.src = Character.getNumericValue(s1[4].charAt(0));
			String [] pos = s1[7].split(",");
			String [] id = s1[2].split(",");
			String [] dest = s1[5].split(",");
			String [] speed = s1[6].split(",");
			String [] value = s1[3].split(",");
			r.pos = new Point3D(Float.parseFloat(pos[0].replace('"', ' ')),Float.parseFloat(pos[1]), Float.parseFloat(pos[2].substring(0, 2)));
			r.id = Integer.parseInt(id[0]);
			r.dest = Integer.parseInt(dest[0]);
			r.speed = Double.parseDouble((speed[0]));
			r.value = Double.parseDouble((value[0]));
			robotList.add(r);
		}
	}
	public void furitsFromString(List<String> list) {
		for(int i=0; i<list.size(); i++) {
			fruits f = new fruits();
			String s = list.get(i);
			String [] s1 = s.split(":");
			String [] value = s1[2].split(",");
			String [] type = s1[3].split(",");
			f.value = Double.parseDouble((value[0]));
			String [] pos = s1[4].split(",");
			f.pos = new Point3D(Float.parseFloat(pos[0].replace('"', ' ')),Float.parseFloat(pos[1]), Float.parseFloat(pos[2].substring(0, 2)));
			f.type = Integer.parseInt(type[0]);
			fruitList.add(f);
		}
	}
}
