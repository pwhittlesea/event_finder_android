package eventlocator.android;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import eventlocator.android.MyLocation.LocationResult;
import eventlocator.android.data.ServerConnection;
import eventlocator.android.data.SpecialGeoPoint;

public class GoogleMapsActivity extends MapActivity {

	private MyLocationOverlay myLocationOverlay = null;
	private Location currentLocation = null;
	MyLocation myLocation;
	LocationResult locationResult;
	MapView mapView;
	EventItemizedOverlay itemizedoverlay;
	Drawable eventPin;

	boolean firstFoundLocation = true;
	@Override 
	public void onConfigurationChanged(android.content.res.Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("onCreate(); called");
		setContentView(R.layout.main);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);
		myLocation = new MyLocation();
		
		eventPin = this.getResources().getDrawable(
				R.drawable.ic_event_pin);
		
		
		itemizedoverlay = new EventItemizedOverlay(
				eventPin, this);


		locationResult = new LocationResult() {
			@Override
			public void gotLocation(final Location location) {
				// This location could still be null!?
				runOnUiThread(new Runnable() {
					public void run() {
						//Log.d("gotLocation()", "Location: " + location);
						currentLocation = location;
						// TODO Location-to-GeoPoint method - YES WE NEED IT
						// MICHAEL
						if (firstFoundLocation == true) {
							mapView.getController().animateTo(
									new GeoPoint((int) (currentLocation
											.getLatitude() * 1E6),
											(int) (currentLocation
													.getLongitude() * 1E6)));
							mapView.getController().setZoom(15);
							refreshEvents();
							firstFoundLocation = false;
						}
					}
				});
			}
		};
		myLocation.getLocation(this, locationResult);

	}

	@Override
	protected void onPause() {
		super.onPause();
		myLocationOverlay.disableCompass();
		myLocationOverlay.disableMyLocation();
	};

	@Override
	protected void onResume() {
		super.onResume();
		myLocationOverlay.enableCompass();
		myLocationOverlay.enableMyLocation();
	};

	private void refreshEvents() {
		myLocation(); // Center on location used for checking events
		Log.d("refreshEvents", "refreshing events");
		if (!mapView.getOverlays().isEmpty()) {
			System.out.println("clearing overlays");
			mapView.getOverlays().clear();
			itemizedoverlay = new EventItemizedOverlay(
					eventPin, this);
			
		}
		System.out.println("add my location overlay");
		mapView.getOverlays().add(myLocationOverlay);
	
		if (currentLocation != null) {
			mapView.getOverlays().add(itemizedoverlay);
			System.out.println("get event");
			getEvents(itemizedoverlay);
			System.out.println("add itemased overlay");
			
		}

	}

	private void getEvents(EventItemizedOverlay itemizedoverlay) {

		// We will return a short list
		Log.d("getEvents()", "getEvents Called events");
		Log.d("serverurl", getString(R.string.server_url));

		SpecialGeoPoint geoPoint = new SpecialGeoPoint(
				currentLocation.getLatitude(), currentLocation.getLongitude());
		/*
		 * Start an Async task to get event from the server and put them on the
		 * overlay
		 */
		ServerConnection serverConnection = new ServerConnection(
				getString(R.string.server_url), geoPoint, itemizedoverlay,
				getApplicationContext());

	}

	private void myLocation() {
		myLocation.getLocation(this, locationResult);
		if (currentLocation != null) {
			mapView.getController().animateTo(
					new GeoPoint((int) (currentLocation.getLatitude() * 1E6),
							(int) (currentLocation.getLongitude() * 1E6)));
			mapView.getController().setZoom(15);
		} else {
			Toast.makeText(getApplicationContext(), "No location available",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.map_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.centre_loc:
			myLocation();
			return true;
		case R.id.refresh_events:
			refreshEvents();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}