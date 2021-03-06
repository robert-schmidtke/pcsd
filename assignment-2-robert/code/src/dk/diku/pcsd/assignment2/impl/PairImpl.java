package dk.diku.pcsd.assignment2.impl;

import java.io.Serializable;

import dk.diku.pcsd.keyvaluebase.interfaces.Pair;

public class PairImpl extends Pair<KeyImpl, ValueListImpl> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1049640512497692751L;

	public PairImpl(){
		super();
	}
	
	public PairImpl(KeyImpl k, ValueListImpl v) {
		super(k, v);
	}
	
	@Override
	public KeyImpl getKey() {
		return super.getKey();
	}
	
	public void setKey(KeyImpl k){
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
	public int compareTo(Pair<KeyImpl, ValueListImpl> o) {
		return k.compareTo(o.getKey());
	}

	

}
