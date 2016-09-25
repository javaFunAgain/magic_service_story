package it.makes.me.angry.processors;

import it.makes.me.angry.data.RelevantData;
import javaslang.collection.Array;
import javaslang.collection.List;
import javaslang.control.Option;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class DataTransformerTests {

    @TestFactory
    Iterable<DynamicTest> dataExtractorTests() throws IOException {

        final List<RelevantData> relevantData = List.of(
                createSampleRelevantData("BUBU", Option.none(), 7, 77),
                createSampleRelevantData("BUBU", Option.some("GMINY WIEJSKIE"), 8, 87)
        );
        final DataTransformer theDataTransformer = new DataTransformer();

        return Array.of(
                dynamicTest(" should return BUBU region",
                        () -> {
                            assertEquals("BUBU", theDataTransformer.transformToAccessibleFormat(relevantData).get().get(0).region);
                        }),
                dynamicTest(" should return 7 events",
                        () -> {
                            assertEquals(7, theDataTransformer.transformToAccessibleFormat(relevantData).get().get(0).events);
                        }),
                dynamicTest(" should return 87 participants",
                        () -> {
                            assertEquals(87, theDataTransformer.transformToAccessibleFormat(relevantData).get().get(1).participants);
                        })
        );
    }


    final RelevantData createSampleRelevantData(String region, Option<String> typ, int events, int participants) {
        return new RelevantData(Array.of("0000", typ.map(v -> region + " - " + v).getOrElse(region), String.valueOf(events), String.valueOf(participants)));
    }


}