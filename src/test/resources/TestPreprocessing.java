package org.example;

import java.util.List;
import java.util.Objects;
import org.example.javaparser.*;

class TestPreprocessing {

    private String test;

    public TestPreprocessing(String test) {
        this.test = test
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