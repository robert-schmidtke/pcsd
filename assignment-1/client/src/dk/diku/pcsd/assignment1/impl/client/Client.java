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
			
			KeyImpl key2 = new KeyImpl();
			key2.setKey("moinkey3");
			
			ValueImpl value2 = new ValueImpl();
			value2.setValue("moinvalue2");
			
			ValueListImpl valueList2 = new ValueListImpl();
			valueList2.getValueList().add(value2);
			
			kvbis.insert(key2, valueList);
			//kvbis.insert(key, valueList2);
			//kvbis.delete(key2);
			kvbis.update(key2, valueList2);
			
			
			
			
			System.out.println("Tralala " + kvbis.read(key).getValueList().get(0).getValue());
			//System.out.println("Tralalu " + kvbis.read(key2).getValueList().get(0).getValue());

		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}

}
