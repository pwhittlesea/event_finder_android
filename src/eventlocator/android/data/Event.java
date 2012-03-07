package eventlocator.android.data;
import java.util.Date;

public class Event {
	String url;
	float lng;
	float lat;
	String label;
	Date start;
	Date end;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
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

	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public String toString(){
		return url + " " + lng + " " + lat + " " +label + " " + start + " " + end;
	}
	
	
	
}
