package dk.diku.pcsd.assignment3.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import dk.diku.pcsd.keyvaluebase.exceptions.BeginGreaterThanEndException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyAlreadyPresentException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyNotFoundException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyInitializedException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceInitializingException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceNotInitializedException;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseLog;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;
import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.Predicate;

public class KeyValueBaseImpl implements KeyValueBaseLog<KeyImpl, ValueListImpl> {
	private IndexImpl index;
	private ReplicatorImpl replicator;
	
	private CheckpointerImpl checkpointer;
	private ReadWriteLock quiesceLock;
	
	private boolean initialized = false, initializing = false, logging = true, recovering = false;

	public KeyValueBaseImpl() {
		this(IndexImpl.getInstance(), ReplicatorImpl.getInstance(), CheckpointerImpl.getInstance());
	}

	private KeyValueBaseImpl(IndexImpl index, ReplicatorImpl replicator, CheckpointerImpl checkpointer) {
		quiesceLock = new ReentrantReadWriteLock(true);
		this.replicator = replicator; this.checkpointer = checkpointer;
		this.checkpointer.init(this);
		
		if(this.replicator.canRecover()) {
			this.index = this.replicator.recoverIndex();
			List<LogRecord> records = this.replicator.recoverLogRecords();
			
			// if we have not the situation that the first log entry is init
			// then we must declare the service as initialized and start the logger and checkpointer
			// because the log is only created after init
			initialized = !(records.size() > 0 && records.get(0).getMethodName().equals("init"));
			boolean wasInitialized = initialized;
			
			logging = false; recovering = true;
			for(LogRecord record : records) {
				try {
					record.invoke(this);
				} catch (Exception e) {
					// throw new RuntimeException(e.getMessage(), e);
				}
			}
			logging = true; recovering = false;

			if(wasInitialized) {
				new Thread(this.replicator).start();
				new Thread(this.checkpointer).start();
			}
		} else
			this.index = index;
	}
	
	@Override
	protected void finalize() throws Throwable {
		replicator.stop();
		checkpointer.stop();
		super.finalize();
	}

	@Override
	public void init(String serverFilename)
			throws ServiceAlreadyInitializedException,
			ServiceInitializingException, FileNotFoundException {
		quiesceLock.readLock().lock();
		if (initialized) {
			quiesceLock.readLock().unlock();
			throw new ServiceAlreadyInitializedException();
		}
		if (initializing) {
			quiesceLock.readLock().unlock();
			throw new ServiceInitializingException();
		}
		
		initializing = true;
		
		if(logging || recovering)
			new Thread(replicator).start();
		new Thread(checkpointer).start();
		
		if(logging) log("init", serverFilename);

		if ("".equals(serverFilename)) {
			initialized = true;
			initializing = false;
			quiesceLock.readLock().unlock();
			return;
		}

		String tmpDir = System.getProperty("java.io.tmpdir");
		String filePath;
		if (tmpDir.endsWith(File.separator))
			filePath = tmpDir + serverFilename;
		else
			filePath = tmpDir + File.separator + serverFilename;

		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);

		
		String current;
		try {
			current = br.readLine();
			String currentKey = null;
			ValueListImpl currentValues = null;
			while (current != null) {

				String[] values = current.split("\\s", 2);

				if (values.length > 1) {
					String key = values[0];
					if (!key.equals(currentKey)) {

						if (currentKey != null) {
							index.insert(new KeyImpl(currentKey), currentValues);
						}

						currentKey = key;
						currentValues = new ValueListImpl();
					}

					for (int i = 1; i < values.length; i++) {
						try {
							// try if we have a number
							currentValues.add(new ValueImpl(Integer.parseInt(values[i])));
						} catch(NumberFormatException e) {
							// otherwise use the string
							currentValues.add(new ValueImpl(values[i]));
						}
					}
				}

				current = br.readLine();
			}

			if (currentKey != null) {
				index.insert(new KeyImpl(currentKey), currentValues);
			}

			br.close();

			initialized = true;
			initializing = false;
		} catch (IOException e) {
			// Throw a FileNotFoundException instead.
			throw new FileNotFoundException(e.getMessage());
		} catch (KeyAlreadyPresentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			quiesceLock.readLock().unlock();
		}
	}

	@Override
	public ValueListImpl read(KeyImpl k) throws KeyNotFoundException,
			IOException, ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		if (!initialized) {
			quiesceLock.readLock().unlock();
			throw new ServiceNotInitializedException();
		}
		ValueListImpl v = index.get(k);
		quiesceLock.readLock().unlock();
		return v;
	}

	@Override
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException,
			ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		if (!initialized) {
			quiesceLock.readLock().unlock();
			throw new ServiceNotInitializedException();
		}
		if(logging) log("insert", k, v);
		index.insert(k, v);
		quiesceLock.readLock().unlock();
	}

	@Override
	public void update(KeyImpl k, ValueListImpl newV)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		if (!initialized) {
			quiesceLock.readLock().unlock();
			throw new ServiceNotInitializedException();
		}
		if(logging) log("update", k, newV);
		index.update(k, newV);
		quiesceLock.readLock().unlock();
	}

	@Override
	public void delete(KeyImpl k) throws KeyNotFoundException,
			ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		if (!initialized) {
			quiesceLock.readLock().unlock();
			throw new ServiceNotInitializedException();
		}
		if(logging) log("delete", k);
		index.remove(k);
		quiesceLock.readLock().unlock();
	}

	@Override
	public List<ValueListImpl> scan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		if (!initialized) {
			quiesceLock.readLock().unlock();
			throw new ServiceNotInitializedException();
		}
		List<ValueListImpl> allValues = index.scan(begin, end);
		for (Iterator<ValueListImpl> i = allValues.iterator(); i.hasNext();) {
			ValueListImpl current = i.next();
			if (!p.evaluate(current))
				i.remove();
		}
		quiesceLock.readLock().unlock();
		return allValues;
	}

	@Override
	public List<ValueListImpl> atomicScan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		if (!initialized) {
			quiesceLock.readLock().unlock();
			throw new ServiceNotInitializedException();
		}
		List<ValueListImpl> allValues = index.atomicScan(begin, end);
		for (Iterator<ValueListImpl> i = allValues.iterator(); i.hasNext();) {
			ValueListImpl current = i.next();
			if (!p.evaluate(current))
				i.remove();
		}
		quiesceLock.readLock().unlock();
		return allValues;
	}

	@Override
	public void bulkPut(List<Pair<KeyImpl, ValueListImpl>> mappings)
			throws IOException, ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		if (!initialized) {
			quiesceLock.readLock().unlock();
			throw new ServiceNotInitializedException();
		}
		if(logging) log("bulkPut", mappings);
		index.bulkPut(mappings);
		quiesceLock.readLock().unlock();
	}
	
	@Override
	public void quiesce() {
		quiesceLock.writeLock().lock();
	}

	@Override
	public void resume() {
		quiesceLock.writeLock().unlock();
	}
	
	/**
	 * Logging helper that waits for the log to be written.
	 * 
	 * @param methodName
	 * @param params
	 */
	private void log(String methodName, Object... params) {
		try {
			replicator.logRequest(new LogRecord(getClass(), methodName, params)).get();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
