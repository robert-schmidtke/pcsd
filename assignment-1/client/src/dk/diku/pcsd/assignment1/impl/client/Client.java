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
		
		KeyImpl key = new KeyImpl();
		key.setKey("testKey");
		
		ValueImpl value = new ValueImpl();
		value.setValue("testValue");
		
		ValueListImpl valueList = new ValueListImpl();
		valueList.getValueList().add(value);
		
		try {
			kvbis.init("");
			kvbis.insert(key, valueList);
			System.out.println(kvbis.read(key).getValueList().get(0).getValue());
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
