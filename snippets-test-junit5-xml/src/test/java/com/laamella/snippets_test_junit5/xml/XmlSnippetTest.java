package com.laamella.snippets_test_junit5.xml;

import com.laamella.snippets_test_junit5.core.BasePath;
import com.laamella.snippets_test_junit5.core.SnippetFileFormat;
import com.laamella.snippets_test_junit5.core.SnippetTestFactory;
import generated.Shiporder;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.tempuri.purchaseorderschema.Address;
import org.tempuri.purchaseorderschema.PurchaseOrder;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static com.laamella.snippets_test_junit5.core.TestCaseFilenameFilter.allFiles;
import static java.util.Collections.singletonList;

class XmlSnippetTest {
    private final BasePath basePath = BasePath.fromMavenModuleRoot(XmlSnippetTest.class).inSrcTestResources();
    private final Service service = new Service();

    @TestFactory
    Stream<DynamicTest> test1() throws IOException {
        return new SnippetTestFactory(
                new SnippetFileFormat(">>>", "<<<\n", "\n---\n", "\n^^^\n", "\n---\n", "\nvvv\n"),
                basePath.inSubDirectory("purchase"),
                allFiles(),
                this::runTestCase
        ).stream();
    }

    private List<String> runTestCase(List<String> inputs) throws JAXBException {
        PurchaseOrder order = XmlSnippet.parse(PurchaseOrder.class, inputs.get(0));
        Shiporder shiporder = service.doWork(order);
        return singletonList(XmlSnippet.print(shiporder));
    }

    @Test
    public void printTestRequest() throws JAXBException, DatatypeConfigurationException {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        Address billTo = new Address();
        billTo.setName("Hans Gustaf");
        billTo.setStreet("Hauptstrasse");
        billTo.setCity("Wien");
        purchaseOrder.setBillTo(billTo);
        purchaseOrder.getShipTos().add(billTo);
        LocalDate localDate = LocalDate.parse("1243-01-01");
        XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(localDate.toString());
        purchaseOrder.setOrderDate(date);
        System.out.println(XmlSnippet.print(purchaseOrder));
    }

}