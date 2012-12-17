package dk.diku.pcsd.assignment3.impl;

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
		super.run();
	}

}
