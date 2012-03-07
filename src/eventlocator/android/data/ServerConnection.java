package eventlocator.android.data;

import java.util.ArrayList;

public class ServerConnection {
	ArrayList<Event> events;
/**
 * The class used by the activity to get the events
 * @param url
 */
	public ServerConnection(String url) {
		String jsonFromServer;
		events = new ArrayList<Event>();
		try {
			jsonFromServer = JSONClient.connect(url);
			JSONToObjects jsonToObjects = new JSONToObjects();
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
