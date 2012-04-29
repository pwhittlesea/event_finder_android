package eventlocator.android;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import eventlocator.android.data.EventLocation;

public class EventLocationOverlayItem extends OverlayItem{
	EventLocation eventLocation;


	public EventLocationOverlayItem(GeoPoint point, String title, String snippet, EventLocation eventLocation) {
		super(point, title, snippet);
		this.eventLocation = eventLocation;
	}
	public EventLocation getEventLocation() {
		return eventLocation;
	}


}
