package eventlocator.android;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import eventlocator.android.data.GetEventsForLocationTask;

public class EventItemizedOverlay extends
		ItemizedOverlay<EventLocationOverlayItem> {
	private ArrayList<EventLocationOverlayItem> mOverlays = new ArrayList<EventLocationOverlayItem>();
	Context mContext;

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
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		EventLocationOverlayItem item = mOverlays.get(index);
		Dialog dialog = new Dialog(mContext);

		dialog.setContentView(R.layout.event_dialog);
		dialog.setTitle(item.getTitle());
ListView listView = (ListView) dialog.findViewById(R.id.event_list);
//		TextView text = (TextView) dialog.findViewById(R.id.text);
//		text.setText(item.getSnippet() + item.getSnippet() + item.getSnippet()
//				+ item.getSnippet() + item.getSnippet() + item.getSnippet());
//		text.setMovementMethod(new ScrollingMovementMethod());
		ImageView image = (ImageView) dialog.findViewById(R.id.image);
		image.setImageResource(R.drawable.advert_phd);
		ImageView brandImage = (ImageView) dialog
				.findViewById(R.id.brand_image);
		brandImage.setImageResource(R.drawable.uos_logo_vert_light);

		dialog.show();

		GetEventsForLocationTask getEventsForLocationTask = new GetEventsForLocationTask(
				mContext.getString(R.string.fetch_events_for_location_server_url),
				item.getEventLocation(), listView, mContext);

		return true;
	}

}
