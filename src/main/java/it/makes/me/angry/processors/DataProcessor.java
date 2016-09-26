package it.makes.me.angry.processors;

import it.makes.me.angry.producer.RelevantData;
import javaslang.control.Either;

public interface DataProcessor {
    Either<GenerationProblem, GeneratedResult> process(AccessibleDataFormat data);
}
