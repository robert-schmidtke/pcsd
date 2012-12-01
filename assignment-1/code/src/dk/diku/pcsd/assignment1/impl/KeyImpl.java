package dk.diku.pcsd.assignment1.impl;

import dk.diku.pcsd.keyvaluebase.interfaces.Key;

public class KeyImpl implements Key<KeyImpl>
{
	private String key;
	
	public KeyImpl() {
	}
	
	public KeyImpl(String key){
		this.key=key;
	}

	@Override
	public int compareTo(KeyImpl k) {
		return key.compareTo(k.getKey());
	}
	
	public String toString() {
		return key.toString();
	}

	public String getKey(){
		return key;
	}
	
	public void setKey(String key){
		this.key = key;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof KeyImpl){
			KeyImpl k =(KeyImpl) o;
			return key.equals(k.getKey());
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return key.hashCode();
	}

}
