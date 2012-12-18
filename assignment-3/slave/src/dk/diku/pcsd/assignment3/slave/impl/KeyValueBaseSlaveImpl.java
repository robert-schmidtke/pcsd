package dk.diku.pcsd.assignment3.slave.impl;

import dk.diku.pcsd.assignment3.impl.KeyImpl;
import dk.diku.pcsd.assignment3.impl.KeyValueBaseReplicaImpl;
import dk.diku.pcsd.assignment3.impl.ValueListImpl;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseSlave;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;

public class KeyValueBaseSlaveImpl extends KeyValueBaseReplicaImpl implements KeyValueBaseSlave<KeyImpl, ValueListImpl> {

	
	
	@Override
	public void logApply(LogRecord record) {
		try{
			record.invoke(this);
		}catch(Exception e){
			// TODO do nothing ??
		}
	}

}
