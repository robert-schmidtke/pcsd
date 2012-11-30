package dk.diku.pcsd.assignment1.impl;

import dk.diku.pcsd.keyvaluebase.interfaces.Key;

@SuppressWarnings("rawtypes")
public class KeyImpl implements Key<KeyImpl>
{
	
	Comparable v; 

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(KeyImpl k) {
		return v.compareTo(k.getKey());
	}
	
	public String toString() {
		return v.toString();
	}
	
	public KeyImpl(Comparable v){
		this.v=v;
	}
	
	public Comparable getKey(){
		return v;
	}

}
