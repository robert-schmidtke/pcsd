
package dk.diku.pcsd.assignment3.slave.impl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import dk.diku.pcsd.assignment3.impl.KeyImpl;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;
import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.Predicate;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "KeyValueBaseSlaveImplService", targetNamespace = "http://impl.slave.assignment3.pcsd.diku.dk/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface KeyValueBaseSlaveImplService {


    /**
     * 
     * @param arg0
     * @throws ServiceAlreadyInitializedException_Exception
     * @throws ServiceInitializingException_Exception
     * @throws FileNotFoundException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "init", targetNamespace = "http://impl.slave.assignment3.pcsd.diku.dk/", className = "dk.diku.pcsd.assignment3.slave.impl.Init")
    @ResponseWrapper(localName = "initResponse", targetNamespace = "http://impl.slave.assignment3.pcsd.diku.dk/", className = "dk.diku.pcsd.assignment3.slave.impl.InitResponse")
    public void init(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0)
        throws FileNotFoundException_Exception, ServiceAlreadyInitializedException_Exception, ServiceInitializingException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns dk.diku.pcsd.keyvaluebase.interfaces.Pair
     * @throws KeyNotFoundException_Exception
     * @throws ServiceNotInitializedException_Exception
     * @throws IOException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "read", targetNamespace = "http://impl.slave.assignment3.pcsd.diku.dk/", className = "dk.diku.pcsd.assignment3.slave.impl.Read")
    @ResponseWrapper(localName = "readResponse", targetNamespace = "http://impl.slave.assignment3.pcsd.diku.dk/", className = "dk.diku.pcsd.assignment3.slave.impl.ReadResponse")
    public Pair read(
        @WebParam(name = "arg0", targetNamespace = "")
        KeyImpl arg0)
        throws IOException_Exception, KeyNotFoundException_Exception, ServiceNotInitializedException_Exception
    ;

    /**
     * 
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns dk.diku.pcsd.keyvaluebase.interfaces.Pair
     * @throws BeginGreaterThanEndException_Exception
     * @throws ServiceNotInitializedException_Exception
     * @throws IOException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "scan", targetNamespace = "http://impl.slave.assignment3.pcsd.diku.dk/", className = "dk.diku.pcsd.assignment3.slave.impl.Scan")
    @ResponseWrapper(localName = "scanResponse", targetNamespace = "http://impl.slave.assignment3.pcsd.diku.dk/", className = "dk.diku.pcsd.assignment3.slave.impl.ScanResponse")
    public Pair scan(
        @WebParam(name = "arg0", targetNamespace = "")
        KeyImpl arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        KeyImpl arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        Predicate arg2)
        throws BeginGreaterThanEndException_Exception, IOException_Exception, ServiceNotInitializedException_Exception
    ;

    /**
     * 
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns dk.diku.pcsd.keyvaluebase.interfaces.Pair
     * @throws BeginGreaterThanEndException_Exception
     * @throws ServiceNotInitializedException_Exception
     * @throws IOException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "atomicScan", targetNamespace = "http://impl.slave.assignment3.pcsd.diku.dk/", className = "dk.diku.pcsd.assignment3.slave.impl.AtomicScan")
    @ResponseWrapper(localName = "atomicScanResponse", targetNamespace = "http://impl.slave.assignment3.pcsd.diku.dk/", className = "dk.diku.pcsd.assignment3.slave.impl.AtomicScanResponse")
    public Pair atomicScan(
        @WebParam(name = "arg0", targetNamespace = "")
        KeyImpl arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        KeyImpl arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        Predicate arg2)
        throws BeginGreaterThanEndException_Exception, IOException_Exception, ServiceNotInitializedException_Exception
    ;

    /**
     * 
     * @param arg0
     */
    @WebMethod
    @RequestWrapper(localName = "logApply", targetNamespace = "http://impl.slave.assignment3.pcsd.diku.dk/", className = "dk.diku.pcsd.assignment3.slave.impl.LogApply")
    @ResponseWrapper(localName = "logApplyResponse", targetNamespace = "http://impl.slave.assignment3.pcsd.diku.dk/", className = "dk.diku.pcsd.assignment3.slave.impl.LogApplyResponse")
    public void logApply(
        @WebParam(name = "arg0", targetNamespace = "")
        LogRecord arg0);

}
