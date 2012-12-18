package dk.diku.pcsd.assignment3.impl;

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
