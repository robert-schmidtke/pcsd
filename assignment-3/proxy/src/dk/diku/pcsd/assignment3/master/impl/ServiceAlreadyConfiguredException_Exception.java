
package dk.diku.pcsd.assignment3.master.impl;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6
 * Generated source version: 2.1
 * 
 */
@WebFault(name = "ServiceAlreadyConfiguredException", targetNamespace = "http://impl.master.assignment3.pcsd.diku.dk/")
public class ServiceAlreadyConfiguredException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private ServiceAlreadyConfiguredException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public ServiceAlreadyConfiguredException_Exception(String message, ServiceAlreadyConfiguredException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public ServiceAlreadyConfiguredException_Exception(String message, ServiceAlreadyConfiguredException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: dk.diku.pcsd.assignment3.master.impl.ServiceAlreadyConfiguredException
     */
    public ServiceAlreadyConfiguredException getFaultInfo() {
        return faultInfo;
    }

}
