package test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.diku.pcsd.assignment3.master.impl.FileNotFoundException_Exception;
import dk.diku.pcsd.assignment3.master.impl.IOException_Exception;
import dk.diku.pcsd.assignment3.master.impl.KeyAlreadyPresentException_Exception;
import dk.diku.pcsd.assignment3.master.impl.KeyImpl;
import dk.diku.pcsd.assignment3.master.impl.KeyNotFoundException_Exception;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplService;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplServiceService;
import dk.diku.pcsd.assignment3.master.impl.ServiceAlreadyInitializedException_Exception;
import dk.diku.pcsd.assignment3.master.impl.ServiceInitializingException_Exception;
import dk.diku.pcsd.assignment3.master.impl.ServiceNotInitializedException_Exception;
import dk.diku.pcsd.assignment3.master.impl.ValueImpl;
import dk.diku.pcsd.assignment3.master.impl.ValueListImpl;

public class MultiReadTest {
	static KeyValueBaseMasterImplServiceService kvbiss;
	static KeyValueBaseMasterImplService kvbis;
	
	static HashMap<String, String> testMap = new HashMap<String, String>();
	static ArrayList<String> keys;
	
	static boolean testSuccessfull = true;
	
	
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
	public void parallelRead() {
		//how often
		int N = 1000;
		
		//how many threads
		int h = 10;
		//how many reads per thread
		int n = N / h;
		
		Random rnd = new Random();		
		
		//write N key values in hashmap and store
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
					//do nothing
				} catch (IOException_Exception e) {
					e.printStackTrace();
				} catch (ServiceNotInitializedException_Exception e) {
					e.printStackTrace();
				}
			}
			
			keys = new ArrayList<String>(testMap.keySet());
		
			//make threads and let them read
			ExecutorService executor = Executors.newFixedThreadPool(h);
		    for (int i = 0; i < h; i++) {
		      Runnable worker = new ReadThread(n);
		      executor.execute(worker);
		    }
		    // This will make the executor accept no new threads
		    // and finish all existing threads in the queue
		    executor.shutdown();
		    // Wait until all threads are finish
		    while (!executor.isTerminated()) {

		    }
		    		
		    //if one read failed fail test case
		    assertTrue("Result", testSuccessfull);
		
	}
	
	public static class ReadThread implements Runnable {
		int n;
		
		ReadThread(int n) {
			  this.n = n;
		}

		@Override
		public void run() {
			Random rnd = new Random();
			String actualValue = "";
			String expectedValue = "";
			
			//get a random key value pair from hash map
			for (int i=0;i<this.n;i++){
				try {
				String randomReadKey = keys.get(rnd.nextInt(keys.size()));
				
				KeyImpl keyRead = new KeyImpl();
				keyRead.setKey(randomReadKey);
				
				expectedValue = testMap.get(randomReadKey);
				actualValue = ((ValueListImpl)kvbis.read(keyRead).getV()).getValueList().get(0).getValue().toString();
				} catch (IOException_Exception e) {
					e.printStackTrace();
				} catch (KeyNotFoundException_Exception e) {
					e.printStackTrace();
				} catch (ServiceNotInitializedException_Exception e) {
					e.printStackTrace();
				}
				
				if (!expectedValue.equals(actualValue)) {
					testSuccessfull = false;
					System.out.print("Fehler: " + expectedValue + actualValue);
				}
				
			}	
		}
	} 
}
