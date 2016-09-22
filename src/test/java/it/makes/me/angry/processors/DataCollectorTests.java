package it.makes.me.angry.processors;

import it.makes.me.angry.MagicService;
import it.makes.me.angry.data.Input;
import it.makes.me.angry.data.RawData;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class DataCollectorTests {
    @TestFactory
    Iterable<DynamicTest> dataCollectorTests() {
        final DataCollector theCollector = new DataCollector();
        return Arrays.asList(
                dynamicTest("should return string for simple file",
                        () -> {
                            assertEquals(new RawData("nothing special"),
                                    theCollector.collectData(new Input("raw_file.csv")));
                        })
                //We do not test Exception - because we do not want it at first place!

        );
    }
}