package it.makes.me.angry.processors;


import it.makes.me.angry.data.AccessibleDataFormat;
import javaslang.collection.Array;
import javaslang.collection.List;
import javaslang.control.Option;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class DataSelectorTests {

    public static final int PARTICIPANTS_BUBU = 14500;
    public static final int PARTICIPANTS_ZUBU = 14512;

    @TestFactory
    Iterable<DynamicTest> dataSelectorTests() {
        final List<AccessibleDataFormat> inputList = List.of(
                new AccessibleDataFormat("BUBU", Option.none(), 8, 90),
                new AccessibleDataFormat("BUBU", Option.some("GMINY MIEJSKIE"), 5, 45),
                new AccessibleDataFormat("BUBU", Option.some("GMINY WIEJSKIE"), 7332, PARTICIPANTS_BUBU),
                new AccessibleDataFormat("ZUBU", Option.some("GMINY WIEJSKIE"), 5232, PARTICIPANTS_ZUBU),
                new AccessibleDataFormat("KUKUU", Option.some("GMINY WIEJSKIE"), 74, 14500)

        );
        final DataSelector theDataSelector = new DataSelector();
        return Array.of(
                dynamicTest("result should have 2 rows",
                        () -> {
                            assertEquals(2, theDataSelector.filter(inputList).size());
                        }),
                dynamicTest("ZUBU row should have 14512  pariticipants",
                        () -> {
                            assertEquals(PARTICIPANTS_ZUBU, theDataSelector.filter(inputList).get(1).participants);
                        }),
                dynamicTest("row should be BUBU",
                        () -> {
                            assertEquals("BUBU", theDataSelector.filter(inputList).get(0).region);
                        })
        );
    }
}