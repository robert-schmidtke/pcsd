package dk.diku.pcsd.keyvaluebase.interfaces;

/**
 * This class is intended to keep the WSDL
 * file locations of all the internal RPC
 * services. Those locations are used by the
 * proxies and the master to find all the
 * replicas.
 */
public class Configuration {
	public String master = "http://localhost:12345/keyValueBase/master?wsdl";
	public String[] slaves = {	"http://localhost:12345/keyValueBase/slave1?wsdl",
								"http://localhost:12345/keyValueBase/slave2?wsdl" };
}
