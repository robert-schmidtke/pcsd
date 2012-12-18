package dk.diku.pcsd.assignment3.master.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

import dk.diku.pcsd.assignment3.impl.KeyImpl;
import dk.diku.pcsd.assignment3.impl.ValueImpl;
import dk.diku.pcsd.assignment3.impl.ValueListImpl;
import dk.diku.pcsd.assignment3.slave.impl.KeyValueBaseSlaveImplService;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;
import dk.diku.pcsd.keyvaluebase.interfaces.Replicator;

/**
 * This class extends your MyLogger from assignment 2 to use its logic, and it
 * is a suggestion. You can choose to follow it or lint to your last
 * assignment's implementation in your own way.
 */

public class ReplicatorImpl extends LoggerImpl implements Replicator {

	private List<KeyValueBaseSlaveImplService> slaves;

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
		while (execute) {
			if (logQueue.size() >= K
					|| (logQueue.size() > 0 && System.currentTimeMillis()
							- lastRun >= TIMEOUT)) {
				Iterator<LogQueueEntry<Date>> it = logQueue.iterator();
				while (it.hasNext()) {
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

				while (logQueue.size() > 0) {
					for (KeyValueBaseSlaveImplService s : slaves) {
						LogRecord r = logQueue.peek().record;
						dk.diku.pcsd.assignment3.slave.impl.LogRecord rec = new dk.diku.pcsd.assignment3.slave.impl.LogRecord();
						rec.setClassName(r.getSrcClass());
						dk.diku.pcsd.assignment3.slave.impl.TimestampLog ts = new dk.diku.pcsd.assignment3.slave.impl.TimestampLog();
						ts.setInd(r.getLSN().getInd());
						rec.setLSN(ts);
						rec.setMethodName(r.getMethodName());
						rec.setNumberParam(r.getNumParams());
						rec.getParams().addAll(Arrays.asList(translate(r.getParams())));

						try {
							s.logApply(rec);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					logQueue.poll().future.signalAll(new Date());
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
	
	private Object[] translate(Object[] in){
		Object[] result = new Object[in.length];
		System.out.println("in comes "+in.length);
		for (int i = 0; i<in.length; i++)
			System.out.println(in[i]);
		
		for (int i=0; i<in.length; i++){
			if (in[i] instanceof KeyImpl){
				System.out.println("is keyimpl");
				KeyImpl current = (KeyImpl)in[i];
				dk.diku.pcsd.assignment3.slave.impl.KeyImpl k = new dk.diku.pcsd.assignment3.slave.impl.KeyImpl();
				k.setKey(current.getKey());
				result[i] = k;
			}else if (in[i] instanceof ValueListImpl){
				System.out.println("is valuelistimpl");
				ValueListImpl current = (ValueListImpl)in[i];
				dk.diku.pcsd.assignment3.slave.impl.ValueListImpl vl = new dk.diku.pcsd.assignment3.slave.impl.ValueListImpl();
				for (ValueImpl vin : current.getValueList()){
					dk.diku.pcsd.assignment3.slave.impl.ValueImpl v = new dk.diku.pcsd.assignment3.slave.impl.ValueImpl();
					v.setValue(vin.getValue());
					vl.getValueList().add(v);
				}
				result[i] = vl;
			}else{
				System.out.println("is other: "+in[i].getClass());
				result[i] = in[i];
			}
			System.out.flush();
		}
		System.out.println("result is ");
		for (int i = 0; i<in.length; i++)
			System.out.println(result[i]);
		return result;
	}
}
