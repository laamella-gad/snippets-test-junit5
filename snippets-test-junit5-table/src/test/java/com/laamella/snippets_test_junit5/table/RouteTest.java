package com.laamella.snippets_test_junit5.table;

import com.laamella.snippets_test_junit5.core.BasePath;
import com.laamella.snippets_test_junit5.core.SnippetFileFormat;
import com.laamella.snippets_test_junit5.core.SnippetTestFactory;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static com.laamella.snippets_test_junit5.core.TestCaseFilenameFilter.allFiles;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

class RouteTest {
    private final BasePath basePath = BasePath.fromMavenModuleRoot(RouteTest.class).inSrcTestResources();
    private final RouteMappingService routeMappingService = new RouteMappingService();

    @TestFactory
    Stream<DynamicTest> test1() throws IOException {
        return new SnippetTestFactory<>(
                new SnippetFileFormat(">>>", "<<<\n", "\n---\n", "\n^^^\n", "\n---\n", "\nvvv\n"),
                basePath.inSubDirectory("route_tables"),
                allFiles(),
                this::makeRoute
        ).stream();
    }

    private List<String> makeRoute(List<String> testCaseParts) {
        // Parse test case (two input parts)
        Table officialRouteTable = Table.parse(testCaseParts.get(0));
        Table ourRouteTable = Table.parse(testCaseParts.get(1));
        List<Stop> officialRoute = officialRouteTable.map(Stop::new, RouteTest::mapRowToStop)
                .collect(toList());
        List<Stop> ourRoute = ourRouteTable.map(Stop::new, RouteTest::mapRowToStop).collect(toList());

        // Call business logic
        List<Stop> finalRoute = routeMappingService.mapRoute(officialRoute, ourRoute);

        // Produce actual
        Table finalRouteTable = Table.fromCollection(finalRoute, Stop.class, "place", "speed", "activity");
        return singletonList(finalRouteTable.toString());
    }

    private static void mapRowToStop(Table.Cell cell, Stop stop) {
        switch (cell.name) {
            case "place":
                stop.place = cell.value().get();
                break;
            case "speed":
                stop.speed = cell.intValue().get();
                break;
            case "activity":
                stop.activity = cell.value().get();
        }
    }
}