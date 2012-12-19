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
						LogRecord r = signalQueue.peek().record;
						// do not replicate configure log requests
						if(!r.getMethodName().equals("config")) {
							dk.diku.pcsd.assignment3.slave.impl.LogRecord rec = new dk.diku.pcsd.assignment3.slave.impl.LogRecord();
							rec.setClassName(r.getSrcClass());
							dk.diku.pcsd.assignment3.slave.impl.TimestampLog ts = new dk.diku.pcsd.assignment3.slave.impl.TimestampLog();
							ts.setInd(r.getLSN().getInd());
							rec.setLSN(ts);
							rec.setMethodName(r.getMethodName());
							rec.setNumberParam(r.getNumParams());
							rec.getParams().addAll(Arrays.asList(translate(r.getParams())));
							
							for (KeyValueBaseSlaveImplService s : slaves) {
								try {
									s.logApply(rec);
								} catch (Exception e) {
									e.printStackTrace();
								}
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
			} catch (InterruptedException e1) {
				// replicator got interrupted
				execute = false;
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
	
	private Object[] translate(Object[] in){
		Object[] result = new Object[in.length];
		
		for (int i=0; i<in.length; i++){
			if (in[i] instanceof KeyImpl){
				KeyImpl current = (KeyImpl)in[i];
				dk.diku.pcsd.assignment3.slave.impl.KeyImpl k = new dk.diku.pcsd.assignment3.slave.impl.KeyImpl();
				k.setKey(current.getKey());
				result[i] = k;
			}else if (in[i] instanceof ValueListImpl){
				ValueListImpl current = (ValueListImpl)in[i];
				dk.diku.pcsd.assignment3.slave.impl.ValueListImpl vl = new dk.diku.pcsd.assignment3.slave.impl.ValueListImpl();
				for (ValueImpl vin : current.getValueList()){
					dk.diku.pcsd.assignment3.slave.impl.ValueImpl v = new dk.diku.pcsd.assignment3.slave.impl.ValueImpl();
					v.setValue(vin.getValue());
					vl.getValueList().add(v);
				}
				result[i] = vl;
			}else if (in[i] instanceof List<?>){
				// has to be List<Pair<KeyImpl, ValueListImpl>>
				List<?> listIn = (List<?>) in[i];
				List<dk.diku.pcsd.assignment3.slave.impl.Pair> list = new ArrayList<dk.diku.pcsd.assignment3.slave.impl.Pair>();
				
				for (Object o : listIn){
					Pair<KeyImpl, ValueListImpl> pIn = (Pair<KeyImpl, ValueListImpl>) o;
					dk.diku.pcsd.assignment3.slave.impl.PairImpl p = new dk.diku.pcsd.assignment3.slave.impl.PairImpl();
					dk.diku.pcsd.assignment3.slave.impl.KeyImpl k = new dk.diku.pcsd.assignment3.slave.impl.KeyImpl();
					k.setKey(pIn.getKey().getKey());
					p.setK(k);
					dk.diku.pcsd.assignment3.slave.impl.ValueListImpl vl = new dk.diku.pcsd.assignment3.slave.impl.ValueListImpl();
					for (ValueImpl vin : pIn.getValue().getValueList()){
						dk.diku.pcsd.assignment3.slave.impl.ValueImpl v = new dk.diku.pcsd.assignment3.slave.impl.ValueImpl();
						v.setValue(vin.getValue());
						vl.getValueList().add(v);
					}
					p.setV(vl);
					list.add(p);
				}
				result[i] = list;
			}else{
				result[i] = in[i];
			}
			System.out.flush();
		}
		return result;
	}
}
