package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.diku.pcsd.assignment3.master.impl.FileNotFoundException_Exception;
import dk.diku.pcsd.assignment3.master.impl.IOException_Exception;
import dk.diku.pcsd.assignment3.master.impl.KeyAlreadyPresentException_Exception;
import dk.diku.pcsd.assignment3.master.impl.KeyImpl;
import dk.diku.pcsd.assignment3.master.impl.KeyNotFoundException_Exception;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplService;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplServiceService;
import dk.diku.pcsd.assignment3.master.impl.Pair;
import dk.diku.pcsd.assignment3.master.impl.PairImpl;
import dk.diku.pcsd.assignment3.master.impl.ServiceAlreadyInitializedException_Exception;
import dk.diku.pcsd.assignment3.master.impl.ServiceInitializingException_Exception;
import dk.diku.pcsd.assignment3.master.impl.ServiceNotInitializedException_Exception;
import dk.diku.pcsd.assignment3.master.impl.ValueImpl;
import dk.diku.pcsd.assignment3.master.impl.ValueListImpl;

public class BulkPutTest {
	static KeyValueBaseMasterImplServiceService kvbiss;
	static KeyValueBaseMasterImplService kvbis;

	static HashMap<String, String> testMap = new HashMap<String, String>();
	HashMap<String, String> updated;
	static ArrayList<String> keys;
	ArrayList<String> updatedKeys;

	static boolean success = true;
	int N = 5000;

	@BeforeClass
	public static void testSetup() {
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
		Random rnd = new Random();

		// write N key values in hashmap and store
		for (int i = 0; i < N; i++) {
			String keyValue = String.valueOf(rnd.nextInt(999999));
			String resultValue = String.valueOf(rnd.nextInt(99999));

			KeyImpl key = new KeyImpl();
			key.setKey(keyValue);

			ValueImpl value = new ValueImpl();
			value.setValue(resultValue);

			ValueListImpl valueList = new ValueListImpl();
			valueList.getValueList().add(value);
			try {
				kvbis.insert(key, valueList);
				testMap.put(keyValue, resultValue);
			} catch (KeyAlreadyPresentException_Exception e) {
				// do nothing
			} catch (IOException_Exception e) {
				e.printStackTrace();
			} catch (ServiceNotInitializedException_Exception e) {
				e.printStackTrace();
			}
		}

		keys = new ArrayList<String>(testMap.keySet());
		Collections.sort(keys);
		updated = new HashMap<String, String>(testMap);
		updatedKeys = new ArrayList<String>();
		List<Pair> pairs = new ArrayList<Pair>();

		for (int i = 0; i < N; i++) {
			String randomUpdateValue = String.valueOf(rnd.nextInt(99999));
			String randomUpdateKey = keys.get(rnd.nextInt(keys.size()));

			PairImpl p = new PairImpl();
			KeyImpl k = new KeyImpl();
			k.setKey(randomUpdateKey);
			p.setKey(k);

			ValueImpl v = new ValueImpl();
			v.setValue(randomUpdateValue);
			ValueListImpl vl = new ValueListImpl();
			vl.getValueList().add(v);

			p.setValue(vl);

			pairs.add(p);
			updated.put(randomUpdateKey, randomUpdateValue);
			updatedKeys.add(randomUpdateKey);
		}

		Collections.sort(updatedKeys);

		Reader reader = new Reader();
		Reader reader2 = new Reader();
		Thread readerThread = new Thread(reader);
		Thread readerThread2 = new Thread(reader2);
		readerThread.start();
		readerThread2.start();
		try {
			kvbis.bulkPut(pairs);

			readerThread.join();
			readerThread2.join();

			assertTrue(success);
		} catch (IOException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceNotInitializedException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private class Reader implements Runnable {

		@Override
		public void run() {
			boolean foundNewVal = false;
			Random rnd = new Random();

			KeyImpl k = new KeyImpl();

			for (int i = -1; i != 0; i--) {
				try {
					String key = updatedKeys
							.get(rnd.nextInt(updatedKeys.size()));
					k.setKey(key);

					ValueListImpl value = (ValueListImpl)kvbis.read(k).getV();
					if (foundNewVal) {
						assertEquals(updated.get(key), value.getValueList()
								.get(0).getValue());
						success = success
								&& updated.get(key).equals(
										value.getValueList().get(0).getValue());
					} else {
						foundNewVal = !testMap.get(key).equals(
								value.getValueList().get(0).getValue());
						i = 500;
					}

				} catch (IOException_Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (KeyNotFoundException_Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServiceNotInitializedException_Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

}
