package test;

import dk.diku.pcsd.assignment3.master.impl.KeyImpl;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplService;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplServiceService;
import dk.diku.pcsd.assignment3.master.impl.TimestampPair;

public class TestClient {
	
	public static void main(String[] args) {
		KeyValueBaseMasterImplServiceService kvbiss;
		KeyValueBaseMasterImplService kvbis;
		
		kvbiss = new KeyValueBaseMasterImplServiceService();
		kvbis = kvbiss.getKeyValueBaseMasterImplServicePort();
	
		try {
//			Configuration cfg = new Configuration();
//			cfg.getSlaves().add("http://localhost:8081/pcsd-assignment3-slave/keyvaluebaseslave?wsdl");
//			kvbis.config(cfg);
//			kvbis.init("dk/init.list");
//			
			KeyImpl k = new KeyImpl();
			k.setKey("42");
			System.out.println(((TimestampPair) kvbis.read(k)).getV().getValueList().get(0).getValue());
//			
//			ValueListImpl vli = new ValueListImpl();
//			ValueImpl vi = new ValueImpl();
//			vi.setValue(1234);
//			vli.getValueList().add(vi);
//			
//			kvbis.update(k, vli);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		System.out.println("done");
	}

}
