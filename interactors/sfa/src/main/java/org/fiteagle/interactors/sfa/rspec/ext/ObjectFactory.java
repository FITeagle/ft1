//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.28 at 02:10:13 PM CET 
//


package org.fiteagle.interactors.sfa.rspec.ext;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.fiteagle.interactors.sfa.rspec package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Property_QNAME_FITEAGLE = new QName("http://fiteagle.org/rspec/ext/1", "property");
    private final static QName _Method_QNAME = new QName("http://fiteagle.org/rspec/ext/1", "method");
    private final static QName _Resource_QNAME = new QName("http://fiteagle.org/rspec/ext/1", "resource");
    private final static QName _Parameter_QNAME = new QName("http://fiteagle.org/rspec/ext/1", "parameter");
    
    
    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.fiteagle.interactors.sfa.rspec
     * 
     */
    public ObjectFactory() {
    }


    
    //FITeagle specific resource description!!!!!!!!!!!!!!!!!!!!
    /**
     * Create an instance of {@link Parameter }
     * 
     */
    public Parameter createParameter() {
        return new Parameter();
    }

    /**
     * Create an instance of {@link Resource }
     * 
     */
    public Resource createResource() {
        return new Resource();
    }

    /**
     * Create an instance of {@link Method }
     * 
     */
    public Method createMethod() {
        return new Method();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Property }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://fiteagle.org/rspec/ext/1", name = "property")
    public JAXBElement<Property> createProperty(Property value) {
        return new JAXBElement<Property>(_Property_QNAME_FITEAGLE, Property.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Method }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://fiteagle.org/rspec/ext/1", name = "method")
    public JAXBElement<Method> createMethod(Method value) {
        return new JAXBElement<Method>(_Method_QNAME, Method.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Resource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://fiteagle.org/rspec/ext/1", name = "resource")
    public JAXBElement<Resource> createResource(Resource value) {
        return new JAXBElement<Resource>(_Resource_QNAME, Resource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Parameter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://fiteagle.org/rspec/ext/1", name = "parameter")
    public JAXBElement<Parameter> createParameter(Parameter value) {
        return new JAXBElement<Parameter>(_Parameter_QNAME, Parameter.class, null, value);
    }
    

    
    

}
