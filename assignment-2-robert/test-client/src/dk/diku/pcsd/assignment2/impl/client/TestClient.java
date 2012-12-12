package dk.diku.pcsd.assignment2.impl.client;

import dk.diku.pcsd.assignment2.impl.KeyValueBaseImplService;
import dk.diku.pcsd.assignment2.impl.KeyValueBaseImplServiceService;

public class TestClient {
	
	public static void main(String[] args) {
		
		KeyValueBaseImplServiceService kvbiss = new KeyValueBaseImplServiceService();
		KeyValueBaseImplService kvbis = kvbiss.getKeyValueBaseImplServicePort();
		
		try {
			kvbis.init("");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
