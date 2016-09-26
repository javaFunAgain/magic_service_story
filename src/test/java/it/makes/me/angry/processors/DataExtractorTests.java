package it.makes.me.angry.processors;


import it.makes.me.angry.data.Input;
import it.makes.me.angry.producer.RawData;
import it.makes.me.angry.producer.DataCollector;
import it.makes.me.angry.producer.DataExtractor;
import javaslang.collection.Array;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class DataExtractorTests {
    @TestFactory
    Iterable<DynamicTest> dataExtractorTests() throws IOException {
        final DataExtractor theExtractor = new DataExtractor();
        final  RawData inputData =
                new DataCollector().collectData(new Input("prabuty_poludniowe.csv")).get();

        return Array.of(
                dynamicTest("result should have 6 rows",
                        () -> {
                            assertEquals(6, theExtractor.extractRelevant(inputData).rows.size());
                        }),
                dynamicTest("result should have 6 rows",
                        () -> {
                            assertEquals("7237", theExtractor.extractRelevant(inputData).rows.get(0).dataColumns.get(2));//TODO: we know too much about RelevantData
                        }),
                dynamicTest("result should have 6 rows",
                        () -> {
                            assertEquals("406778", theExtractor.extractRelevant(inputData).rows.get(5).dataColumns.get(3));
                        }),
                dynamicTest("result should have 6 rows",
                        () -> {
                            assertEquals("PRABUTY POL", theExtractor.extractRelevant(inputData).rows.get(0).dataColumns.get(1));
                        }),
                dynamicTest("result should have 6 rows",
                        () -> {
                            assertEquals("PRABUTY POL - GMINY MIEJSKIE", theExtractor.extractRelevant(inputData).rows.get(1).dataColumns.get(1));
                        })

        );
    }

}