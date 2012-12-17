package dk.diku.pcsd.keyvaluebase.interfaces;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import dk.diku.pcsd.assignment3.impl.PairImpl;

@XmlSeeAlso({PairImpl.class})
public abstract class Pair<K, V> implements Comparable<Pair<K,V>>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6120085642616703347L;
	@XmlElement
	protected K k;
	@XmlElement
	protected V v;
	
	public Pair()
	{
	}
	
	public Pair (K k, V v){
		this.k = k;
		this.v = v;
	}
	
	public K getKey(){
		return k;
	}
	
	public V getValue(){
		return v;
	}
	
}
