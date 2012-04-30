package eventlocator.android.data;

import java.io.StringWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import eventlocator.android.ActivitySwipeDetector;
import eventlocator.android.EventItemizedOverlay;
import eventlocator.android.R;

public class GetEventsForLocationTask {

	Context context;
	ListView listView;

	/**
	 * The class used by the activity to get the events
	 * 
	 * @param url
	 * @param listView
	 * @param context
	 */
	public GetEventsForLocationTask(String url, EventLocation eventLocation,
			ListView listView, Context context) {
		this.context = context;
		this.listView = listView;
		String json = "\"" + eventLocation.getPlace() + "\"";
		json = "{\"place\":" + json + "}";
		Log.d("jsonUrlBeforeEncode", json);
		json = URLEncoder.encode(json);

		String jsonUrl = url + "?req=" + json;
		Log.d("jsonUrl", jsonUrl);
		new GetJSONTask().execute(jsonUrl, null, null);

	}

	private class GetJSONTask extends AsyncTask<String, Void, ArrayList<Event>> {
		protected ArrayList<Event> doInBackground(String... jsonUrl) {
			ArrayList<Event> subList = new ArrayList<Event>();

			String jsonFromServer;
			ArrayList<Event> events = new ArrayList<Event>();
			try {
				jsonFromServer = JSONClient.connect(jsonUrl[0]);
				Log.d("jsonFromServer", "fromServer: " + jsonFromServer);
				JSONToObjects<Event, Events> jsonToObjects = new JSONToObjects<Event, Events>(
						Event.class, Events.class);
				jsonToObjects.init(jsonFromServer);
				events = jsonToObjects.findAll();

				Log.d("getEvents()", "Found " + events.size());
			} catch (Exception e1) {
				Log.e("getEvents()", "Couldn't get events from server");
				e1.printStackTrace();
			}
			if (events.size() > 150) {
				subList.addAll(events.subList(0, 150));
			} else {
				return events;
			}
			return subList;
		}

		protected void onProgressUpdate(Void... progress) {

		}

		protected void onPostExecute(ArrayList<Event> result) {

			if (result.size() == 0) {
				Log.d("getEvents()", "No Events available");
				Toast.makeText(context, "No events near your location",
						Toast.LENGTH_LONG).show();

			} else {

			}

			listView.setAdapter(new ArrayAdapter<Event>(context,
					R.layout.list_item, result));
			listView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View view,
						int pos, long id) {
					Event event = (Event) arg0.getAdapter().getItem(pos);
					Dialog dialog = new Dialog(context);

					dialog.setContentView(R.layout.event_dialog);
					dialog.setTitle(event.getLabel());

					TextView textView = (TextView) dialog
							.findViewById(R.id.event_text);
					 SimpleDateFormat simpleDateformat = new SimpleDateFormat("E dd MMM HH:mm");
					
					textView.setText(event.getDesc() + "\n \n Event Start: "
							+ simpleDateformat.format(event.getStart()) + "\n \n Event End: "
							+ simpleDateformat.format(event.getEnd()));
					textView.setMovementMethod(new ScrollingMovementMethod());

					LinearLayout layoutRoot = (LinearLayout) dialog
							.findViewById(R.id.layout_root);
					ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(
							dialog);
					layoutRoot.setOnTouchListener(activitySwipeDetector);
					WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
					lp.copyFrom(dialog.getWindow().getAttributes());
					lp.width = WindowManager.LayoutParams.FILL_PARENT;
					lp.height = WindowManager.LayoutParams.FILL_PARENT;
					dialog.show();
					dialog.getWindow().setAttributes(lp);

				}
			});
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
