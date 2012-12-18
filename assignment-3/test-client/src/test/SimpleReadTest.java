package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.diku.pcsd.assignment3.master.impl.FileNotFoundException_Exception;
import dk.diku.pcsd.assignment3.master.impl.KeyAlreadyPresentException_Exception;
import dk.diku.pcsd.assignment3.master.impl.KeyImpl;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplService;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplServiceService;
import dk.diku.pcsd.assignment3.master.impl.ServiceAlreadyInitializedException_Exception;
import dk.diku.pcsd.assignment3.master.impl.ServiceInitializingException_Exception;
import dk.diku.pcsd.assignment3.master.impl.ValueImpl;
import dk.diku.pcsd.assignment3.master.impl.ValueListImpl;

public class SimpleReadTest {
	static KeyValueBaseMasterImplServiceService kvbiss;
	static KeyValueBaseMasterImplService kvbis;
	
	@BeforeClass 
	public static void testSetup(){
		kvbiss = new KeyValueBaseMasterImplServiceService();
		kvbis = kvbiss.getKeyValueBaseMasterImplServicePort();
		try {
			kvbis.init("");
		} catch (FileNotFoundException_Exception e) {
			e.printStackTrace();
		} catch (ServiceAlreadyInitializedException_Exception e) {
			e.printStackTrace();
		} catch (ServiceInitializingException_Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void simpleWriteRead() {
		Random rnd = new Random();
		
		String keyValue = String.valueOf(rnd.nextInt(99999));
		String resultValue = String.valueOf(rnd.nextInt(99999));
		String returnedResultValue = new String();
		
		try {
			
			KeyImpl key = new KeyImpl();
			key.setKey(keyValue);
			
			ValueImpl value = new ValueImpl();
			value.setValue(resultValue);
			
			ValueListImpl valueList = new ValueListImpl();
			valueList.getValueList().add(value);
			
			kvbis.insert(key, valueList);
			
			returnedResultValue = ((ValueListImpl)kvbis.read(key).getV()).getValueList().get(0).getValue().toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertEquals("Result", resultValue, returnedResultValue);
	}
	
	@Test
	public void uniformRandomWriteRead() {
		int N = 10;
		Random rnd = new Random();
		boolean testSuccessfull = true;
		int error_sum = 0;
		
		HashMap<String, String> testMap = new HashMap<String, String>();
		
		try {
			//insert values
			for (int i=0; i<N; i++) {
				String keyValue = String.valueOf(rnd.nextInt(999999));
				String resultValue = String.valueOf(rnd.nextInt(99999));
				
				KeyImpl key = new KeyImpl();
				key.setKey(keyValue);
				
				ValueImpl value = new ValueImpl();
				value.setValue(resultValue);
				
				ValueListImpl valueList = new ValueListImpl();
				valueList.getValueList().add(value);
				try{
				kvbis.insert(key, valueList);
				testMap.put(keyValue, resultValue);
				} catch (KeyAlreadyPresentException_Exception e) {
					//e.printStackTrace();
				}
			}
			
			ArrayList<String> keys = new ArrayList<String>(testMap.keySet());
			
			for (int j=0; j<N; j++) {
				//String actualValue = "";
			
				//a random update
				
				
				String randomUpdateKey = keys.get( rnd.nextInt(keys.size()) );
				String randomUpdateValue  = String.valueOf(rnd.nextInt(99999));
				
				testMap.put(randomUpdateKey, randomUpdateValue);
				
				KeyImpl keyUpdate = new KeyImpl();
				keyUpdate.setKey(randomUpdateKey);
				
				ValueImpl value = new ValueImpl();
				value.setValue(randomUpdateValue);
			
				ValueListImpl valueUpdateList = new ValueListImpl();
				valueUpdateList.getValueList().add(value);
				
				kvbis.update(keyUpdate, valueUpdateList);
				
				

				//a random read
				String randomReadKey = keys.get(rnd.nextInt(keys.size()));
				
				KeyImpl keyRead = new KeyImpl();
				keyRead.setKey(randomReadKey);
				
				String expectedValue = testMap.get(randomReadKey);
				String actualValue = ((ValueListImpl)kvbis.read(keyRead).getV()).getValueList().get(0).getValue().toString();
				
				if (!expectedValue.equals(actualValue)) {
					testSuccessfull = false;
					System.out.println("Error:" + expectedValue + " " + actualValue);
					error_sum ++;
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(error_sum);
		assertTrue("Result2", testSuccessfull);
		
	}
	

}
