package dk.diku.pcsd.assignment1.impl;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import dk.diku.pcsd.keyvaluebase.interfaces.MemoryMappedFile;
import dk.diku.pcsd.keyvaluebase.interfaces.Store;

public class StoreImpl implements Store {
	
	// size of the file if it does not exist already
	private static final long MMF_INITIAL_SIZE = 1048576; // 1MB
	
	// store it in the system's temporary folder
	private static final String MMF_PATH =
			System.getProperty("java.io.tmpdir").endsWith(File.separator) ?
					(System.getProperty("java.io.tmpdir") + "store.mmf") :
					(System.getProperty("java.io.tmpdir") + File.separator + "store.mmf");
	
	// the actual file
	private final RandomAccessFile mmfRandomAccessFile;
	
	// abstraction over the file
	private final MemoryMappedFile mmf;
	
	// singleton
	private static StoreImpl instance = null;
	
	public static StoreImpl getInstance() {
		if(instance == null)
				instance = new StoreImpl();
		return instance;
	}
		
	private StoreImpl() {
		try {
			File mmfFile = new File(MMF_PATH);
			if(mmfFile.exists())
				mmfRandomAccessFile = new RandomAccessFile(mmfFile, "rw");
			else {
				// create if necessary
				if(mmfFile.createNewFile()) {
					mmfRandomAccessFile = new RandomAccessFile(mmfFile, "rw");
					mmfRandomAccessFile.setLength(MMF_INITIAL_SIZE);
				} else
					throw new RuntimeException("Could not create " + MMF_PATH);
			}
			
			// initialize the memory mapped file with either the old file or the newly created one
			mmf = new MemoryMappedFile(mmfRandomAccessFile.getChannel(), FileChannel.MapMode.READ_WRITE, 0, mmfRandomAccessFile.length());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		// close the raf on garbace collection
		mmfRandomAccessFile.close();
		super.finalize();		
	}

	@Override
	public byte[] read(Long position, int length) {
		try {
			byte[] dst = new byte[length];
			mmf.get(dst, position);
			return dst;
		} catch(Exception e) {
			throw new RuntimeException("Position: " + position + ", Length: " + length + ", " + e.getMessage(), e);
		}
	}

	@Override
	public void write(Long position, byte[] value) {
		try {
			mmf.put(value, position);
		} catch(Exception e) {
			throw new RuntimeException("Position: " + position + ", Length: " + value.length + ", " + e.getMessage(), e);
		}
	}
	
}
