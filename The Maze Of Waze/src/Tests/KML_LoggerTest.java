package Tests;

import gameClient.KML_Logger;
import org.junit.Test;

import Server.Game_Server;
import Server.game_service;

import static org.junit.Assert.*;

public class KML_LoggerTest {
	private game_service game = Game_Server.getServer(0);
	int i=0;
	
    @Test
    public void makeKML()  {
        boolean checkSave = true;
        try {
        	KML_Logger kml = new KML_Logger(i,game);
        	kml.KML_Stop();
        }
        catch (Exception e){
        	checkSave=false;}

        assertEquals(true,checkSave);

    }
}