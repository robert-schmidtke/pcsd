package test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.diku.pcsd.assignment1.impl.KeyImpl;
import dk.diku.pcsd.assignment1.impl.KeyValueBaseImplService;
import dk.diku.pcsd.assignment1.impl.KeyValueBaseImplServiceService;
import dk.diku.pcsd.assignment1.impl.StringLengthPredicate;
import dk.diku.pcsd.assignment1.impl.ValueImpl;
import dk.diku.pcsd.assignment1.impl.ValueListImpl;

public class ScanAndBulkPutTest {
	
	private static final KeyValueBaseImplService kvbis = new KeyValueBaseImplServiceService().getKeyValueBaseImplServicePort();
	
	@BeforeClass
	public static void setup() {
		try {
			kvbis.init(null);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	@Test
	public void testScan() {
		final int numKeys = 5000;
		final int keyLength = Integer.toString(numKeys).length();
		
		for(int i = 0; i < numKeys; ++i) {
			KeyImpl key = new KeyImpl();
			key.setKey(padZeros(Integer.toString(i), keyLength));
			
			ValueListImpl valueList = new ValueListImpl();
			ValueImpl value = new ValueImpl();
			value.setValue("initialValue");
			valueList.getValueList().add(value);
			
			try {
				kvbis.insert(key, valueList);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			} 
		}
		
		// start the scan
		final KeyImpl from = new KeyImpl();
		from.setKey(padZeros("0", keyLength));
		final KeyImpl to = new KeyImpl();
		to.setKey(Integer.toString(numKeys));
		final List<ValueListImpl> scanResult = new ArrayList<ValueListImpl>();
		Thread scanner = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					scanResult.addAll(kvbis.scan(from, to, new StringLengthPredicate()));
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage(), e.getCause());
				}
			}
		});
		scanner.start();
		
		// inserter thread
		Thread inserter = new Thread(new Runnable() {
			@Override
			public void run() {
				// update keys
				for(int i = 0; i < numKeys; ++i) {
					KeyImpl key = new KeyImpl();
					key.setKey(padZeros(Integer.toString(i), keyLength));
					
					ValueListImpl valueList = new ValueListImpl();
					ValueImpl value = new ValueImpl();
					value.setValue("newV");
					valueList.getValueList().add(value);
					
					try {
						kvbis.update(key, valueList);
					} catch (Exception e) {
						throw new RuntimeException(e.getMessage(), e);
					} 
				}
			}
		});
		inserter.start();
		
		try {
			inserter.join();
			scanner.join();
		} catch(InterruptedException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		Assert.assertTrue("Invalid size of result set: " + scanResult.size() + ", expecting less than " + numKeys, scanResult.size() < numKeys);
	}
	
	@Test
	public void testAtomicScan() {
		final int offset = 5001;
		final int numKeys = 10000;
		final int keyLength = Integer.toString(numKeys).length();
		
		// insert keys with even number
		for(int i = offset; i < numKeys + offset; i += 2) {
			KeyImpl key = new KeyImpl();
			key.setKey(padZeros(Integer.toString(i), keyLength));
			
			ValueListImpl valueList = new ValueListImpl();
			ValueImpl value = new ValueImpl();
			value.setValue("youShouldFindThis");
			valueList.getValueList().add(value);
			
			try {
				kvbis.insert(key, valueList);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			} 
		}
		
		// start the scan
		final KeyImpl from = new KeyImpl();
		from.setKey(padZeros(Integer.toString(offset), keyLength));
		final KeyImpl to = new KeyImpl();
		to.setKey(Integer.toString(offset + numKeys));
		final List<ValueListImpl> scanResult = new ArrayList<ValueListImpl>();
		Thread scanner = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					scanResult.addAll(kvbis.atomicScan(from, to, new StringLengthPredicate()));
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage(), e.getCause());
				}
			}
		});
		scanner.start();
		
		// inserter thread
		Thread inserter = new Thread(new Runnable() {
			@Override
			public void run() {
				// insert keys with odd number
				for(int i = offset + 1; i < numKeys + offset; i += 2) {
					KeyImpl key = new KeyImpl();
					key.setKey(padZeros(Integer.toString(i), keyLength));
					
					ValueListImpl valueList = new ValueListImpl();
					ValueImpl value = new ValueImpl();
					value.setValue("youShouldNotFindThis");
					valueList.getValueList().add(value);
					
					try {
						kvbis.insert(key, valueList);
					} catch (Exception e) {
						throw new RuntimeException(e.getMessage(), e);
					} 
				}
			}
		});
		inserter.start();
		
		try {
			inserter.join();
			scanner.join();
		} catch(InterruptedException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		Assert.assertTrue("Invalid size of result set: " + scanResult.size() + ", expecting " + (numKeys / 2), scanResult.size() == (numKeys / 2));
		for(ValueListImpl valueList : scanResult)
			Assert.assertEquals("Found unexpected value!", "youShouldFindThis", valueList.getValueList().get(0).getValue());
	}
	
	@Test
	public void testBulkPut() {
		
	}
	
	private String padZeros(String s, int length) {
		while(s.length() < length)
			s = "0" + s;
		return s;
	}

}
