package dk.diku.pcsd.assignment3.impl;

import java.io.Serializable;

import dk.diku.pcsd.keyvaluebase.interfaces.Pair;

public class PairImpl<K extends Comparable<K>,V> extends Pair<K, V> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1049640512497692751L;

	public PairImpl(){
		super();
	}
	
	public PairImpl(K k, V v) {
		super(k, v);
	}
	
	@Override
	public K getKey() {
		return super.getKey();
	}
	
	public void setKey(K k){
		this.k=k;
	}
	
	public void setValue(V v){
		this.v=v;
	}
	
	@Override
	public V getValue() {
		return super.getValue();
	}

	@Override
	public int compareTo(Pair<K, V> o) {
		return k.compareTo(o.getKey());
	}

	

}
