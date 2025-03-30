package com.laamella.snippets_test_junit5.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Sample business logic.
 */
class RouteMappingService {
    List<Stop> mapRoute(List<Stop> officialRoute, List<Stop> ourRoute) {
        Map<String, Stop> officialPlaces = officialRoute.stream().collect(toMap(s -> s.place, s -> s));
        List<Stop> finalRoute = new ArrayList<>();
        int speed = 0;
        for (Stop stop : ourRoute) {
            String activity = "";
            Stop officialStop = officialPlaces.get(stop.place);
            if (officialStop != null) {
                speed = officialStop.speed;
                activity = officialStop.activity;
            }
            finalRoute.add(new Stop(stop.place, speed, activity));
        }
        return finalRoute;
    }
}
