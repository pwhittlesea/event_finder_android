package eventlocator.android.data;

public class SpecialGeoPoint {
double lng;
double lat;

public SpecialGeoPoint(double lat, double lng){
	this.lat = lat;
	this.lng = lng;
}

public double getLong() {
	return lng;
}
public void setLong(double lng) {
	this.lng = lng;
}
public double getLat() {
	return lat;
}
public void setLat(double lat) {
	this.lat = lat;
}


}
