package it.makes.me.angry;


import it.makes.me.angry.data.Input;
import it.makes.me.angry.data.Output;
import it.makes.me.angry.processors.AverageParticipantsProcessor;
import it.makes.me.angry.processors.GenerationProblem;
import it.makes.me.angry.producer.InputProblem;
import it.makes.me.angry.producer.ResourceDataProducer;
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
        final MagicService theTestedService = new MagicService( new ResourceDataProducer(),
                new AverageParticipantsProcessor());
        return Array.of(
                forProblem("bad_file" ,InputProblem.RESOURCE_NOT_FOUND ),
                forProblem("bad_file.csv" ,InputProblem.BAD_DATA_FORMAT ),
                forProblem("too_short.csv" , GenerationProblem.NOT_ENOUGH_DATA),
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
    private Tuple2<Either<CalculationProblem, Output>, String> forProblem(
            final String file, final InputProblem problem ) {
        return Tuple.of(Either.left(new CalculationProblem(problem)), file);
    }
    private Tuple2<Either<CalculationProblem, Output>, String> forProblem(
            final String file, final GenerationProblem problem ) {
        return Tuple.of(Either.left(new CalculationProblem(problem)), file);
    }

}