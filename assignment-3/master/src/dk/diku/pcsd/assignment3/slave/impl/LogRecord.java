
package dk.diku.pcsd.assignment3.slave.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for logRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="logRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="className" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="methodName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numberParam" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="LSN" type="{http://impl.slave.assignment3.pcsd.diku.dk/}timestampLog" minOccurs="0"/>
 *         &lt;element name="params" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "logRecord", propOrder = {
    "className",
    "methodName",
    "numberParam",
    "lsn",
    "params"
})
public class LogRecord {

    protected String className;
    protected String methodName;
    protected int numberParam;
    @XmlElement(name = "LSN")
    protected TimestampLog lsn;
    protected List<Object> params;

    /**
     * Gets the value of the className property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets the value of the className property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassName(String value) {
        this.className = value;
    }

    /**
     * Gets the value of the methodName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Sets the value of the methodName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethodName(String value) {
        this.methodName = value;
    }

    /**
     * Gets the value of the numberParam property.
     * 
     */
    public int getNumberParam() {
        return numberParam;
    }

    /**
     * Sets the value of the numberParam property.
     * 
     */
    public void setNumberParam(int value) {
        this.numberParam = value;
    }

    /**
     * Gets the value of the lsn property.
     * 
     * @return
     *     possible object is
     *     {@link TimestampLog }
     *     
     */
    public TimestampLog getLSN() {
        return lsn;
    }

    /**
     * Sets the value of the lsn property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimestampLog }
     *     
     */
    public void setLSN(TimestampLog value) {
        this.lsn = value;
    }

    /**
     * Gets the value of the params property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the params property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParams().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getParams() {
        if (params == null) {
            params = new ArrayList<Object>();
        }
        return this.params;
    }

}
