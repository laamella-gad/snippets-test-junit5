package com.laamella.snippets_test_junit5.xml;

import com.laamella.snippets_test_junit5.core.BasePath;
import com.laamella.snippets_test_junit5.core.SnippetFileFormat;
import com.laamella.snippets_test_junit5.core.SnippetTestFactory;
import generated.Shiporder;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.tempuri.purchaseorderschema.PurchaseOrder;

import java.io.IOException;
import java.util.stream.Stream;

import static com.laamella.snippets_test_junit5.core.TestCase.simpleTestCase;
import static com.laamella.snippets_test_junit5.core.TestCaseFilenameFilter.allFiles;

class XmlSnippetTest {
    private final BasePath basePath = BasePath.fromMavenModuleRoot(XmlSnippetTest.class).inSrcTestResources();
    private final Service service = new Service();

    @TestFactory
    Stream<DynamicTest> test1() throws IOException {
        return new SnippetTestFactory(
                new SnippetFileFormat(">>>", "<<<\n", "\n---\n", "\n^^^\n", "\n---\n", "\nvvv\n"),
                basePath.inSubDirectory("purchase"),
                allFiles(),
                simpleTestCase(this::runTestCase)
        ).stream();
    }

    private String runTestCase(String inputs) throws JAXBException {
        PurchaseOrder order = XmlSnippet.parse(PurchaseOrder.class, inputs);
        Shiporder shiporder = service.doWork(order);
        return XmlSnippet.print(shiporder);
    }
}