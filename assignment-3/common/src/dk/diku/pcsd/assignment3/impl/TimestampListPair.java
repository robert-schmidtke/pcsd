package dk.diku.pcsd.assignment3.impl;

import java.util.List;

import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.TimestampLog;

public class TimestampListPair extends Pair<TimestampLog, List<ValueListImpl>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3385885792546510360L;

	public TimestampListPair(TimestampLog t, List<ValueListImpl> l){
		this.k = t;
		this.v = l;
	}
	
	public TimestampListPair(){
		super();
	}
	
	@Override
	public TimestampLog getKey() {
		return super.getKey();
	}
	
	public void setKey(TimestampLog k){
		this.k=k;
	}
	
	public void setValue(List<ValueListImpl> v){
		this.v=v;
	}
	
	@Override
	public List<ValueListImpl> getValue() {
		return super.getValue();
	}
	
	@Override
	public int compareTo(Pair<TimestampLog, List<ValueListImpl>> arg0) {
		return k.compareTo(arg0.getKey());
	}

}
