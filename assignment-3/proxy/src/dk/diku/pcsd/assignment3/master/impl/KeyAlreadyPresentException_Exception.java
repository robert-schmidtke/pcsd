
package dk.diku.pcsd.assignment3.master.impl;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6
 * Generated source version: 2.1
 * 
 */
@WebFault(name = "KeyAlreadyPresentException", targetNamespace = "http://impl.master.assignment3.pcsd.diku.dk/")
public class KeyAlreadyPresentException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private KeyAlreadyPresentException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public KeyAlreadyPresentException_Exception(String message, KeyAlreadyPresentException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public KeyAlreadyPresentException_Exception(String message, KeyAlreadyPresentException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: dk.diku.pcsd.assignment3.master.impl.KeyAlreadyPresentException
     */
    public KeyAlreadyPresentException getFaultInfo() {
        return faultInfo;
    }

}
