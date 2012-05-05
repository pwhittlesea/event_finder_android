package eventlocator.android;

import java.util.Random;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import eventlocator.android.data.EventLocation;

public class EventLocationOverlayItem extends OverlayItem {
	EventLocation eventLocation;

	public EventLocationOverlayItem(GeoPoint point, String title,
			String snippet, EventLocation eventLocation, Context context) {
		super(point, title, snippet);
		this.eventLocation = eventLocation;
		Drawable drawable;

		Random r = new Random();
		// TODO Set this to the number of events
		switch (r.nextInt(8)) {
		case 1:
			drawable = context.getResources().getDrawable(R.drawable.map_pin_1);
			break;
		case 2:
			drawable = context.getResources().getDrawable(R.drawable.map_pin_2);
			break;
		case 3:
			drawable = context.getResources().getDrawable(R.drawable.map_pin_3);
			break;
		case 4:
			drawable = context.getResources().getDrawable(R.drawable.map_pin_4);
			break;
		default:
			drawable = context.getResources().getDrawable(
					R.drawable.map_pin_5plus);
			break;

		}
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		this.setMarker(drawable);
	}

	public EventLocation getEventLocation() {
		return eventLocation;
	}

}
