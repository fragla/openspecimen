//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2011.06.16 at 07:41:18 GMT
//


package edu.wustl.cider.jaxb.domain;


/**
 * Java content class for SiteType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/G:/specimen_new.xsd line 67)
 * <p>
 * <pre>
 * &lt;complexType name="SiteType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Coordinator" type="{}CoordinatorType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 */
public interface SiteType {


    /**
     * Gets the value of the coordinator property.
     *
     * @return
     *     possible object is
     *     {@link generated.CoordinatorType}
     */
    CoordinatorType getCoordinator();

    /**
     * Sets the value of the coordinator property.
     *
     * @param value
     *     allowed object is
     *     {@link generated.CoordinatorType}
     */
    void setCoordinator(CoordinatorType value);

    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getName();

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setName(java.lang.String value);

}