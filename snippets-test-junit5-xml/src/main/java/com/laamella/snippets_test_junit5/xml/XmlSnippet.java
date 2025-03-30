package com.laamella.snippets_test_junit5.xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.StringReader;
import java.io.StringWriter;

public class XmlSnippet {
    @SuppressWarnings("unchecked")
    public static <T> T parse(Class<T> rootClass, String xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(rootClass);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new StringReader(xml));
    }

    public static <T> String print(T root) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(root.getClass());
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        mar.marshal(root, sw);
        return sw.toString().trim();
    }
}
