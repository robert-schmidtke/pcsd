package dk.diku.pcsd.assignment1.impl.client;

import dk.diku.pcsd.assignment1.impl.KeyImpl;
import dk.diku.pcsd.assignment1.impl.KeyValueBaseImplService;
import dk.diku.pcsd.assignment1.impl.KeyValueBaseImplServiceService;
import dk.diku.pcsd.assignment1.impl.ValueImpl;
import dk.diku.pcsd.assignment1.impl.ValueListImpl;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		KeyValueBaseImplServiceService kvbiss = new KeyValueBaseImplServiceService();
		KeyValueBaseImplService kvbis = kvbiss.getKeyValueBaseImplServicePort();
		try {
			kvbis.init("mooooooin");
			
			KeyImpl key = new KeyImpl();
			key.setKey("moinkey");
			
			ValueImpl value = new ValueImpl();
			value.setValue("moinvalue");
			
			ValueListImpl valueList = new ValueListImpl();
			valueList.getValueList().add(value);
			
			kvbis.insert(key, valueList);
			
			kvbis.read(key);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}

}
