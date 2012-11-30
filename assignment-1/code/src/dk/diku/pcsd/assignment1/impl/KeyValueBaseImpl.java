package dk.diku.pcsd.assignment1.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import dk.diku.pcsd.keyvaluebase.exceptions.BeginGreaterThanEndException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyAlreadyPresentException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyNotFoundException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyInitializedException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceInitializingException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceNotInitializedException;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBase;
import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.Predicate;

public class KeyValueBaseImpl implements KeyValueBase<KeyImpl,ValueListImpl>
{
	IndexImpl ind; 
	boolean initialized = false;

	public KeyValueBaseImpl() {
		ind = IndexImpl.getInstance();
	}
	
	public KeyValueBaseImpl(IndexImpl index) {
		ind = index;
	}

	@Override
	public void init(String serverFilename)
			throws ServiceAlreadyInitializedException,
			ServiceInitializingException, FileNotFoundException {
		// TODO: implement
		initialized = true;
	}

	@Override
	public ValueListImpl read(KeyImpl k) throws KeyNotFoundException,
			IOException, ServiceNotInitializedException {
		if (!initialized)
			throw new ServiceNotInitializedException();
		return ind.get(k);
	}

	@Override
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException,
			ServiceNotInitializedException {
		if (!initialized)
			throw new ServiceNotInitializedException();
		ind.insert(k, v);
	}

	@Override
	public void update(KeyImpl k, ValueListImpl newV)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		if (!initialized)
			throw new ServiceNotInitializedException();
		ind.update(k, newV);		
	}

	@Override
	public void delete(KeyImpl k) throws KeyNotFoundException,
			ServiceNotInitializedException {
		if (!initialized)
			throw new ServiceNotInitializedException();
		ind.remove(k);
	}

	@Override
	public List<ValueListImpl> scan(KeyImpl begin, KeyImpl end, Predicate<ValueListImpl> p)
			throws IOException, BeginGreaterThanEndException,
			ServiceNotInitializedException {
		if (!initialized)
			throw new ServiceNotInitializedException();
		List<ValueListImpl> allValues = ind.scan(begin, end);
		for (Iterator<ValueListImpl> i = allValues.iterator(); i.hasNext(); ){
			ValueListImpl current = i.next();
			if (!p.evaluate(current))
				i.remove();
		}
		return allValues;
	}

	@Override
	public List<ValueListImpl> atomicScan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bulkPut(List<Pair<KeyImpl, ValueListImpl>> mappings)
			throws IOException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		
	}

}
