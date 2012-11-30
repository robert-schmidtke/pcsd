package dk.diku.pcsd.assignment1.impl;

import dk.diku.pcsd.keyvaluebase.interfaces.Value;

@SuppressWarnings("serial")
public class ValueImpl implements Value
{
	Object v;

	public String toString() {
		return v.toString();
	}
	
	public ValueImpl(Object o){
		this.v=o;
	}
	
	public Object getValue(){
		return v;
	}
}

