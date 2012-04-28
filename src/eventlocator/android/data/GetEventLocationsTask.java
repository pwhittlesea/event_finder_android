package eventlocator.android.data;

import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import eventlocator.android.EventItemizedOverlay;

public class GetEventLocationsTask {

	EventItemizedOverlay itemizedoverlay;
	Context context;

	/**
	 * The class used by the activity to get the events
	 * 
	 * @param url
	 * @param context
	 */
	public GetEventLocationsTask(String url, SpecialGeoPoint geoPoint,
			EventItemizedOverlay itemizedoverlay, Context context) {
		this.itemizedoverlay = itemizedoverlay;
		this.context = context;

		String json = jsonFromObject(geoPoint);
		System.out.println("geo json " + json);
		json = "{\"geo\":" + json + "}";
		json = URLEncoder.encode(json);

		System.out.println("server url" + url);

		String jsonUrl = url + "?req=" + json;

		new GetJSONTask().execute(jsonUrl, null, null);

	}

	private class GetJSONTask extends
			AsyncTask<String, Void, ArrayList<EventLocation>> {
		protected ArrayList<EventLocation> doInBackground(String... jsonUrl) {
			ArrayList<EventLocation> subList = new ArrayList<EventLocation>();

			String jsonFromServer;
			ArrayList<EventLocation> eventLocations = new ArrayList<EventLocation>();
			try {
				jsonFromServer = JSONClient.connect(jsonUrl[0]);
				Log.d("jsonFromServer", jsonFromServer);
				JSONToObjects<EventLocation, EventLocations> jsonToObjects = new JSONToObjects<EventLocation, EventLocations>(
						EventLocation.class, EventLocations.class);
				jsonToObjects.init(jsonFromServer);
				eventLocations = jsonToObjects.findAll();

				Log.d("getEventLocations", "Found " + eventLocations.size());
			} catch (Exception e1) {
				Log.e("getEvents()", "Couldn't get events from server");
				e1.printStackTrace();
			}
			if (eventLocations.size() > 150) {
				subList.addAll(eventLocations.subList(0, 150));
			} else {
				return eventLocations;
			}
			return subList;
		}

		protected void onProgressUpdate(Void... progress) {

		}

		protected void onPostExecute(ArrayList<EventLocation> result) {

			if (result.size() == 0) {
				Log.d("getEventLocations", "No Events available");
				Toast.makeText(context, "No events near your location",
						Toast.LENGTH_LONG).show();

			} else {

			}
			for (EventLocation eventLocation : result) {
				GeoPoint point = new GeoPoint(
						(int) (eventLocation.getLat() * 1E6),
						(int) (eventLocation.getLong() * 1E6));
				OverlayItem overlayitem = new OverlayItem(point,
						eventLocation.getLabel(), eventLocation.getPlace());
				itemizedoverlay.addOverlay(overlayitem);

			}

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

			Log.e("jsonFromObj", "Unable to serialize to json: " + object, e);

			return null;
		}
		return writer.toString();
	}

}
