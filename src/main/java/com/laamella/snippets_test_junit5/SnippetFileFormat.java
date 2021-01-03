package com.laamella.snippets_test_junit5;

import static java.util.Objects.requireNonNull;

/**
 * The various strings used to organize a snippet.
 */
public class SnippetFileFormat {
    final String descriptionStartString;
    final String descriptionEndString;
    final String separatorAfterTestCase;
    final String separatorBetweenExpectations;
    final String expectationsEndString;

    /**
     * @param descriptionStartString       the string that indicates the start of the optional description. This has to be the very first piece of text to appear in the snippet, or it is assumed that there is no description.
     * @param descriptionEndString         the string that indicates the end of the optional description. You probably want to end it with a \n
     * @param separatorAfterTestCase       the separator that comes right after the test case. If it is not found, it is assumed that no expectation is in the snippet (you probably just created a new snippet here), and the "actual" is written to the snippet file. Don't forget to include \n if you want new lines.
     * @param separatorBetweenExpectations the separator that goes between the expectations in the file. They come in the same order as the actualGenerators. Don't forget to include \n if you want new lines.
     * @param expectationsEndString        the last piece of text in the file. Don't forget to include \n if you want new lines.
     */
    public SnippetFileFormat(String descriptionStartString, String descriptionEndString, String separatorAfterTestCase, String separatorBetweenExpectations, String expectationsEndString) {
        this.descriptionStartString = requireNonNull(descriptionStartString);
        this.descriptionEndString = requireNonNull(descriptionEndString);
        this.separatorAfterTestCase = requireNonNull(separatorAfterTestCase);
        this.separatorBetweenExpectations = requireNonNull(separatorBetweenExpectations);
        this.expectationsEndString = requireNonNull(expectationsEndString);
    }
}
