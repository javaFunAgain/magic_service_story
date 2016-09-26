package it.makes.me.angry.processors;

import it.makes.me.angry.data.Input;
import it.makes.me.angry.producer.RawData;
import it.makes.me.angry.producer.DataCollector;
import javaslang.collection.Array;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class DataCollectorTests {
    @TestFactory
    Iterable<DynamicTest> dataCollectorTests() {
        final DataCollector theCollector = new DataCollector();
        return Array.of(
                dynamicTest("should return string for simple file",
                        () -> {
                            assertEquals(new RawData("nothing special"),
                                    theCollector.collectData(new Input("raw_file.csv")).get());
                        })
                //TODO: We do not test Exception - because we do not want it at first place!
        );
    }
}