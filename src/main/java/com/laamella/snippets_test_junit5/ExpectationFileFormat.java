package com.laamella.snippets_test_junit5;

import static java.util.Objects.requireNonNull;

/**
 * The various strings used to organize an expectation file.
 */
public class ExpectationFileFormat {
    final String expectationsStartString;
    final String separatorBetweenExpectations;
    final String expectationsEndString;

    /**
     * @param expectationsStartString      the separator that comes at the start of the file. Don't forget to include \n if you want new lines.
     * @param separatorBetweenExpectations the separator that goes between the expectations in the file. They come in the same order as the actualGenerators. Don't forget to include \n if you want new lines.
     * @param expectationsEndString        the last piece of text in the file. Don't forget to include \n if you want new lines.
     */
    public ExpectationFileFormat(String expectationsStartString, String separatorBetweenExpectations, String expectationsEndString) {
        this.expectationsStartString = requireNonNull(expectationsStartString);
        this.separatorBetweenExpectations = requireNonNull(separatorBetweenExpectations);
        this.expectationsEndString = requireNonNull(expectationsEndString);
    }
}
