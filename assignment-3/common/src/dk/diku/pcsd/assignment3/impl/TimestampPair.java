package dk.diku.pcsd.assignment3.impl;

import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.TimestampLog;

public class TimestampPair extends Pair<TimestampLog, ValueListImpl> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2297814542470880237L;

	public TimestampPair(TimestampLog t, ValueListImpl v) {
		this.k = t;
		this.v = v;
	}
	
	public TimestampPair(){
		super();
	}
	
	@Override
	public TimestampLog getKey() {
		return super.getKey();
	}
	
	public void setKey(TimestampLog k){
		this.k=k;
	}
	
	public void setValue(ValueListImpl v){
		this.v=v;
	}
	
	@Override
	public ValueListImpl getValue() {
		return super.getValue();
	}

	@Override
	public int compareTo(Pair<TimestampLog, ValueListImpl> o) {
		return k.compareTo(o.getKey());
	}

}
