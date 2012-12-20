package dk.diku.pcsd.assignment3.proxy.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import dk.diku.pcsd.assignment3.impl.KeyImpl;
import dk.diku.pcsd.assignment3.impl.TimestampListPair;
import dk.diku.pcsd.assignment3.impl.TimestampPair;
import dk.diku.pcsd.assignment3.impl.ValueListImpl;
import dk.diku.pcsd.assignment3.master.impl.FileNotFoundException_Exception;
import dk.diku.pcsd.assignment3.master.impl.KeyAlreadyPresentException_Exception;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplService;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplServiceService;
import dk.diku.pcsd.assignment3.master.impl.ServiceAlreadyConfiguredException_Exception;
import dk.diku.pcsd.assignment3.master.impl.ServiceAlreadyInitializedException_Exception;
import dk.diku.pcsd.assignment3.master.impl.ServiceInitializingException_Exception;
import dk.diku.pcsd.assignment3.slave.impl.BeginGreaterThanEndException_Exception;
import dk.diku.pcsd.assignment3.slave.impl.IOException_Exception;
import dk.diku.pcsd.assignment3.slave.impl.KeyNotFoundException_Exception;
import dk.diku.pcsd.assignment3.slave.impl.KeyValueBaseSlaveImplService;
import dk.diku.pcsd.assignment3.slave.impl.KeyValueBaseSlaveImplServiceService;
import dk.diku.pcsd.assignment3.slave.impl.ServiceNotInitializedException_Exception;
import dk.diku.pcsd.keyvaluebase.exceptions.BeginGreaterThanEndException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyAlreadyPresentException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyNotFoundException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyConfiguredException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyInitializedException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceInitializingException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceNotInitializedException;
import dk.diku.pcsd.keyvaluebase.interfaces.Configuration;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseProxy;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseReplica;
import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.Predicate;
import dk.diku.pcsd.keyvaluebase.interfaces.TimestampLog;

public class KeyValueBaseProxyImpl implements KeyValueBaseProxy<KeyImpl,ValueListImpl> {
	private KeyValueBaseMasterImplService master;
	private List<KeyValueBaseSlaveImplService> slaves;
	
	private int currentReplica = 0;
	private TimestampLog lastLSN;
	
	public KeyValueBaseProxyImpl(){
		lastLSN = new TimestampLog(0L);
	}
	
	
	@Override
	public void init(String serverFilename)
			throws ServiceAlreadyInitializedException,
			ServiceInitializingException, FileNotFoundException {
		try {
			master.init(serverFilename);
		} catch (FileNotFoundException_Exception e) {
			throw new FileNotFoundException(e.getMessage());
		} catch (ServiceAlreadyInitializedException_Exception e) {
			throw new ServiceAlreadyInitializedException(e.getMessage());
		} catch (ServiceInitializingException_Exception e) {
			throw new ServiceInitializingException(e.getMessage());
		}
		
	}

	@Override
	public ValueListImpl read(KeyImpl k) throws KeyNotFoundException,
			IOException, ServiceNotInitializedException {
		ValueListImpl result=null;
		try {
			Pair p= getReplica().read(k);
			if (p instanceof TimestampPair){
				TimestampPair tsp = (TimestampPair) p;
				if (tsp.getKey().before(lastLSN)){
					return read(k);
				}else{
					lastLSN = tsp.getKey();
					result = tsp.getValue();
				}
			}
		} catch (IOException_Exception e) {
			throw new IOException(e.getMessage());
		} catch (KeyNotFoundException_Exception e) {
			throw new KeyNotFoundException(e.getMessage(), null);
		} catch (ServiceNotInitializedException_Exception e) {
			throw new ServiceNotInitializedException(e.getMessage());
		}
		return result;
	}

	@Override
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException,
			ServiceNotInitializedException {
		try {
			master.insert(k, v);
		} catch (dk.diku.pcsd.assignment3.master.impl.IOException_Exception e) {
			throw new IOException(e.getMessage());
		} catch (KeyAlreadyPresentException_Exception e) {
			throw new KeyAlreadyPresentException(e.getMessage(), null);
		} catch (dk.diku.pcsd.assignment3.master.impl.ServiceNotInitializedException_Exception e) {
			throw new ServiceNotInitializedException(e.getMessage());
		}
		
	}

	@Override
	public void update(KeyImpl k, ValueListImpl newV)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		try {
			master.update(k, newV);
		} catch (dk.diku.pcsd.assignment3.master.impl.IOException_Exception e) {
			throw new IOException(e.getMessage());
		} catch (dk.diku.pcsd.assignment3.master.impl.KeyNotFoundException_Exception e) {
			throw new KeyNotFoundException(e.getMessage(), null);
		} catch (dk.diku.pcsd.assignment3.master.impl.ServiceNotInitializedException_Exception e) {
			throw new ServiceNotInitializedException(e.getMessage());
		}
		
	}

	@Override
	public void delete(KeyImpl k) throws KeyNotFoundException,
			ServiceNotInitializedException {
		try {
			master.delete(k);
		} catch (dk.diku.pcsd.assignment3.master.impl.KeyNotFoundException_Exception e) {
			throw new KeyNotFoundException(e.getMessage(), null);
		} catch (dk.diku.pcsd.assignment3.master.impl.ServiceNotInitializedException_Exception e) {
			throw new ServiceNotInitializedException(e.getMessage());
		}
	}

	@Override
	public List<ValueListImpl> scan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> pred) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		List<ValueListImpl> result=null;
		try {
			Pair p= getReplica().scan(begin, end, pred);
			if (p instanceof TimestampListPair){
				TimestampListPair tsp = (TimestampListPair) p;
				if (tsp.getKey().before(lastLSN)){
					return scan(begin, end, pred);
				}else{
					lastLSN = tsp.getKey();
					result = tsp.getValue();
				}
			}
		} catch (IOException_Exception e) {
			throw new IOException(e.getMessage());
		} catch (ServiceNotInitializedException_Exception e) {
			throw new ServiceNotInitializedException(e.getMessage());
		} catch (BeginGreaterThanEndException_Exception e) {
			throw new BeginGreaterThanEndException(e.getMessage(), null, null);
		}
		return result;
	}

	@Override
	public List<ValueListImpl> atomicScan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> pred) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		List<ValueListImpl> result=null;
		try {
			Pair p= getReplica().atomicScan(begin, end, pred);
			if (p instanceof TimestampListPair){
				TimestampListPair tsp = (TimestampListPair) p;
				if (tsp.getKey().before(lastLSN)){
					return atomicScan(begin, end, pred);
				}else{
					lastLSN = tsp.getKey();
					result = tsp.getValue();
				}
			}
		} catch (IOException_Exception e) {
			throw new IOException(e.getMessage());
		} catch (ServiceNotInitializedException_Exception e) {
			throw new ServiceNotInitializedException(e.getMessage());
		} catch (BeginGreaterThanEndException_Exception e) {
			throw new BeginGreaterThanEndException(e.getMessage(), null, null);
		}
		return result;
	}

	@Override
	public void bulkPut(List<Pair<KeyImpl, ValueListImpl>> mappings)
			throws IOException, ServiceNotInitializedException {
		List list = mappings;
		try {
			master.bulkPut(list);
		} catch (dk.diku.pcsd.assignment3.master.impl.IOException_Exception e) {
			throw new IOException(e.getMessage());
		} catch (dk.diku.pcsd.assignment3.master.impl.ServiceNotInitializedException_Exception e) {
			throw new ServiceNotInitializedException(e.getMessage());
		}
	}

	@Override
	public void config(Configuration conf)
			throws ServiceAlreadyConfiguredException {
		if (slaves != null) {
			throw new ServiceAlreadyConfiguredException();
		}
		
		try{
			URL baseUrl;
			baseUrl = dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplServiceService.class
					.getResource(".");
			URL url = new URL(baseUrl, conf.getMaster());
			QName qn = new QName(
					"http://impl.master.assignment3.pcsd.diku.dk/",
					"KeyValueBaseMasterImplServiceService");
			KeyValueBaseMasterImplServiceService mservice = new KeyValueBaseMasterImplServiceService(
					url, qn);
			
			master = mservice.getKeyValueBaseMasterImplServicePort();

			slaves = new ArrayList<KeyValueBaseSlaveImplService>();

			for (String current : conf.getSlaves()) {
				try {
					baseUrl = dk.diku.pcsd.assignment3.slave.impl.KeyValueBaseSlaveImplServiceService.class
							.getResource(".");
					url = new URL(baseUrl, current);
					qn = new QName(
							"http://impl.slave.assignment3.pcsd.diku.dk/",
							"KeyValueBaseSlaveImplServiceService");
					KeyValueBaseSlaveImplServiceService service = new KeyValueBaseSlaveImplServiceService(
							url, qn);
					slaves.add(service.getKeyValueBaseSlaveImplServicePort());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
			
			try {
				master.config(conf);
			} catch (ServiceAlreadyConfiguredException_Exception e) {
				// Fuck it, bin ich wohl nicht der erste.
			}
		}catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}
	
	private Replica getReplica(){
		if (currentReplica >= slaves.size()){
			currentReplica = (currentReplica + 1) % (slaves.size()+1);
			return new Replica(master);
		}else{
			int index = currentReplica;
			currentReplica = (currentReplica + 1) % (slaves.size()+1);
			return new Replica(slaves.get(index));
		}
	}

}
