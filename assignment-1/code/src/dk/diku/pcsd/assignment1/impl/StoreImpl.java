package dk.diku.pcsd.assignment1.impl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import dk.diku.pcsd.keyvaluebase.interfaces.MemoryMappedFile;
import dk.diku.pcsd.keyvaluebase.interfaces.Store;

public class StoreImpl implements Store
{
	
	private static StoreImpl instance = null;
	
	private final PrintWriter logFileWriter;
		
	private final MemoryMappedFile mmf;
	private final RandomAccessFile mmfBase;
		
	public static StoreImpl getInstance() {
		if(instance == null)
				instance = new StoreImpl();
		return instance;
	}
	
	private StoreImpl() {
		try {
			logFileWriter = new PrintWriter("store-" + new SimpleDateFormat("yyyy-MM-DD_HH-mm-ss-SSS").format(new Date()) + ".log");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Error creating log file for StoreImpl");
		}
		
		try {
			mmfBase = new RandomAccessFile("store.mmf", "rw");
			mmf = new MemoryMappedFile(
					mmfBase.getChannel(),
					FileChannel.MapMode.READ_WRITE,
					0, 24 * 1073741824); // 24GB
		} catch (Exception e) {
			e.printStackTrace(logFileWriter);
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
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void write(Long position, byte[] value) {
		try {
			mmf.put(value, position);
		} catch(Exception e) {
			e.printStackTrace(logFileWriter);
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
}
