package gameClient;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * this class create 24 kml files that can be loaded to google earth and view the game
 * in a specific level
 * this is only for auto game.
 */


class KML_Logger {

    private int level;
    private StringBuffer str;

    /**
     * simple constructor
     * @param level
     */
    KML_Logger(int level) {
        this.level = level;
        str = new StringBuffer();
        //KML_Play();
        KML_Start();
    }
    
    /**
     * this function initialise the working platform to KML
     */
    private void KML_Start(){
        str.append(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                        "<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" +
                        "  <Document>\r\n" +
                        "    <name>" + "Game stage :"+level + "</name>" +"\r\n"
        );
        KML_node();
    }
    
    /**
     * this function initialise the node icon to KML
     */
    private void KML_node(){
        str.append(" <Style id=\"node\">\r\n" +
                "      <IconStyle>\r\n" +
                "        <Icon>\r\n" +
                "          <href>http://maps.google.com/mapfiles/kml/pal3/icon35.png</href>\r\n" +
                "        </Icon>\r\n" +
                "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                "      </IconStyle>\r\n" +
                "    </Style>"
        );
        KML_Fruit();
    }
    
    /**
     * this function initialise the Fruits icon to KML (Type 1 and -1)
     */
    private void KML_Fruit(){
        str.append(
                " <Style id=\"fruit_-1\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/paddle/purple-stars.png</href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>" +
                        " <Style id=\"fruit_1\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/paddle/red-stars.png</href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>"
        );
        KML_Robot();
    }
    
    /**
     * this function initialise the Robots icon to KML
     */
    private void KML_Robot(){
        str.append(
                " <Style id=\"robot\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/shapes/motorcycling.png></href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>"
        );
    }

    /**
     * this function is used in "paint"
     * after painting each element
     * the function enters the kml the location of each element
     * @param id
     * @param location
     */
    void Place_Mark(String id, String location){
        LocalDateTime Present_time = LocalDateTime.now();
        str.append(
                "    <Placemark>\r\n" +
                        "      <TimeStamp>\r\n" +
                        "        <when>" + Present_time+ "</when>\r\n" +
                        "      </TimeStamp>\r\n" +
                        "      <styleUrl>#" + id + "</styleUrl>\r\n" +
                        "      <Point>\r\n" +
                        "        <coordinates>" + location + "</coordinates>\r\n" +
                        "      </Point>\r\n" +
                        "    </Placemark>\r\n"
        );
    }

    
    void KML_Stop(){
        str.append("  \r\n</Document>\r\n" +
                "</kml>");
        SaveFile();
    }

   
    private void SaveFile(){
        try {
            File file=new File("data/"+level+".kml");
            PrintWriter pw=new PrintWriter(file);
            pw.write(str.toString());
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}