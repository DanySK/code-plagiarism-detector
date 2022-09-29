package org.example.javaparser.samples.Test;

/**
 * This is a plagiarized testing class.
 */
public class SimpleTestClass { // test class
    public static void main(String[] args) {
        Integer k = (new java.util.Random()).nextInt(1000);
        for (int i = 0; i < 100; i++) {
            k++;
            if (k % 5 == 0) {
                System.out.println(k + "5 is 0!");
            }
        }
    }
}

