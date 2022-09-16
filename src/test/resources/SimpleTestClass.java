package org.example.javaparser.samples.Test;

import java.util.Random;
import java.util.stream.IntStream;

/**
 * This is a testing class.
 */
public class SimpleTestClass { // test class
    public static void main(String[] args) {
        var k = (new Random()).nextInt(1500);
        IntStream.range(0, 1000).forEach( i -> {
            k++;
            if (!k%5) {
                System.out.println(k + "5 is 0!");
            }
        });
    }
}
