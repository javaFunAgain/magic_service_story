package it.makes.me.angry.processors;


import it.makes.me.angry.data.GeneratedResult;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.Array;
import javaslang.collection.List;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class OutputFormatterTests {

    @TestFactory
    Iterable<DynamicTest> testOutput() {
        final OutputFormatter theOututFormatter = new OutputFormatter();
        final Array<Tuple2<BigDecimal, String>> testCases = Array.of(
                Tuple.of(new BigDecimal("665.99"), "666"),
                Tuple.of(new BigDecimal("666"), "666"),
                Tuple.of(new BigDecimal("0.00"), "0")
        );
        return testCases.map(
                (Tuple2<BigDecimal, String> testCase) -> dynamicTest("should return empty if file does not exist", () -> {
                    final GeneratedResult sampleResult = new GeneratedResult(testCase._1);
                    assertEquals(testCase._2, theOututFormatter.formatOutput(sampleResult).value);
                }));
    }
}