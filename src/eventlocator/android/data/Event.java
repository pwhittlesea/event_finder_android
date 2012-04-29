package eventlocator.android.data;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Event {
	private String url;
	private String label;
	private String desc;
	private Date start;
	private Date end;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
		 SimpleDateFormat simpleDateformat = new SimpleDateFormat("E d MMM");
		return label + " - " + simpleDateformat.format(getStart()); //url + " " +label + " " + start + " " + end + " " + desc;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	
}
