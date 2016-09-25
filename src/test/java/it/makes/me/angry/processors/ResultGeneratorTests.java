package it.makes.me.angry.processors;


import it.makes.me.angry.data.AccessibleDataFormat;
import javaslang.collection.List;
import javaslang.control.Option;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class ResultGeneratorTests {
    @TestFactory
    Iterable<DynamicTest> dataSelectorTests() {
        final List<AccessibleDataFormat> inputList = List.of(

                new AccessibleDataFormat("BUBU", Option.some("GMINA WIEJSKA"), 5232, 145900),
                new AccessibleDataFormat("ZUBU", Option.some("GMINA WIEJSKA"), 6732, 148500)
        );
        final ResultGenerator theGenerator = new ResultGenerator();
        return Arrays.asList(
                dynamicTest("result should have 1 row",
                        () -> {
                            assertEquals(1, theGenerator.generate(inputList).size());
                        }),
                dynamicTest("result row schould have  correct average",
                        () -> {
                            assertEquals(new BigDecimal(147200), theGenerator.generate(inputList).get(0).value);
                        })

        );
    }
}