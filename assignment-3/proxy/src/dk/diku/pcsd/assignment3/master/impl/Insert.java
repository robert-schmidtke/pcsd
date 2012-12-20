
package dk.diku.pcsd.assignment3.master.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import dk.diku.pcsd.assignment3.impl.KeyImpl;
import dk.diku.pcsd.assignment3.impl.ValueListImpl;


/**
 * <p>Java class for insert complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="insert">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://impl.master.assignment3.pcsd.diku.dk/}keyImpl" minOccurs="0"/>
 *         &lt;element name="arg1" type="{http://impl.master.assignment3.pcsd.diku.dk/}valueListImpl" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "insert", propOrder = {
    "arg0",
    "arg1"
})
public class Insert {

    protected KeyImpl arg0;
    protected ValueListImpl arg1;

    /**
     * Gets the value of the arg0 property.
     * 
     * @return
     *     possible object is
     *     {@link KeyImpl }
     *     
     */
    public KeyImpl getArg0() {
        return arg0;
    }

    /**
     * Sets the value of the arg0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyImpl }
     *     
     */
    public void setArg0(KeyImpl value) {
        this.arg0 = value;
    }

    /**
     * Gets the value of the arg1 property.
     * 
     * @return
     *     possible object is
     *     {@link ValueListImpl }
     *     
     */
    public ValueListImpl getArg1() {
        return arg1;
    }

    /**
     * Sets the value of the arg1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueListImpl }
     *     
     */
    public void setArg1(ValueListImpl value) {
        this.arg1 = value;
    }

}
