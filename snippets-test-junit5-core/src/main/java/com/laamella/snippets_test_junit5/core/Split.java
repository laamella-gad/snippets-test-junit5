package com.laamella.snippets_test_junit5.core;

import java.util.ArrayList;
import java.util.List;

class Split {
    static List<String> split(String str, String pattern) {
        List<String> result = new ArrayList<>();
        if (pattern == null || pattern.isEmpty()) {
            result.add(str);
            return result;
        }
        boolean done = false;
        int fromIndex = 0;
        int matchIndex;

        while (!done && fromIndex < str.length()) {
            matchIndex = str.indexOf(pattern, fromIndex);
            if (matchIndex >= 0) {
                result.add(str.substring(fromIndex, matchIndex));
                fromIndex = matchIndex + pattern.length();
                if (fromIndex == str.length()) {
                    result.add("");
                }
            } else {
                done = true;
                result.add(str.substring(fromIndex));
            }
        }
        return result;
    }
}
