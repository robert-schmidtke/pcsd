package dk.diku.pcsd.assignment3.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import dk.diku.pcsd.keyvaluebase.exceptions.BeginGreaterThanEndException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyAlreadyPresentException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyNotFoundException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyConfiguredException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyInitializedException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceInitializingException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceNotInitializedException;
import dk.diku.pcsd.keyvaluebase.interfaces.Configuration;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseLog;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseMaster;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;
import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.Predicate;
import dk.diku.pcsd.keyvaluebase.interfaces.TimestampLog;

public class KeyValueBaseMasterImpl extends KeyValueBaseReplicaImpl implements
		KeyValueBaseMaster<KeyImpl, ValueListImpl>,
		KeyValueBaseLog<KeyImpl, ValueListImpl> {

	private ReadWriteLock quiesceLock;

	private CheckpointerImpl checkpointer;

	private ReplicatorImpl replicator;

	private boolean logging = true, recovering = false;

	public KeyValueBaseMasterImpl() {
		this(IndexImpl.getInstance(), ReplicatorImpl.getInstance(),
				CheckpointerImpl.getInstance());
	}

	private KeyValueBaseMasterImpl(IndexImpl index, ReplicatorImpl replicator,
			CheckpointerImpl checkpointer) {
		quiesceLock = new ReentrantReadWriteLock(true);
		this.replicator = replicator;
		this.checkpointer = checkpointer;
		this.checkpointer.init(this);

		if (this.replicator.canRecover()) {
			this.index = this.replicator.recoverIndex();
			List<LogRecord> records = this.replicator.recoverLogRecords();

			// if we have not the situation that the first log entry is init
			// then we must declare the service as initialized and start the
			// logger and checkpointer
			// because the log is only created after init
			initialized = !(records.size() > 0 && records.get(0)
					.getMethodName().equals("init"));
			boolean wasInitialized = initialized;

			logging = false;
			recovering = true;
			for (LogRecord record : records) {
				try {
					record.invoke(this);
				} catch (Exception e) {
					// throw new RuntimeException(e.getMessage(), e);
				}
			}
			logging = true;
			recovering = false;

			if (wasInitialized) {
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
		try {
			if (initialized) {
				throw new ServiceAlreadyInitializedException();
			}
			if (initializing) {
				throw new ServiceInitializingException();
			}

			if (logging || recovering)
				new Thread(replicator).start();
			new Thread(checkpointer).start();

			if (logging)
				log("init", serverFilename);
			
			super.init(serverFilename);
		} finally {
			quiesceLock.readLock().unlock();
		}
	}

	@Override
	public Pair<TimestampLog, ValueListImpl> read(KeyImpl k)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		try {
			return super.read(k);
		} finally {
			quiesceLock.readLock().unlock();
		}
	}

	@Override
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException,
			ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		try {
			if (!initialized) {
				quiesceLock.readLock().unlock();
				throw new ServiceNotInitializedException();
			}
			if (logging)
				log("insert", k, v);
			index.insert(k, v);
		} finally {
			quiesceLock.readLock().unlock();
		}
	}

	@Override
	public void update(KeyImpl k, ValueListImpl newV)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		try {
			if (!initialized) {
				quiesceLock.readLock().unlock();
				throw new ServiceNotInitializedException();
			}
			if (logging)
				log("update", k, newV);
			index.update(k, newV);
		} finally {
			quiesceLock.readLock().unlock();
		}
	}

	@Override
	public void delete(KeyImpl k) throws KeyNotFoundException,
			ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		try {
			if (!initialized) {

				quiesceLock.readLock().unlock();
				throw new ServiceNotInitializedException();
			}
			if (logging)
				log("delete", k);
			index.remove(k);
		} finally {
			quiesceLock.readLock().unlock();
		}
	}

	@Override
	public Pair<TimestampLog, List<ValueListImpl>> scan(KeyImpl begin,
			KeyImpl end, Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		try {
			return super.scan(begin, end, p);
		} finally {
			quiesceLock.readLock().unlock();
		}
	}

	@Override
	public Pair<TimestampLog, List<ValueListImpl>> atomicScan(KeyImpl begin,
			KeyImpl end, Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		try {
			return super.atomicScan(begin, end, p);
		} finally {
			quiesceLock.readLock().unlock();
		}
	}

	@Override
	public void bulkPut(List<Pair<KeyImpl, ValueListImpl>> mappings)
			throws IOException, ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		if (!initialized) {
			quiesceLock.readLock().unlock();
			throw new ServiceNotInitializedException();
		}
		if (logging)
			log("bulkPut", mappings);
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
			replicator
					.logRequest(new LogRecord(getClass(), methodName, params))
					.get();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void config(Configuration conf)
			throws ServiceAlreadyConfiguredException {
		// TODO Auto-generated method stub

	}

}