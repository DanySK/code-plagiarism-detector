package org.example;

import java.util.Objects;
import org.example.javaparser.*;

class TestAnalyzer {

    private final String test;

    public TestPreprocessing(String test) {
        this.test = test;
    }

    public String getTest() {
        return this.test;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestPreprocessing that = (TestPreprocessing) o;
        return Objects.equals(test, that.test);
    }

    @Override
    public int hashCode() {
        return Objects.hash(test);
    }
}