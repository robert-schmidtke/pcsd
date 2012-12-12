package dk.diku.pcsd.assignment2.impl.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dk.diku.pcsd.assignment2.impl.KeyImpl;
import dk.diku.pcsd.assignment2.impl.KeyValueBaseImplService;
import dk.diku.pcsd.assignment2.impl.KeyValueBaseImplServiceService;
import dk.diku.pcsd.assignment2.impl.ValueImpl;
import dk.diku.pcsd.assignment2.impl.ValueListImpl;

public class ExperimentClient {
	
	public static void main(String[] args) {
		new ExperimentClient().runExperiment();
	}
	
	public void runExperiment() {
		KeyValueBaseImplServiceService kvbiss = new KeyValueBaseImplServiceService();
		KeyValueBaseImplService kvbis = kvbiss.getKeyValueBaseImplServicePort();
		
		int[] threadCounts = new int[] { 1, 2, 3, 4, 5, 6 };
		int repititions = 5;
		int keyFrom = 1000, keyTo = 9999;
		
		// preserve insertion order
		Map<KeyImpl, ValueListImpl> values = new LinkedHashMap<KeyImpl, ValueListImpl>();
		for(int i = keyFrom; i <= keyTo; ++i) {
			KeyImpl key = new KeyImpl();
			key.setKey(Integer.toString(i));
			
			ValueListImpl valueList = new ValueListImpl();
			ValueImpl value = new ValueImpl();
			value.setValue(i);
			valueList.getValueList().add(value);
			
			values.put(key, valueList);
		}
		
		try {
			kvbis.init("dk/init.list");
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		long[] subResults = new long[threadCounts.length];
		for(int i = 0; i < threadCounts.length; ++i)
			subResults[i] = runSubExperiment(kvbis, threadCounts[i], repititions, values);
		
		for(int i = 0; i < subResults.length; ++i) {
			System.out.println(threadCounts[i] + " threads: " + subResults[i] + "ns");
		}
	}
	
	public Long runSubExperiment(final KeyValueBaseImplService kvbis, final int threadCount, final int repititions, final Map<KeyImpl, ValueListImpl> values) {
		
		System.out.println("Running sub experiment with " + threadCount + " threads ...");
		
		List<Callable<Long>> workers = new ArrayList<Callable<Long>>();
		for(int i = 0; i < threadCount; ++i) {
			workers.add(new Worker(repititions, values, kvbis));
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		List<Future<Long>> results;
		try {
			results = executor.invokeAll(workers);
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		long averageTime = 0;
		for(Future<Long> result : results) {
			try {
				averageTime += result.get();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		
		executor.shutdown();
		
		return averageTime / threadCount;
		
	}
	
	private static class Worker implements Callable<Long> {
		
		private int repititions;
		
		private Map<KeyImpl, ValueListImpl> values;
		
		private KeyValueBaseImplService kvbis;
		
		public Worker(int repititions, Map<KeyImpl, ValueListImpl> values, KeyValueBaseImplService kvbis) {
			this.repititions = repititions;
			this.values = values;
			this.kvbis = kvbis;
		}
		
		@Override
		public Long call() {
			long averageTime = 0;
			
			for(int i = 0; i < repititions; ++i) {
				
				System.out.println(Thread.currentThread().getId() + ": Running repitition " + (i+1) + "/" + repititions + "... ");
				Iterator<Entry<KeyImpl, ValueListImpl>> it = values.entrySet().iterator();
				while(it.hasNext()) {
					Entry<KeyImpl, ValueListImpl> entry = it.next();
					try {
						long start = System.nanoTime();
						kvbis.update(entry.getKey(), entry.getValue());
						averageTime += (System.nanoTime() - start);
					} catch(Exception e) {
						throw new RuntimeException(e.getMessage(), e);
					}
				}
				System.out.println(Thread.currentThread().getId() + ": done " + (i+1) + "/" + repititions);
			}
			
			return averageTime / (repititions * values.size());
		}
	}

}
