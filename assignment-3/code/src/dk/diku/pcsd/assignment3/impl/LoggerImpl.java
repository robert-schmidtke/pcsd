package dk.diku.pcsd.assignment3.impl;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;

import dk.diku.pcsd.keyvaluebase.interfaces.FutureLog;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;
import dk.diku.pcsd.keyvaluebase.interfaces.Logger;

public class LoggerImpl implements Logger {
	
	private static final int K = 1, TIMEOUT = 0;
		
	private boolean execute, truncate;
	
	private LinkedBlockingQueue<LogQueueEntry<Date>> logQueue;
	
	private File logFile;
	private ObjectOutputStream out;
		
	protected LoggerImpl() {
		logQueue = new LinkedBlockingQueue<LogQueueEntry<Date>>();
		execute = false; truncate = false;
		
		String tmpDir = System.getProperty("java.io.tmpdir");
		if(!tmpDir.endsWith(File.separator))
			tmpDir += File.separator;
		logFile = new File(tmpDir + getClass().getPackage().getName().replaceAll("\\.", Matcher.quoteReplacement(File.separator)) + File.separator + "records.log");
		logFile.getParentFile().mkdirs();
	}
	
	private void initOutputStream() {
		try {
			if(logFile.exists())
				out = new AppendableObjectOutputStream(new FileOutputStream(logFile, true));
			else {
				out = new ObjectOutputStream(new FileOutputStream(logFile));
			
				// always write the index at the beginning
				out.writeLong(IndexImpl.getInstance().getFileLength());
				out.writeObject(IndexImpl.getInstance().getEmptyList());
				out.writeObject(IndexImpl.getInstance().getMappings());
				out.flush();
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		out.close();
		super.finalize();
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
	
	public void stop() {
		execute = false;
	}
	
	public void truncate() {
		truncate = true;
	}

	@Override
	public Future<?> logRequest(LogRecord record) {
		FutureLog<Date> future = new FutureLog<Date>();
		logQueue.add(new LogQueueEntry<Date>(record, future));
		return future;
	}
	
	public boolean canRecover() {
		return logFile.exists();
	}
	
	public IndexImpl recoverIndex() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(logFile));
			IndexImpl index = IndexImpl.getInstance();
			index.init(in.readLong(), (SortedSet<SpaceIdent>) in.readObject(), (Map<KeyImpl, SpaceIdent>) in.readObject());
			in.close();
			return index;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public List<LogRecord> recoverLogRecords() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(logFile));
			in.readLong(); in.readObject(); in.readObject(); // read index impl
			List<LogRecord> records = new ArrayList<LogRecord>();
			boolean read = true;
			while(read) {
				try {
					records.add((LogRecord) in.readObject());
				} catch(EOFException e) {
					// phew, this is bad
					read = false;
				}
			}
			in.close();
			return records;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	private static class LogQueueEntry<T> {
		public LogRecord record;
		public FutureLog<T> future;
		public LogQueueEntry(LogRecord record, FutureLog<T> future) {
			this.record = record; this.future = future;
		}
	}
	
	private static class AppendableObjectOutputStream extends ObjectOutputStream {
		public AppendableObjectOutputStream(OutputStream os) throws IOException {
			super(os);
		}
		
		@Override
		protected void writeStreamHeader() throws IOException {
			// if we append we don't want the header
			reset();
		}
	}

}
