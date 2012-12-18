package dk.diku.pcsd.keyvaluebase.interfaces;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is intended to keep the WSDL
 * file locations of all the internal RPC
 * services. Those locations are used by the
 * proxies and the master to find all the
 * replicas.
 */
public class Configuration {
	private String master = "http://localhost:12345/keyValueBase/master?wsdl";
	private List<String> slaves = new ArrayList<String>();
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public List<String> getSlaves() {
		return slaves;
	}
	public void setSlaves(List<String> slaves) {
		this.slaves = slaves;
	}
	
	
}
