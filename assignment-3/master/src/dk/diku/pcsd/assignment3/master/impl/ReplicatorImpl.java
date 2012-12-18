package dk.diku.pcsd.assignment3.master.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.Future;

import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;
import dk.diku.pcsd.keyvaluebase.interfaces.Replicator;

/**
 * This class extends your MyLogger from assignment 2 to use its
 * logic, and it is a suggestion. You can choose to follow it
 * or lint to your last assignment's implementation in your own way.
 */

public class ReplicatorImpl extends LoggerImpl implements Replicator {
	
	private static ReplicatorImpl instance;
	
	public static ReplicatorImpl getInstance() {
		if(instance == null)
			instance = new ReplicatorImpl();
		return instance;
	}
	
	private ReplicatorImpl() {
		// FIXME well this probably needs to be changed
		super();
	}

	@Override
	public Future<?> makeStable(LogRecord record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		execute = true;
		initOutputStream();
		long lastRun = System.currentTimeMillis();
		while(execute) {
			if(logQueue.size() >= K || (logQueue.size() > 0 && System.currentTimeMillis() - lastRun >= TIMEOUT)) {
				Iterator<LogQueueEntry<Date>> it = logQueue.iterator();
				while(it.hasNext()) {
					LogQueueEntry<Date> entry = it.next();
					try {
						out.writeObject(entry.record);
					} catch (IOException e) {
						throw new RuntimeException(e.getMessage(), e);
					}
				}
				
				try {
					out.flush();
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
				
				while(logQueue.size() > 0) {
					logQueue.poll().future.signalAll(new Date());
				}
				
				lastRun = System.currentTimeMillis();
			}
			
			if(truncate) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
				
				logFile.delete();
				initOutputStream();
				truncate = false;
			}
		}
		
		try {
			out.flush();
			out.close();
		} catch(Exception e) {
			// maybe it was flushed and closed before
		}
	}

}
