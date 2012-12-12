package dk.diku.pcsd.assignment2.impl;

import dk.diku.pcsd.keyvaluebase.interfaces.Checkpointer;

public class CheckpointerImpl implements Checkpointer {
	
	private static final long INTERVAL = 900000;
	
	private boolean execute;
	
	private KeyValueBaseImpl keyValueBase;
	
	private static CheckpointerImpl instance;
	
	public static CheckpointerImpl getInstance() {
		if(instance == null)
			instance = new CheckpointerImpl();
		return instance;
	}
	
	private CheckpointerImpl() {
		execute = false;
	}
	
	public void init(KeyValueBaseImpl keyValueBase) {
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
			LoggerImpl.getInstance().truncate();
			keyValueBase.resume();
		}
	}
	
	public void stop() {
		execute = false;
	}

}
