package dk.diku.pcsd.assignment1.impl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import dk.diku.pcsd.keyvaluebase.interfaces.MemoryMappedFile;
import dk.diku.pcsd.keyvaluebase.interfaces.Store;

public class StoreImpl implements Store
{
	
	private static StoreImpl instance = null;
	
	private final PrintWriter logFileWriter;
	
	private final long MMF_SIZE = 1024 * 1024;
	private final MemoryMappedFile mmf;
	private final RandomAccessFile mmfBase;
		
	public static StoreImpl getInstance() {
		if(instance == null)
				instance = new StoreImpl();
		return instance;
	}
	
	private StoreImpl() {
		try {
			logFileWriter = new PrintWriter("store.log");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Error creating log file for StoreImpl");
		}
		
		try {
			mmfBase = new RandomAccessFile("store.mmf", "rw");
			mmfBase.setLength(MMF_SIZE);
			mmf = new MemoryMappedFile(mmfBase.getChannel(), FileChannel.MapMode.READ_WRITE, 0, MMF_SIZE);
		} catch (Exception e) {
			e.printStackTrace(logFileWriter);
			logFileWriter.flush();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public byte[] read(Long position, int length) {
		try {
			byte[] dst = new byte[length];
			mmf.get(dst, position);
			return dst;
		} catch(Exception e) {
			e.printStackTrace(logFileWriter);
			logFileWriter.flush();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void write(Long position, byte[] value) {
		try {
			mmf.put(value, position);
		} catch(Exception e) {
			e.printStackTrace(logFileWriter);
			logFileWriter.flush();
			throw new RuntimeException("Position: " + position + ", Length: " + value.length + ", " + e.getMessage(), e);
		}
	}
	
}
