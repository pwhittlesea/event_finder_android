package eventlocator.android;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import eventlocator.android.MyLocation.LocationResult;
import eventlocator.android.data.EventLocation;
import eventlocator.android.data.EventLocations;
import eventlocator.android.data.GetEventLocationsTask;
import eventlocator.android.data.GetEventsForLocationTask;
import eventlocator.android.data.SpecialGeoPoint;

public class GoogleMapsActivity extends MapActivity {

	private MyLocationOverlay myLocationOverlay = null;
	private Location currentLocation = null;
	MyLocation myLocation;
	LocationResult locationResult;
	MapView mapView;
	EventItemizedOverlay itemizedoverlay;
	Drawable eventPin;
	private EventLocations currentEventLocations;
	private EditText filterText = null;
	ArrayAdapter<EventLocation> filterListAdapter = null;
	Context contex = this;

	boolean firstFoundLocation = true;

	protected Dialog mSplashDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentEventLocations = new EventLocations();
		System.out.println("onCreate(); called");
		setContentView(R.layout.main);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);
		myLocation = new MyLocation();

		MyStateSaver data = (MyStateSaver) getLastNonConfigurationInstance();
		if (data != null) {
			// Show splash screen if still loading
			if (data.showSplashScreen) {
				showSplashScreen();
			}
			setContentView(findViewById(R.id.mapview));

			// Rebuild your UI with your saved state here
		} else {
			showSplashScreen();
			setContentView((findViewById(R.id.mapview)));
			// Do your heavy loading here on a background thread
		}

		eventPin = this.getResources().getDrawable(R.drawable.map_pin_1);

		itemizedoverlay = new EventItemizedOverlay(eventPin, mapView, this);

		locationResult = new LocationResult() {
			@Override
			public void gotLocation(final Location location) {
				// This location could still be null!?
				runOnUiThread(new Runnable() {
					public void run() {
						// Log.d("gotLocation()", "Location: " + location);
						currentLocation = location;
						// TODO Location-to-GeoPoint method - YES WE NEED IT
						// MICHAEL
						if (firstFoundLocation == true) {
							mapView.getController().animateTo(
									new GeoPoint((int) (currentLocation
											.getLatitude() * 1E6),
											(int) (currentLocation
													.getLongitude() * 1E6)));
							mapView.getController().setZoom(16);
							refreshEventLocations();
							firstFoundLocation = false;
						}
					}
				});
			}
		};
		myLocation.getLocation(this, locationResult);

	}

	// override on screen rotate so onCreate is not called again with every
	// rotate
	@Override
	public void onConfigurationChanged(
			android.content.res.Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	};

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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (filterText != null) {// remove listener only if list view has been
									// viewed.
			filterText.removeTextChangedListener(filterTextWatcher);
		}
	}

	private void refreshEventLocations() {
		myLocation(); // Center on location used for checking events
		Log.d("refreshEvents", "refreshing events");
		if (!mapView.getOverlays().isEmpty()) {
			System.out.println("clearing overlays");
			mapView.getOverlays().clear();
			itemizedoverlay = new EventItemizedOverlay(eventPin, mapView, this);

		}
		System.out.println("add my location overlay");
		mapView.getOverlays().add(myLocationOverlay);

		if (currentLocation != null) {
			mapView.getOverlays().add(itemizedoverlay);
			System.out.println("get event");
			getEventLocations(itemizedoverlay);
			System.out.println("add itemased overlay");

		}

	}

	private void getEventLocations(EventItemizedOverlay itemizedoverlay) {

		// We will return a short list
		Log.d("getEvents()", "getEvents Called events");// TODO change method
														// names etc

		SpecialGeoPoint geoPoint = new SpecialGeoPoint(
				currentLocation.getLatitude(), currentLocation.getLongitude());
		/*
		 * Start an Async task to get event from the server and put them on the
		 * overlay
		 */
		GetEventLocationsTask serverConnection = new GetEventLocationsTask(
				getString(R.string.fetch_locations_server_url), geoPoint,
				itemizedoverlay, currentEventLocations, getApplicationContext());

	}

	private void myLocation() {
		myLocation.getLocation(this, locationResult);
		if (currentLocation != null) {
			mapView.getController().animateTo(
					new GeoPoint((int) (currentLocation.getLatitude() * 1E6),
							(int) (currentLocation.getLongitude() * 1E6)));
			mapView.getController().setZoom(16);
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
			refreshEventLocations();
			return true;
		case R.id.list_view:
			showListView();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			filterListAdapter.getFilter().filter(s);
		}

	};

	private void showListView() {

		if (currentEventLocations.size() == 0) {
			Toast.makeText(getApplicationContext(),
					"No events have been loaded yet, try refreshing",
					Toast.LENGTH_SHORT).show();
		} else {
			final Dialog locationFilterDialog = new Dialog(this);
			locationFilterDialog
					.setContentView(R.layout.filter_locations_dialog);
			locationFilterDialog.setTitle("Location List");

			filterText = (EditText) locationFilterDialog
					.findViewById(R.id.filter_text_box);

			filterText.addTextChangedListener(filterTextWatcher);

			LinearLayout layoutRoot = (LinearLayout) locationFilterDialog
					.findViewById(R.id.location_list_layout_root);

			ListView list = (ListView) locationFilterDialog
					.findViewById(R.id.filtered_list);
			filterListAdapter = new ArrayAdapter<EventLocation>(
					getApplicationContext(), R.layout.list_item,
					currentEventLocations);
			list.setAdapter(filterListAdapter);

			list.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long id) {
					final EventLocation eventLocation = (EventLocation) arg0
							.getAdapter().getItem(pos);
					final Dialog dialog = new Dialog(contex);

					dialog.setContentView(R.layout.location_dialog);
					dialog.setTitle(eventLocation.getLabel());
					ListView listView = (ListView) dialog
							.findViewById(R.id.event_list);

					Button focusBtn = (Button) dialog
							.findViewById(R.id.focus_map);
					focusBtn.setOnClickListener(new View.OnClickListener() {
						// Button listener to focus the map on a building.
						public void onClick(View v) {
							GeoPoint gp = new GeoPoint((int) (eventLocation
									.getLat() * 1E6), (int) (eventLocation
									.getLong() * 1E6));
							mapView.getController().animateTo(gp);
							locationFilterDialog.hide();
							dialog.hide();
							locationFilterDialog.hide();
							mapView.getController().setZoom(20);

						}
					});

					GetEventsForLocationTask getEventsForLocationTask = new GetEventsForLocationTask(
							contex.getString(R.string.fetch_events_for_location_server_url),
							eventLocation, listView, contex);
					WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
					lp.copyFrom(dialog.getWindow().getAttributes());
					lp.width = WindowManager.LayoutParams.FILL_PARENT;
					lp.height = WindowManager.LayoutParams.FILL_PARENT;
					dialog.show();
					dialog.getWindow().setAttributes(lp);

					LinearLayout layoutRoot = (LinearLayout) dialog
							.findViewById(R.id.layout_root);
					ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(
							dialog);
					layoutRoot.setOnTouchListener(activitySwipeDetector);

				}
			});

			ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(
					locationFilterDialog);
			layoutRoot.setOnTouchListener(activitySwipeDetector);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(locationFilterDialog.getWindow().getAttributes());
			lp.width = WindowManager.LayoutParams.FILL_PARENT;
			lp.height = WindowManager.LayoutParams.FILL_PARENT;
			locationFilterDialog.show();
			locationFilterDialog.getWindow().setAttributes(lp);

		}

	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		MyStateSaver data = new MyStateSaver();
		// Save your important data here

		if (mSplashDialog != null) {
			data.showSplashScreen = true;
			removeSplashScreen();
		}
		return data;
	}

	/**
	 * Removes the Dialog that displays the splash screen
	 */
	protected void removeSplashScreen() {
		if (mSplashDialog != null) {
			mSplashDialog.dismiss();
			mSplashDialog = null;
		}
	}

	/**
	 * Shows the splash screen over the full Activity
	 */
	protected void showSplashScreen() {
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mSplashDialog = new Dialog(this, R.style.SplashScreen);
		mSplashDialog.setContentView(R.layout.splashscreen);
		mSplashDialog.setCancelable(false);
		mSplashDialog.show();

		// Set Runnable to remove splash screen just in case
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				removeSplashScreen();
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
			}
		}, 3000);
	}

	private class MyStateSaver {
		public boolean showSplashScreen = false;
		// Your other important fields here
	}
}