
package dk.diku.pcsd.assignment3.slave.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for timestampListPair complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="timestampListPair">
 *   &lt;complexContent>
 *     &lt;extension base="{http://impl.slave.assignment3.pcsd.diku.dk/}pair">
 *       &lt;sequence>
 *         &lt;element name="k" type="{http://impl.slave.assignment3.pcsd.diku.dk/}timestampLog" minOccurs="0"/>
 *         &lt;element name="v" type="{http://impl.slave.assignment3.pcsd.diku.dk/}valueListImpl" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "timestampListPair", propOrder = {
    "k",
    "v"
})
public class TimestampListPair
    extends Pair
{

    protected TimestampLog k;
    protected List<ValueListImpl> v;

    /**
     * Gets the value of the k property.
     * 
     * @return
     *     possible object is
     *     {@link TimestampLog }
     *     
     */
    public TimestampLog getK() {
        return k;
    }

    /**
     * Sets the value of the k property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimestampLog }
     *     
     */
    public void setK(TimestampLog value) {
        this.k = value;
    }

    /**
     * Gets the value of the v property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the v property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getV().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValueListImpl }
     * 
     * 
     */
    public List<ValueListImpl> getV() {
        if (v == null) {
            v = new ArrayList<ValueListImpl>();
        }
        return this.v;
    }

}
