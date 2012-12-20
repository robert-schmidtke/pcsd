package dk.diku.pcsd.assignment2.impl.client;

import dk.diku.pcsd.assignment3.impl.KeyImpl;
import dk.diku.pcsd.assignment3.impl.KeyValueBaseImplService;
import dk.diku.pcsd.assignment3.impl.KeyValueBaseImplServiceService;
import dk.diku.pcsd.assignment3.impl.ValueImpl;
import dk.diku.pcsd.assignment3.impl.ValueListImpl;

public class TestClient {
	
	public static void main(String[] args) {
		
		KeyValueBaseImplServiceService kvbiss = new KeyValueBaseImplServiceService();
		KeyValueBaseImplService kvbis = kvbiss.getKeyValueBaseImplServicePort();
		
		try {
//			kvbis.init("");
			KeyImpl k = new KeyImpl();
			k.setKey("42");
			ValueImpl v = new ValueImpl();
			v.setValue(88);
			ValueListImpl vi = new ValueListImpl();
			vi.getValueList().add(v);
			kvbis.update(k, vi);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
