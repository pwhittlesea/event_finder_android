package eventlocator.android.data;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

import android.util.Log;

import com.google.android.maps.GeoPoint;

public class ServerConnection {
	ArrayList<Event> events;

	/**
	 * The class used by the activity to get the events
	 * 
	 * @param url
	 */
	public ServerConnection(String url, SpecialGeoPoint geoPoint) {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = jsonFromObject(geoPoint);
		json = URLEncoder.encode("{\"geo\":{\"lat\":50.9358682,\"long\":-1.3988324}}");
				//URLEncoder.encode(json.toString());
		System.out.println("server url" + url);
		String jsonFromServer;
	
		String jsonUrl = url + "?req=" + json;
		//String jsonUrl = url + "?req={\"geo\":{\"lat\":50.9358682,\"long\":-1.3988324}}";
		System.out.println("json " + jsonUrl);
		events = new ArrayList<Event>();
		try {
			jsonFromServer = JSONClient.connect(jsonUrl);
			JSONToObjects jsonToObjects = new JSONToObjects();
			jsonToObjects.init(jsonFromServer);
			events = jsonToObjects.findAll();

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static String jsonFromObject(Object object) {

		final ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();

		try {
			mapper.writeValue(writer, object);

		} catch (RuntimeException e) {
			throw e;

		} catch (Exception e) {

			Log.e("jsonfromobj", "Unable to serialize to json: " + object, e);

			return null;
		}
		return writer.toString();
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

}
