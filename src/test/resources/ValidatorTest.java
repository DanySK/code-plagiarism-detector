package jhaturanga.test.model.validator;

import static jhaturanga.model.user.validators.StringValidatorImpl.ValidationResult.CORRECT;
import static jhaturanga.model.user.validators.StringValidatorImpl.ValidationResult.EMPTY;
import static jhaturanga.model.user.validators.StringValidatorImpl.ValidationResult.TOO_LONG;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jhaturanga.model.user.validators.FunctionConcatenator;
import jhaturanga.model.user.validators.StringValidatorImpl;
import jhaturanga.model.user.validators.StringValidatorImpl.ValidationResult;

/**
 * Test class
 * @see https://github.com/DanySK/Student-Project-OOP20-Andruccioli-Mazzoli-Patriti-Scolari-Jhaturanga/blob/main/src/test/java/jhaturanga/test/model/validator/ValidatorTest.java
 */
class ValidatorTest {

    private FunctionConcatenator<String, ValidationResult> v;

    @BeforeEach
    void initialize() {
        v = new StringValidatorImpl();
    }

    @Test
    void emptyBuild() {
        assertSame(CORRECT, v.create().apply("try1"));
    }

    @Test
    void addOne() {
        v.add(s -> s.length() == 0 ? CORRECT : EMPTY);
        assertSame(CORRECT, v.create().apply(""));
        assertSame(EMPTY, v.create().apply("try2"));
    }

    @Test
    void addTwo() {
        v.add(s -> s.length() != 0 ? CORRECT : EMPTY).add(s -> s.length() < 3 ? CORRECT : TOO_LONG);
        assertSame(CORRECT, v.create().apply("tr")); // OK
        assertSame(EMPTY, v.create().apply(""));
        assertSame(TOO_LONG, v.create().apply("tryError"));
    }

}