package it.makes.me.angry;


import it.makes.me.angry.data.Input;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class MagicServiceTests {


    @TestFactory
    Iterable<DynamicTest> magicServiceBasics() {
        final MagicService theTestedService = new MagicService();
        return Arrays.asList(
                dynamicTest("should return empty if file does not exist",
                        () -> {
                            assertFalse(
                                    theTestedService.performComplexCalculations(
                                            new Input("bad_file")).isPresent());
                        })
        );
    }
}