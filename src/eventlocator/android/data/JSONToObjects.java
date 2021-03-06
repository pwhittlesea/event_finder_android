package eventlocator.android.data;
import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

public class JSONToObjects<T, C extends ArrayList<T>> {

//	private final String json = "{ \"events\": [ {\"url\":\"http://id.southampton.ac.uk/opendays/2011/09/event/ComputerScienceAndSoftwareEngineering-680741694b03543a0225abaa0484ebcb-2011-09-02-10:00-10:50\",\"long\":\"-1.3988324\",\"lat\":\"50.9358682\",\"label\":\"Computer Science and Software Engineering\",\"start\":\"2011-09-02T10:00+01:00\",\"end\":\"2011-09-02T10:50+01:00\"}, {\"url\":\"http://id.southampton.ac.uk/opendays/2011/09/event/ComputerScienceAndSoftwareEngineering-680741694b03543a0225abaa0484ebcb-2011-09-02-11:00-11:50\",\"long\":\"-1.3988324\",\"lat\":\"50.9358682\",\"label\":\"Computer Science and Software Engineering\",\"start\":\"2011-09-02T11:00+01:00\",\"end\":\"2011-09-02T11:50+01:00\"}, {\"url\":\"http://id.southampton.ac.uk/opendays/2011/09/event/ComputerScienceAndSoftwareEngineering-680741694b03543a0225abaa0484ebcb-2011-09-02-12:00-13:00\",\"long\":\"-1.3988324\",\"lat\":\"50.9358682\",\"label\":\"Computer Science and Software Engineering\",\"start\":\"2011-09-02T12:00+01:00\",\"end\":\"2011-09-02T13:00+01:00\"} ]}";
	private ObjectMapper objectMapper = null;
	private JsonFactory jsonFactory = null;
	private JsonParser jp = null;
	private ArrayList<T> events = null;
	private C mEvents = null;
	private final Class<T> clazz;
	private final Class<C> containerClazz;

	public JSONToObjects(Class<T> clazz, Class<C>  containerClazz ) {
		this.clazz = clazz;
		this.containerClazz = containerClazz;
		objectMapper = new ObjectMapper();
		jsonFactory = new JsonFactory();
	}

	public void init(String json) {
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", Locale.ENGLISH);


		try {
			jp = jsonFactory.createJsonParser(json);
			// objectMapper.setDateFormat(simpleDateFormat);
			mEvents = objectMapper.readValue(jp, containerClazz);
		
			events = mEvents;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<T> findAll() {
		return events;
	}

	public T findById(int id) {
		return events.get(id);
	}
}
