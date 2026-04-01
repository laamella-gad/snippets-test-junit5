# Snippets tests for JUnit5

## What is it for?
There is a common pattern in (unit) testing where we write a test that makes a large print-out of the test result -
like a `.toString`, and `assertEquals` that to a print-out we have stored on disk. These tests are *great* because we
can see any little change to the test result!

The library helps you run the tests and manage the expectations for the test cases.

## Approaches

Two main approaches are supported:

### Input and expected output in a file

A dynamic test that reads input from files, runs your test case for each of them, then compares the output with the
   expectations in the files.

A practical example is [antlr-snippets-test-junit5](snippets-test-junit5-antlr/readme.md).
This uses the library to test an Antlr grammar.
It lets you use pieces of code as the inputs,
parses it with the grammar you are developing,
lets you transform the grammar as you like,
then outputs various representations (token list, parse tree) that you can use as expectations.

### Normal unit tests, but the expected output is in a file

An "assertEquals"-like assert that compares your test output with the contents of a file.
An example
is [SnippetTestExtensionTest](snippets-test-junit5-core/src/test/java/com/laamella/snippets_test_junit5/core/SnippetTestExtensionTest.java).

## What's the design for the dynamic tests?
The library works with JUnit5 to generate test cases for each file in a directory structure. These files go through a
little life cycle:

1. you create one, and put the test input in them (some text)
2. the tests are run, and for every file with only input and no expectation, the expectation is created from the actual
   test result. The test will fail to show that you need to check if that expectation is really what you want.
3. the tests are run again, and every file should now have input and an expectation. These are compared, and if they
   don't match the test fails.
4. the code changes and the expectation in a file is no longer correct. The test fails. You can correct the expectation,
   or let the library regenerate it. Go to 3 :-)

## How to set it up

Add a dependency on the [latest version](https://central.sonatype.com/artifact/com.laamella/snippets-test-junit5).

You have to decide on a file format. You need to pick separators that won't get in the way of the input and
expectations. These are documented
in [SnippetFileFormat.java](snippets-test-junit5-core/src/main/java/com/laamella/snippets_test_junit5/core/SnippetFileFormat.java).

Next you can create a unit test. JUnit5's `@TestFactory` approach is used to dynamically generate unit tests for your
test files. An example
is [SnippetTestFactoryTest](snippets-test-junit5-core/src/test/java/com/laamella/snippets_test_junit5/core/SnippetTestFactoryTest.java)
and the
documentation is
in [SnippetTestFactory](snippets-test-junit5-core/src/main/java/com/laamella/snippets_test_junit5/core/SnippetTestFactory.java).
That
particular setup creates two unit tests, they can be seen [here](snippets-test-junit5-core/src/test/resources/test1)

If you want to regenerate the expectations for a single test, delete the expectations from the file and run the test.

If you want to regenerate the expectations for *all* files, run your tests with the system property
`REGENERATE_EXPECTATIONS` set (the value doesn't matter):

```
mvn test -DREGENERATE_EXPECTATIONS
```

The tests will still fail on that run, so you can review what changed before committing.

## Support for specific cases

## Credits
Based on, and contains parts of Gerald Rosenberg's [SnippetsTest](https://github.com/grosenberg/SnippetsTest).
