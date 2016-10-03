package it.makes.me.angry.processors;

import javaslang.control.Either;

public class AverageParticipantsProcessor implements DataProcessor {

    private final DataSelector dataSelector = new DataSelector();
    private final ResultGenerator resultGenerator  = new ResultGenerator();

    @Override
    public Either<GenerationProblem, GeneratedResult> process(final AccessibleDataFormat data) {
        return resultGenerator.generate(dataSelector.filter(data));
    }
}
