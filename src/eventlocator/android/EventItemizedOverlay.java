package eventlocator.android;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class EventItemizedOverlay extends ItemizedOverlay {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
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
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mOverlays.get(index);
		Dialog dialog = new Dialog(mContext);

		dialog.setContentView(R.layout.event_dialog);
		dialog.setTitle(item.getTitle());

		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText(item.getSnippet() + item.getSnippet() + item.getSnippet() + item.getSnippet() + item.getSnippet() + item.getSnippet());
		text.setMovementMethod(new ScrollingMovementMethod());
		ImageView image = (ImageView) dialog.findViewById(R.id.image);
		image.setImageResource(R.drawable.advert_phd);
		ImageView brandImage = (ImageView) dialog
				.findViewById(R.id.brand_image);
		brandImage.setImageResource(R.drawable.uos_logo_vert_light);

		dialog.show();
		return true;
	}

}
