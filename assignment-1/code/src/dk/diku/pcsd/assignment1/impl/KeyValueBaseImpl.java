package dk.diku.pcsd.assignment1.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dk.diku.pcsd.keyvaluebase.exceptions.BeginGreaterThanEndException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyAlreadyPresentException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyNotFoundException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyInitializedException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceInitializingException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceNotInitializedException;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBase;
import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.Predicate;

@WebService
public class KeyValueBaseImpl implements KeyValueBase<KeyImpl,ValueListImpl>
{
	
	public KeyValueBaseImpl() {
		// no-arg constructor so the service can be instantiated
	}

	public KeyValueBaseImpl(IndexImpl index) {
		// TODO Auto-generated constructor stub
	}

	@Override
	@WebMethod
	public void init(String serverFilename)
			throws ServiceAlreadyInitializedException,
			ServiceInitializingException, FileNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	@WebMethod
	public ValueListImpl read(KeyImpl k) throws KeyNotFoundException,
			IOException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException,
			ServiceNotInitializedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	@WebMethod
	public void update(KeyImpl k, ValueListImpl newV)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	@WebMethod
	public void delete(KeyImpl k) throws KeyNotFoundException,
			ServiceNotInitializedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	@WebMethod
	public List<ValueListImpl> scan(KeyImpl begin, KeyImpl end, Predicate<ValueListImpl> p)
			throws IOException, BeginGreaterThanEndException,
			ServiceNotInitializedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod
	public List<ValueListImpl> atomicScan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod
	public void bulkPut(List<Pair<KeyImpl, ValueListImpl>> mappings)
			throws IOException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		
	}

}
