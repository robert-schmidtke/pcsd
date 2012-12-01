
package dk.diku.pcsd.assignment1.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "KeyValueBaseImplServiceService", targetNamespace = "http://impl.assignment1.pcsd.diku.dk/", wsdlLocation = "http://localhost:8080/pcsd-assignment1/keyvaluebase?wsdl")
public class KeyValueBaseImplServiceService
    extends Service
{

    private final static URL KEYVALUEBASEIMPLSERVICESERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(dk.diku.pcsd.assignment1.impl.KeyValueBaseImplServiceService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = dk.diku.pcsd.assignment1.impl.KeyValueBaseImplServiceService.class.getResource(".");
            url = new URL(baseUrl, "http://localhost:8080/pcsd-assignment1/keyvaluebase?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://localhost:8080/pcsd-assignment1/keyvaluebase?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        KEYVALUEBASEIMPLSERVICESERVICE_WSDL_LOCATION = url;
    }

    public KeyValueBaseImplServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public KeyValueBaseImplServiceService() {
        super(KEYVALUEBASEIMPLSERVICESERVICE_WSDL_LOCATION, new QName("http://impl.assignment1.pcsd.diku.dk/", "KeyValueBaseImplServiceService"));
    }

    /**
     * 
     * @return
     *     returns KeyValueBaseImplService
     */
    @WebEndpoint(name = "KeyValueBaseImplServicePort")
    public KeyValueBaseImplService getKeyValueBaseImplServicePort() {
        return super.getPort(new QName("http://impl.assignment1.pcsd.diku.dk/", "KeyValueBaseImplServicePort"), KeyValueBaseImplService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns KeyValueBaseImplService
     */
    @WebEndpoint(name = "KeyValueBaseImplServicePort")
    public KeyValueBaseImplService getKeyValueBaseImplServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://impl.assignment1.pcsd.diku.dk/", "KeyValueBaseImplServicePort"), KeyValueBaseImplService.class, features);
    }

}
