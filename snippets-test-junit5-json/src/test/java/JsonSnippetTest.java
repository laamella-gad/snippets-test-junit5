import com.laamella.snippets_test_junit5.core.BasePath;
import com.laamella.snippets_test_junit5.core.SnippetFileFormat;
import com.laamella.snippets_test_junit5.core.SnippetTestFactory;
import com.laamella.snippets_test_junit5.json.JsonSnippet;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.util.stream.Stream;

import static com.laamella.snippets_test_junit5.core.TestCase.simpleTestCase;
import static com.laamella.snippets_test_junit5.core.TestCaseFilenameFilter.allFiles;

class JsonSnippetTest {
    private final BasePath basePath = BasePath.fromMavenModuleRoot(JsonSnippetTest.class).inSrcTestResources();
    private final Service service = new Service();

    @TestFactory
    Stream<DynamicTest> test1() throws IOException {
        return new SnippetTestFactory(
                new SnippetFileFormat(">>>", "<<<\n", "\n---\n", "\n^^^\n", "\n---\n", "\nvvv\n"),
                basePath.inSubDirectory("testcases"),
                allFiles(),
                simpleTestCase(this::runTestCase)
        ).stream();
    }

    private String runTestCase(String input) {
        SourceClass[] sc = JsonSnippet.parse(SourceClass[].class, input);
        service.sortSourceClasses(sc);
        return JsonSnippet.print(sc);
    }
}