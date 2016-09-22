package it.makes.me.angry.processors;


import it.makes.me.angry.data.AccessibleDataFormat;
import javaslang.collection.List;
import javaslang.control.Option;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class DataSelectorTests {
    @TestFactory
    Iterable<DynamicTest> dataSelectorTests() {
        final List<AccessibleDataFormat> inputList = List.of(
                new AccessibleDataFormat("BUBU", Option.none(), 8, 90),
                new AccessibleDataFormat("BUBU", Option.some("GMINA MIEJSKA"), 5, 45),
                new AccessibleDataFormat("BUBU", Option.some("GMINA WIEJSKA"), 32, 145),
                new AccessibleDataFormat("ZUBU", Option.some("GMINA WIEJSKA"), 32, 145)
        );
        final DataSelector theDataSelector = new DataSelector();
        return Arrays.asList(
                dynamicTest("result should have 2 rows",
                        () -> {
                            assertEquals(2, theDataSelector.filter(inputList).size());
                        }),
                dynamicTest("row should have 45 pareticipants",
                        () -> {
                            assertEquals(145, theDataSelector.filter(inputList).get(1).participants);
                        }),
                dynamicTest("row should be BUBU",
                        () -> {
                            assertEquals("BUBU", theDataSelector.filter(inputList).get(0).region);
                        })
        );
    }
}