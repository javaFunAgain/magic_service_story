package it.makes.me.angry.processors;

import it.makes.me.angry.data.Input;
import it.makes.me.angry.data.RawData;
import it.makes.me.angry.data.RelevantData;
import javaslang.collection.List;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class DataExtractorTests {
    @TestFactory
    Iterable<DynamicTest> dataExtractorTests() throws IOException {
        final DataExtractor theExtractor = new DataExtractor();
        final RawData inputData = new DataCollector().collectData(new Input("prabuty_poludniowe.csv"));

        return Arrays.asList(
                dynamicTest("result should have 6 rows",
                        () -> {
                            assertEquals(6, theExtractor.extractRelevant(inputData).size());
                        }),
                dynamicTest("result should have 6 rows",
                        () -> {
                            assertEquals("7237", theExtractor.extractRelevant(inputData).get(0).dataColumns.get(2));//TODO: we know too much about RelevantData
                        }),
                dynamicTest("result should have 6 rows",
                        () -> {
                            assertEquals("406778", theExtractor.extractRelevant(inputData).get(5).dataColumns.get(3));
                        }),
                dynamicTest("result should have 6 rows",
                        () -> {
                            assertEquals("PRABUTY POL", theExtractor.extractRelevant(inputData).get(0).dataColumns.get(1));
                        }),
                dynamicTest("result should have 6 rows",
                        () -> {
                            assertEquals("PRABUTY POL - GMINY MIEJSKIE", theExtractor.extractRelevant(inputData).get(1).dataColumns.get(1));
                        })

        );
    }


    private String getValue(final List<RelevantData> data, int row, int cell) {
        return data.get(row).dataColumns.get(cell);
    }


}