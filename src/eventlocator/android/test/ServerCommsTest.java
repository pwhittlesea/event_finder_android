package eventlocator.android.test;

import java.io.StringWriter;
import java.net.URLEncoder;

import org.codehaus.jackson.map.ObjectMapper;

import eventlocator.android.data.GetEventLocationsTask;
import eventlocator.android.data.JSONClient;
import eventlocator.android.data.SpecialGeoPoint;
import junit.framework.Assert;
import android.test.AndroidTestCase;
import android.util.Log;

public class ServerCommsTest extends AndroidTestCase {

	public void testFetchLocations() throws Throwable {
		String url = "http://kanga-ac10g08.ecs.soton.ac.uk/mobile/fetch_locations.php";
		SpecialGeoPoint geoPoint = new SpecialGeoPoint(50.9, -1.38);
		String json = GetEventLocationsTask.jsonFromObject(geoPoint);

		json = "{\"geo\":" + json + "}";
		Log.d("jsonUrlBeforeEncode", json);
		json = URLEncoder.encode(json);

		String jsonUrl = url + "?req=" + json;
		Log.d("TESTjsonUrl", jsonUrl);

		String jsonFromServer = JSONClient.connect(jsonUrl);
		Log.d("TESTjsonFromServer", jsonFromServer);

		Assert.assertTrue(jsonFromServer.length() > 5);// Check that we are
														// getting something
														// from the server
	}

	public void testJSONfromObj() {
		SpecialGeoPoint geoPoint = new SpecialGeoPoint(50.9, -1.38);
		String json = GetEventLocationsTask.jsonFromObject(geoPoint);

		assertNotNull(json);

	}
}
