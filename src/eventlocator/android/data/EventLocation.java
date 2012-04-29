package eventlocator.android.data;

public class EventLocation {

	private float lng;
	private float lat;
	private String label;
	private String place;

	public float getLong() {
		return lng;
	}

	public void setLong(float lng) {
		this.lng = lng;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String toString() {
		return lng + " " + lat + " " + label + " " + place;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

}
