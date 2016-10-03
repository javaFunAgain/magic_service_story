package it.makes.me.angry.processors;

import javaslang.control.Either;

public interface DataProcessor {
    Either<GenerationProblem, GeneratedResult> process(AccessibleDataFormat data);
}
