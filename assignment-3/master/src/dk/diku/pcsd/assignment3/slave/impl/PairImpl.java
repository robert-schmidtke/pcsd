
package dk.diku.pcsd.assignment3.slave.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for pairImpl complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pairImpl">
 *   &lt;complexContent>
 *     &lt;extension base="{http://impl.slave.assignment3.pcsd.diku.dk/}pair">
 *       &lt;sequence>
 *         &lt;element name="k" type="{http://impl.slave.assignment3.pcsd.diku.dk/}keyImpl" minOccurs="0"/>
 *         &lt;element name="v" type="{http://impl.slave.assignment3.pcsd.diku.dk/}valueListImpl" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pairImpl", propOrder = {
    "k",
    "v"
})
public class PairImpl
    extends Pair
{

    protected KeyImpl k;
    protected ValueListImpl v;

    /**
     * Gets the value of the k property.
     * 
     * @return
     *     possible object is
     *     {@link KeyImpl }
     *     
     */
    public KeyImpl getK() {
        return k;
    }

    /**
     * Sets the value of the k property.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyImpl }
     *     
     */
    public void setK(KeyImpl value) {
        this.k = value;
    }

    /**
     * Gets the value of the v property.
     * 
     * @return
     *     possible object is
     *     {@link ValueListImpl }
     *     
     */
    public ValueListImpl getV() {
        return v;
    }

    /**
     * Sets the value of the v property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueListImpl }
     *     
     */
    public void setV(ValueListImpl value) {
        this.v = value;
    }

}
