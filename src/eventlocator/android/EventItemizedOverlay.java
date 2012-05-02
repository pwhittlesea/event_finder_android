package eventlocator.android;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import eventlocator.android.data.GetEventsForLocationTask;

public class EventItemizedOverlay extends
		ItemizedOverlay<EventLocationOverlayItem> {
	Context mContext;
	private ArrayList<EventLocationOverlayItem> mOverlays = new ArrayList<EventLocationOverlayItem>();
	

	public EventItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		populate();
	}

	public EventItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
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
//		Drawable drawable = mContext.getResources().getDrawable(R.drawable.map_pin_2);
//		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//		overlay.setMarker(mContext.getResources().getDrawable(R.drawable.map_pin_2));
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		EventLocationOverlayItem item = mOverlays.get(index);
		Dialog dialog = new Dialog(mContext);

		dialog.setContentView(R.layout.location_dialog);
		dialog.setTitle(item.getTitle());
		ListView listView = (ListView) dialog.findViewById(R.id.event_list);
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

}
