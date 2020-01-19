package gameClient;

import java.io.File;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONObject;


import Server.game_service;
import dataStructure.DGraph;
import dataStructure.graph;
import de.micromata.opengis.kml.v_2_2_0.Data;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;
import elements.edge_data;
import elements.node_data;
import utils.Point3D;

public class KML_Logger {
	private game_service game;
	private graph graph;
	private Kml k;
	private Document doc;
	DGraph d;

	public KML_Logger(MyGameGUI gui) {
		this.graph = gui.getGraph();
		this.game = gui.getGame();
		this.d = gui.getGraph();
	}

	

	public void setFruits(String time,String end){
		Icon iconGreen = new Icon().withHref("http://maps.google.com/mapfiles/kml/paddle/grn-stars.png");
		Style greenStyle = doc.createAndAddStyle();
		greenStyle.withId("greenI").createAndSetIconStyle().withIcon(iconGreen).withScale(1.2);
		Icon iconYellow = new Icon().withHref("http://maps.google.com/mapfiles/kml/paddle/ylw-stars.png");
		Style yelloStyle = doc.createAndAddStyle();
		yelloStyle.withId("yellowI").createAndSetIconStyle().withIcon(iconYellow).withScale(1.2);
		Icon testRmove = new Icon().withHref("http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png");
		Style removeStyle = doc.createAndAddStyle();
		removeStyle.withId("removeS").createAndSetIconStyle().withIcon(testRmove).withScale(0.0);
		List<String> fruitList = game.getFruits();
		for (String json : fruitList) {
			try {
				JSONObject obj = new JSONObject(json);
				JSONObject CurrFruit = (JSONObject) obj.get("Fruit");
				String pos = CurrFruit.getString("pos");
				String[] arr = pos.split(",");
				double x = Double.parseDouble(arr[0]);
				double y = Double.parseDouble(arr[1]);
				int type = CurrFruit.getInt("type");

				Placemark fr = doc.createAndAddPlacemark();
				if(type == -1)
				{
					fr.setStyleUrl("#greenI");
				}
				else
				{
					fr.setStyleUrl("#yellowI");
				}
				fr.createAndSetPoint().addToCoordinates(x, y);
				fr.createAndSetTimeSpan().withBegin(time).withEnd(end);
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setBots(String time,String end){	
		Icon BusIcon = new Icon().withHref("http://maps.google.com/mapfiles/kml/shapes/bus.png");
		Style busStyle = doc.createAndAddStyle();
		busStyle.withId("Bus").createAndSetIconStyle().withIcon(BusIcon).withScale(1.2);
		List<String> robotList = game.getRobots();
		for (String string : robotList) {
			try {
				JSONObject obj = new JSONObject(string);
				JSONObject CurrBot = (JSONObject) obj.get("Robot");
				String pos = CurrBot.getString("pos");
				String[] arr = pos.split(",");
				double x = Double.parseDouble(arr[0]);
				double y = Double.parseDouble(arr[1]);
				Placemark bot = doc.createAndAddPlacemark();
				bot.setStyleUrl("#Bus");
				bot.createAndSetPoint().addToCoordinates(x, y);
				bot.createAndSetTimeSpan().withBegin(time).withEnd(end);
			}
			catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}

	public game_service getGame() {
		return game;
	}

	public void setGame(game_service game) {
		this.game = game;
	}

	public graph getG() {
		return graph;
	}

	public void setG(graph g) {
		this.graph = graph;
	}

	public Kml getK() {
		return k;
	}

	public void setK(Kml k) {
		this.k = k;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	private Hashtable<String, Integer> read(Feature f){
		Hashtable<String, Integer> answer = new Hashtable<String, Integer>();
		if(f!=null) {
			if(f instanceof Document) {
				Document document = (Document) f;
				ExtendedData  data = document.getExtendedData();
				if(data!=null) {
					List<Data> ld = data.getData();
					for (Data d : ld) {
						answer.put(d.getName(), Integer.parseInt(d.getValue()));
					}
				}

			}
		}
		return answer;
	}

	public void saveToFile(String nameS,String resault){
		try {
			int pGrade = 0;
			int pMoves = 0;
			File tmpDir = new File("data/"+nameS+".kml");
			boolean exists = tmpDir.exists();
			if(exists){
				Kml pKml = Kml.unmarshal(tmpDir);
				Feature feat = pKml.getFeature();
				Hashtable<String, Integer> ans = read(feat);
				pGrade = ans.get("grade");
				pMoves = ans.get("moves");

				JSONObject obj = new JSONObject(resault);
				JSONObject res = (JSONObject) obj.get("GameServer");
				int grade = res.getInt("grade");
				int moves = res.getInt("moves");
				ExtendedData data = doc.createAndSetExtendedData();
				data.createAndAddData(grade+"").setName("grade");
				data.createAndAddData(moves+"").setName("moves");

				if(grade>pGrade){
					k.marshal(tmpDir);
				}

				else{
					if(moves<pMoves){
						k.marshal(tmpDir);
					}
				}
			}

			else {
				JSONObject obj = new JSONObject(resault);
				JSONObject CurrRes = (JSONObject) obj.get("GameServer");
				int grade = CurrRes.getInt("grade");
				int moves = CurrRes.getInt("moves");
				ExtendedData ed = doc.createAndSetExtendedData();
				ed.createAndAddData(grade+"").setName("grade");
				ed.createAndAddData(moves+"").setName("moves");
				k.marshal(tmpDir);
			}
		}

		catch (Exception e) {
			e.printStackTrace();	
		}
	}
	
	public void BuildGraph(){
		k = new Kml();
		doc = k.createAndSetDocument().withName("KML").withOpen(true);
		Folder folder = doc.createAndAddFolder();
		folder.withName("Folder").withOpen(true);

		Icon icon = new Icon().withHref("http://maps.google.com/mapfiles/kml/shapes/parking_lot.png");
		Style placeMarkStyle = doc.createAndAddStyle();
		placeMarkStyle.withId("placemarkid").createAndSetIconStyle().withIcon(icon).withScale(1.2);

		Collection<node_data> nodes = graph.getV();
		for (node_data node_data : nodes) {
			Placemark p = doc.createAndAddPlacemark();
			p.withName(node_data.getKey()+"");
			p.withStyleUrl("#placemarkid");
			p.createAndSetPoint().addToCoordinates(node_data.getLocation().x(), node_data.getLocation().y());
			Style redStyle = doc.createAndAddStyle();
			redStyle.withId("redstyle").createAndSetLineStyle().withColor("ff0000ff").setWidth(3.0);;
		
			Collection<edge_data> edges = graph.getE(node_data.getKey());
			for (edge_data edgess : edges) {
				Placemark p2 = doc.createAndAddPlacemark();
				p2.withStyleUrl("#redstyle");
				Point3D p1  = graph.getNode(edgess.getSrc()).getLocation();
				Point3D locNext = graph.getNode(edgess.getDest()).getLocation();
			
				p2.createAndSetLineString().withTessellate(true).addToCoordinates(p1.x(),p1.y()).addToCoordinates(locNext.x(),locNext.y());
			}
		}
	}
}
