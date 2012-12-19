package dk.diku.pcsd.assignment3.master.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import dk.diku.pcsd.assignment3.impl.KeyImpl;
import dk.diku.pcsd.assignment3.impl.ValueImpl;
import dk.diku.pcsd.assignment3.impl.ValueListImpl;
import dk.diku.pcsd.assignment3.slave.impl.KeyValueBaseSlaveImplService;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;
import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.Replicator;

/**
 * This class extends your MyLogger from assignment 2 to use its logic, and it
 * is a suggestion. You can choose to follow it or lint to your last
 * assignment's implementation in your own way.
 */

public class ReplicatorImpl extends LoggerImpl implements Replicator {

	private List<KeyValueBaseSlaveImplService> slaves = new ArrayList<KeyValueBaseSlaveImplService>();

	private static ReplicatorImpl instance;

	public static ReplicatorImpl getInstance() {
		if (instance == null)
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

		// remember processed log requests so we can signal them later
		LinkedList<LogQueueEntry<Date>> signalQueue = new LinkedList<LogQueueEntry<Date>>();
		while (execute) {
			LogQueueEntry<Date> next;
			try {
				// wait for log request to appear
				next = logQueue.poll(10L, TimeUnit.SECONDS);
			} catch (InterruptedException e1) {
				next = null;
			}
			
			if(next != null)
				signalQueue.add(next);
			
			// if we have reached a certain amount of requests
			// or some time has passed
			// then write the log records
			if (signalQueue.size() >= K ||
					(signalQueue.size() > 0 && System.currentTimeMillis() - lastRun >= TIMEOUT)) {
				
				for(LogQueueEntry<Date> entry : signalQueue) {
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

				while (signalQueue.size() > 0) {
					for (KeyValueBaseSlaveImplService s : slaves) {
						LogRecord r = signalQueue.peek().record;
						

						try {
							s.logApply(r);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					signalQueue.poll().future.signalAll(new Date());
				}

				lastRun = System.currentTimeMillis();
			}

			if (truncate) {
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
		} catch (Exception e) {
			// maybe it was flushed and closed before
		}
	}

	public void setSlaves(List<KeyValueBaseSlaveImplService> s) {
		this.slaves = s;
	}
	
	
}
