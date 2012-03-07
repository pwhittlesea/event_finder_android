package eventlocator.android.data;


import java.util.ArrayList;

import eventlocator.android.R;



public class ServerConnection {
	ArrayList<Event> events;
	
	public ServerConnection(String url){
		String jsonFromServer;
		events = new ArrayList<Event>();
		try {
		
			jsonFromServer = JSONClient.connect(url);
			JSONToObjects  jsonToObjects = new JSONToObjects();
			jsonToObjects.init(jsonFromServer);
			events = jsonToObjects.findAll();
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
	}
	public ArrayList<Event> getEvents() {
		return events;
	}
	

}
