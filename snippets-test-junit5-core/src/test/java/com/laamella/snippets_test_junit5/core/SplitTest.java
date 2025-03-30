package com.laamella.snippets_test_junit5.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SplitTest {
    @Test
    void test1() {
        List<String> split = Split.split("", "\n--\n");
        assertThat(split).isEmpty();
    }

    @Test
    void test2() {
        List<String> split = Split.split("\n--\nceoiaejoiea\n--\njfoieajoiaje\n--\n", "\n--\n");
        assertThat(split).containsExactly("", "ceoiaejoiea", "jfoieajoiaje", "");
    }

    @Test
    void test4() {
        List<String> split = Split.split("x\n--\nceoiaejoiea\n--\njfoieajoiaje\n--\nx", "\n--\n");
        assertThat(split).containsExactly("x", "ceoiaejoiea", "jfoieajoiaje", "x");
    }

    @Test
    void test3() {
        List<String> split = Split.split("aoiejoiaj\n--\nceoiaejoiea\n--\njfoieajoiaje\n--\n", "\n--\n");
        assertThat(split).containsExactly("aoiejoiaj", "ceoiaejoiea", "jfoieajoiaje", "");
    }

    @Test
    void test5() {
        List<String> split = Split.split("abc", "abc");
        assertThat(split).containsExactly("", "");
    }

    @Test
    void test6() {
        List<String> split = Split.split("abc", "def");
        assertThat(split).containsExactly("abc");
    }

    @Test
    void test7() {
        List<String> split = Split.split("abc", "");
        assertThat(split).containsExactly("abc");
    }
}