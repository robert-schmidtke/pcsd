package data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dk.diku.pcsd.assignment1.impl.FileNotFoundException_Exception;
import dk.diku.pcsd.assignment1.impl.IOException_Exception;
import dk.diku.pcsd.assignment1.impl.KeyImpl;
import dk.diku.pcsd.assignment1.impl.KeyNotFoundException_Exception;
import dk.diku.pcsd.assignment1.impl.KeyValueBaseImplService;
import dk.diku.pcsd.assignment1.impl.KeyValueBaseImplServiceService;
import dk.diku.pcsd.assignment1.impl.ServiceAlreadyInitializedException_Exception;
import dk.diku.pcsd.assignment1.impl.ServiceInitializingException_Exception;
import dk.diku.pcsd.assignment1.impl.ServiceNotInitializedException_Exception;
import dk.diku.pcsd.assignment1.impl.ValueImpl;

public class LoadReadExperiment {
	static KeyValueBaseImplServiceService kvbiss;
	static KeyValueBaseImplService kvbis;

	public static void main(String[] args){
		kvbiss = new KeyValueBaseImplServiceService();
		kvbis = kvbiss.getKeyValueBaseImplServicePort();
		
		try {
			//put in Twitter data set
			kvbis.init("twitter.small");
		} catch (FileNotFoundException_Exception e) {
			e.printStackTrace();
		} catch (ServiceAlreadyInitializedException_Exception e) {
			e.printStackTrace();
		} catch (ServiceInitializingException_Exception e) {
			e.printStackTrace();
		}
		
		//how many threads
		int numOfThreadSize = 5;
		int[] numOfThreads = new int[numOfThreadSize];
		
		//initialize different numbers of threads
		int currentNum = 1;
		for (int i = 0; i < numOfThreads.length; i++){
			numOfThreads[i] = currentNum;
			currentNum += 10;
		}

		//how many reads per thread
		int n = 100;
		
		//get Zipf keys
		int COUNT = 10856;
		//int COUNT = 61578403; //highest key -11
		float theta = (float) 0.8;
		ZipfGenerator zg = new ZipfGenerator(COUNT, theta);
		Random rnd = new Random();
		
		List<Long> zipfs = new ArrayList<Long>();
		for(int i = 0; i < n; ++i)
			zipfs.add(zg.calculateZipf(rnd.nextDouble()));
		
		//iterate over different thread numbers
		for (int j = 0; j < numOfThreads.length; j++){
			//get the current number of threads
			int h = numOfThreads[j];
			
			long overallAverage = 0;
			
			//make threads and let them read
			ExecutorService executor = Executors.newFixedThreadPool(h);
			
			List<Future<Long>> results;
			try {
				List<Callable<Long>> threadList = new ArrayList<Callable<Long>>();
				for(int i = 0; i < h; i++)
					threadList.add(new ReadThread(zipfs));
				results = executor.invokeAll(threadList);
				
				for (Future<Long> result : results) {
					overallAverage += result.get(); // Prints "myResult" after 2 seconds.
		        }
			    executor.shutdown();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			
			System.out.println(h + " " + ((overallAverage/h)/1000000));
		}
	}
	
	public static class ReadThread implements Callable<Long> {
		List<Long> zipfs;
		ReadThread(List<Long> zipfs) {
			  this.zipfs = zipfs;
		}
		
		public Long call() {
			
			long threadSum = 0;

			for (int i=0;i<zipfs.size();i++){
				//get key
		        long rank = zipfs.get(i);
		        
		        KeyImpl key = new KeyImpl();
				key.setKey(Long.toString(rank+11));//+11 to avoid having a large number of miss hits
				
				//start measurement
				long startTime = System.nanoTime();
				
				try {
					List<ValueImpl> tempValueList = kvbis.read(key).getValueList();
				} //in case of errors do nothing
				catch (IOException_Exception e) {} 
				catch (KeyNotFoundException_Exception e) {} 
				catch (ServiceNotInitializedException_Exception e) {}

		        //stop measurement
				threadSum += (System.nanoTime() - startTime);
				
			}

			//write measurement
			return threadSum/zipfs.size();
			
		}
	} 
}
