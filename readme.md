Snippets tests for JUnit5
=====================

What is it for?
---------------------
There is a common anti-pattern in (unit) testing where we write a test that makes a large print-out of the test result -
like a `.toString`, and `assertEquals` that to a print-out we have stored on disk. These tests are *great* because we
can any little change to the test result!
These tests are *terrible* because every little change to the test result will break the test, even if it was totally
irrelevant to the test case.

This library offers a well-polished library for creating these kinds of tests.

It is called *snippet* test because using small test cases (snippets) will hopefully avoid the bad side of this pattern!

In practice, you will set up a few unit test that work on a few directories, then fill those directories with test cases
one by one. The library helps you manage the expectations for those test cases.

A practical example is [antlr-snippets-test-junit5](https://github.com/laamella-gad/antlr-snippets-test-junit5) which
lets you use pieces of code as the inputs, parses it with the grammar you are developing, lets you transform the grammar
as you like, then outputs various representations (token list, parse tree) that you can use as expectations.

What's the design?
--------------------------
The library works with JUnit5 to generate test cases for each file in a directory structure. These files go through a
little life cycle:

1. you create one, and put the test input in them (some text)
2. the tests are run, and for every file with only input and no expectation, the expectation is created from the actual
   test result. The test will fail to show that you need to check if that expectation is really what you want.
3. the tests are run again, and every file should now have input and an expectation. These are compared, and if they
   don't match the test fails.
4. the code changes and the expectation in a file is no longer correct. The test fails. You can correct the expectation,
   or let the library regenerate it. Go to 3 :-)

A variation where the input of the test is written in Java code, and not in the test file itself, is in development.

How to set it up
----------------------
Add a dependency on the [latest version](https://search.maven.org/search?q=a:snippets-test-junit5).

You have to decide on a file format. You need to pick separators that won't get in the way of the input and
expectations. These are documented
in [SnippetFileFormat.java](src/main/java/com/laamella/snippets_test_junit5/SnippetFileFormat.java). And example
is [SnippetTestFactoryTest.java](https://github.com/laamella-gad/snippets-test-junit5/blob/master/src/test/java/com/laamella/snippets_test_junit5/SnippetTestFactoryTest.java)

Next you can create a unit test. JUnit5's `@TestFactory` approach is used to dynamically generate unit tests for your
test files. An example
is [SnippetTestFactoryTest](src/test/java/com/laamella/snippets_test_junit5/SnippetTestFactoryTest.java) and the
documentation is in [SnippetTestFactory](src/main/java/com/laamella/snippets_test_junit5/SnippetTestFactory.java). That
particular setup creates two unit tests, they can be seen [here](src/test/resources/test1)

If you want to regenerate the expectations for a single test, delete them from the file and run the test.

If you want to regenerate the expectations for *all* files, look for the `.stream()` call on the factory, and replace it
with `.regenerateAllExpectations()` for one run.

Credits
=======
Based on, and contains parts of Gerald Rosenberg's [SnippetsTest](https://github.com/grosenberg/SnippetsTest).