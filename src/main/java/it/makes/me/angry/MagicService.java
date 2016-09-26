package it.makes.me.angry;


import it.makes.me.angry.data.*;
import it.makes.me.angry.output.OutputFormatter;
import it.makes.me.angry.processors.*;
import it.makes.me.angry.producer.*;
import javaslang.control.Either;

public class MagicService {
    private final DataProducer dataProducer;

    private final DataProcessor dataProcessor;

    private final OutputFormatter outputFormatter = new OutputFormatter();

    public MagicService(DataProducer dataProducer, DataProcessor dataProcessor) {
        this.dataProducer = dataProducer;
        this.dataProcessor = dataProcessor;
    }

    public Either<CalculationProblem, Output> performComplexCalculations(Input input){
            final Either<InputProblem, AccessibleDataFormat> inputData = dataProducer.extractData(input);
            final Either<CalculationProblem, GeneratedResult> generatedData =
                    inputData
                            .mapLeft( prob ->new CalculationProblem(prob))
                            .flatMap( data -> dataProcessor.process(data).mapLeft(
                                    prob -> new CalculationProblem(prob)
                            ));

            return generatedData.map(outputFormatter::formatOutput);
    }
}
