package it.makes.me.angry.processors;


import javaslang.collection.Array;
import javaslang.collection.List;
import javaslang.control.Option;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class ResultGeneratorTests {
    @TestFactory
    Iterable<DynamicTest> dataSelectorTests() {
        final FilteredDataFormat inputList =  new FilteredDataFormat(List.of(

                new AccessibleDataFormat.AccessibleDataRow("BUBU", Option.some("GMINY WIEJSKIE"), 5232, 145900),
                new AccessibleDataFormat.AccessibleDataRow("ZUBU", Option.some("GMINY WIEJSKIE"), 6732, 148500))
        );
        final ResultGenerator theGenerator = new ResultGenerator();
        return Array.of(
                dynamicTest("result row schould have  correct average",
                        () -> {
                            assertEquals(new BigDecimal(147200), theGenerator.generate(inputList).get().value);
                        })

        );
    }
}