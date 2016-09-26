package it.makes.me.angry.producer;

import it.makes.me.angry.data.Input;
import it.makes.me.angry.processors.AccessibleDataFormat;
import javaslang.control.Either;

public interface DataProducer {
    Either<InputProblem,AccessibleDataFormat> extractData(Input input);
}
