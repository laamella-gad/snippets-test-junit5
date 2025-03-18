package com.laamella.snippets_test_junit5.table;

import com.laamella.snippets_test_junit5.core.BasePath;
import com.laamella.snippets_test_junit5.core.SnippetFileFormat;
import com.laamella.snippets_test_junit5.core.SnippetTestFactory;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.laamella.snippets_test_junit5.core.TestCaseFilenameFilter.allFiles;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

class RouteTest {
    private final BasePath basePath = BasePath.fromMavenModuleRoot(RouteTest.class).inSrcTestResources();

    @TestFactory
    Stream<DynamicTest> test1() throws IOException {
        return new SnippetTestFactory<>(
                new SnippetFileFormat(">>>", "<<<\n", "\n---\n", "\n^^^\n", "\n---\n", "\nvvv\n"),
                basePath.inSubDirectory("route_tables"),
                allFiles(),
                this::makeRoute
        ).stream();
    }

    static class Stop {
        String place;
        int speed;
        String activity;

        Stop() {
        }

        Stop(String place, int speed, String activity) {
            this.place = place;
            this.speed = speed;
            this.activity = activity;
        }
    }

    private List<String> makeRoute(List<String> testCaseParts) {
        Table officialRouteTable = Table.parse(testCaseParts.get(0));
        Map<String, Stop> officialRoute = officialRouteTable.map(Stop::new, RouteTest::mapRowToStop)
                .collect(toMap(s -> s.place, s -> s));
        Table ourRouteTable = Table.parse(testCaseParts.get(1));
        List<Stop> ourRoute = ourRouteTable.map(Stop::new, RouteTest::mapRowToStop).collect(toList());

        List<Stop> finalRoute = new ArrayList<>();
        int speed = 0;
        for (Stop stop : ourRoute) {
            String activity = "";
            Stop officialStop = officialRoute.get(stop.place);
            if (officialStop != null) {
                speed = officialStop.speed;
                activity = officialStop.activity;
            }
            finalRoute.add(new Stop(stop.place, speed, activity));
        }

        return singletonList(Table.fromCollection(finalRoute, Stop.class, "place", "speed", "activity").toString());
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