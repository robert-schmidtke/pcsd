
package dk.diku.pcsd.assignment1.impl;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6
 * Generated source version: 2.1
 * 
 */
@WebFault(name = "KeyNotFoundException", targetNamespace = "http://impl.assignment1.pcsd.diku.dk/")
public class KeyNotFoundException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private KeyNotFoundException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public KeyNotFoundException_Exception(String message, KeyNotFoundException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public KeyNotFoundException_Exception(String message, KeyNotFoundException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: dk.diku.pcsd.assignment1.impl.KeyNotFoundException
     */
    public KeyNotFoundException getFaultInfo() {
        return faultInfo;
    }

}
