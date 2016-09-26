package it.makes.me.angry;


import it.makes.me.angry.data.Input;
import it.makes.me.angry.data.Output;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.Array;
import javaslang.control.Either;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class MagicServiceTests {


    @TestFactory
    Iterable<DynamicTest> magicServiceBasics() {
        final MagicService theTestedService = new MagicService();
        return Array.of(
                forInputProblem("bad_file" ,CalculationProblem.RESOURCE_NOT_FOUND ),
                forInputProblem("bad_file.csv" ,CalculationProblem.BAD_DATA_FORMAT ),
                forInputProblem("too_short.csv" ,CalculationProblem.NOT_ENOUGH_DATA),
                forInputOutput("prabuty_poludniowe.csv" ,"373603")
                )
                .map( testCase -> dynamicTest("Case :" + testCase, () -> {
                    assertEquals(testCase._1,
                            theTestedService.performComplexCalculations(
                                    new Input(testCase._2)));
                }) );
    }

    private Tuple2<Either<CalculationProblem, Output>, String> forInputOutput(
            final String file, final String output ) {
        return Tuple.of(Either.right(new Output(output)), file );
    }
    private Tuple2<Either<CalculationProblem, Output>, String> forInputProblem(
            final String file, final CalculationProblem problem ) {
        return Tuple.of(Either.left(problem), file);
    }

}