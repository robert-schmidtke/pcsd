package dk.diku.pcsd.assignment3.impl;

import dk.diku.pcsd.keyvaluebase.interfaces.Checkpointer;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseLog;

public class CheckpointerImpl implements Checkpointer {
	
	private static final long INTERVAL = 60000;
	
	private boolean execute;
	
	private KeyValueBaseLog<KeyImpl, ValueListImpl> keyValueBase;
	
	private static CheckpointerImpl instance;
	
	public static CheckpointerImpl getInstance() {
		if(instance == null)
			instance = new CheckpointerImpl();
		return instance;
	}
	
	private CheckpointerImpl() {
		execute = false;
	}
	
	public void init(KeyValueBaseLog<KeyImpl, ValueListImpl> keyValueBase) {
		this.keyValueBase = keyValueBase;
	}

	@Override
	public void run() {
		execute = true;
		while(execute) {
			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			
			keyValueBase.quiesce();
			StoreImpl.getInstance().flush();
			ReplicatorImpl.getInstance().truncate();
			keyValueBase.resume();
		}
	}
	
	public void stop() {
		execute = false;
	}

}
