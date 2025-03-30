package com.laamella.snippets_test_junit5.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSnippet {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static <T> T parse(Class<T> rootClass, String json) {
        return gson.fromJson(json, rootClass);
    }

    public static <T> String print(T root) {
        return gson.toJson(root);
    }
}
