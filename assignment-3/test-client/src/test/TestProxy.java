package test;

import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplService;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplServiceService;
import dk.diku.pcsd.assignment3.proxy.impl.Configuration;
import dk.diku.pcsd.assignment3.proxy.impl.FileNotFoundException_Exception;
import dk.diku.pcsd.assignment3.proxy.impl.IOException_Exception;
import dk.diku.pcsd.assignment3.proxy.impl.KeyAlreadyPresentException_Exception;
import dk.diku.pcsd.assignment3.proxy.impl.KeyImpl;
import dk.diku.pcsd.assignment3.proxy.impl.KeyNotFoundException_Exception;
import dk.diku.pcsd.assignment3.proxy.impl.KeyValueBaseProxyImplService;
import dk.diku.pcsd.assignment3.proxy.impl.KeyValueBaseProxyImplServiceService;
import dk.diku.pcsd.assignment3.proxy.impl.ServiceAlreadyConfiguredException_Exception;
import dk.diku.pcsd.assignment3.proxy.impl.ServiceAlreadyInitializedException_Exception;
import dk.diku.pcsd.assignment3.proxy.impl.ServiceInitializingException_Exception;
import dk.diku.pcsd.assignment3.proxy.impl.ServiceNotInitializedException_Exception;
import dk.diku.pcsd.assignment3.proxy.impl.ValueImpl;
import dk.diku.pcsd.assignment3.proxy.impl.ValueListImpl;

public class TestProxy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		KeyValueBaseProxyImplServiceService kvbiss = new KeyValueBaseProxyImplServiceService();
		KeyValueBaseProxyImplService kvbis = kvbiss.getKeyValueBaseProxyImplServicePort();
		Configuration conf = new Configuration();
		conf.setMaster("http://localhost:8080/master/keyvaluebasemaster?wsdl");
		conf.getSlaves().add("http://localhost:8080/slave/keyvaluebaseslave?wsdl");
		try {
			kvbis.config(conf);
			kvbis.init("");
			KeyImpl key = new KeyImpl();
			key.setKey("testKey");
			
			ValueListImpl vl = new ValueListImpl();
			
			ValueImpl v = new ValueImpl();
			vl.getValueList().add(v);
			v.setValue("testValue");
			
			kvbis.insert(key, vl);
			
			for (int i = 0; i<10; i++)
				System.out.println(kvbis.read(key).getValueList().get(0).getValue());
			

			
		} catch (ServiceAlreadyConfiguredException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceAlreadyInitializedException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceInitializingException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyAlreadyPresentException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceNotInitializedException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyNotFoundException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
