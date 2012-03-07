package eventlocator.android;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import eventlocator.android.R;
import eventlocator.android.data.Event;
import eventlocator.android.data.ServerConnection;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class GoogleMapsActivity extends MapActivity {

	private LocationManager myLocationManager;
	private LocationListener myLocationListener;

	MapView mapView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		// List<Overlay> mapOverlays = mapView.getOverlays();

		myLocationListener = new MyLocationListener();

		myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				0, 0, myLocationListener);

		if (myLocationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
			// Get the current location in start-up, if a past location is
			// available
			myLocation();
//			GeoPoint initGeoPoint = new GeoPoint((int) (myLocationManager
//					.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//					.getLatitude() * 1000000), (int) (myLocationManager
//					.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//					.getLongitude() * 1000000));
//			CenterLocation(initGeoPoint);
		}

		refreshEvents();

	}

	private void refreshEvents() {
		Log.d("refreshEvents", "refreshing events");
		if (!mapView.getOverlays().isEmpty()) {
			mapView.getOverlays().clear();

			mapView.invalidate();
		}
		System.out.println("invalidated");
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.ic_event_pin);
		EventItemizedOverlay itemizedoverlay = new EventItemizedOverlay(
				drawable, this);

		for (Event event : getEvents()) {
			GeoPoint point = new GeoPoint((int) (event.getLat() * 1E6),
					(int) (event.getLong() * 1E6));
			OverlayItem overlayitem = new OverlayItem(point, event.getLabel(),
					event.getUrl());
			itemizedoverlay.addOverlay(overlayitem);

		}

		mapView.getOverlays().add(itemizedoverlay);
	}

	private ArrayList<Event> getEvents() {
		Log.d("serverurl", getString(R.string.server_url));
		ServerConnection serverConnection = new ServerConnection(
				getString(R.string.server_url));
		ArrayList<Event> events = serverConnection.getEvents();
		return events;
	}

	private void CenterLocation(GeoPoint centerGeoPoint) {
		// myMapController.animateTo(centerGeoPoint);
		mapView.getController().animateTo(centerGeoPoint);

	}

	private void myLocation() {

		GeoPoint initGeoPoint = new GeoPoint((int) (myLocationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER)
				.getLatitude() * 1000000), (int) (myLocationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER)
				.getLongitude() * 1000000));
		CenterLocation(initGeoPoint);

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

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location argLocation) {
			// TODO Auto-generated method stub
			// GeoPoint myGeoPoint = new GeoPoint(
			// (int)(argLocation.getLatitude()*1000000),
			// (int)(argLocation.getLongitude()*1000000));
			//
			// CenterLocatio(myGeoPoint);
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}

}