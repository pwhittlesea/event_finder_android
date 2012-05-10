package eventlocator.android;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

import eventlocator.android.data.GetEventsForLocationTask;

public class EventItemizedOverlay extends
		ItemizedOverlay<EventLocationOverlayItem> {
	Context mContext;
	MapView mapView;
	private ArrayList<EventLocationOverlayItem> mOverlays = new ArrayList<EventLocationOverlayItem>();

	public EventItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		populate();
	}

	public EventItemizedOverlay(Drawable defaultMarker, MapView mapView,
			Context context) {
		super(boundCenterBottom(defaultMarker));
		this.mapView = mapView;
		mContext = context;
		populate();
	}

	@Override
	protected EventLocationOverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	public void addOverlay(EventLocationOverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		final EventLocationOverlayItem item = mOverlays.get(index);
		final Dialog dialog = new Dialog(mContext);

		dialog.setContentView(R.layout.location_dialog);
		dialog.setTitle(item.getTitle());
		ListView listView = (ListView) dialog.findViewById(R.id.event_list);
		Button focusBtn = (Button) dialog.findViewById(R.id.focus_map);
		focusBtn.setOnClickListener(new View.OnClickListener() {
			//Button listener to focus the map on a building.
			public void onClick(View v) {
				GeoPoint gp = new GeoPoint((int) (item.getEventLocation()
						.getLat() * 1E6), (int) (item.getEventLocation()
						.getLong() * 1E6));
				mapView.getController().animateTo(gp);
				dialog.hide();
				mapView.getController().setZoom(20);

			}
		});

		GetEventsForLocationTask getEventsForLocationTask = new GetEventsForLocationTask(
				mContext.getString(R.string.fetch_events_for_location_server_url),
				item.getEventLocation(), listView, mContext);

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

		return true;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		if (!shadow) {
			super.draw(canvas, mapView, false);
		}
	}

}
