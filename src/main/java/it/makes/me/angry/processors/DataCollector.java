package it.makes.me.angry.processors;

import it.makes.me.angry.CalculationProblem;
import it.makes.me.angry.data.Input;
import it.makes.me.angry.data.RawData;
import javaslang.control.Either;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class DataCollector {
    public Either<CalculationProblem, RawData> collectData(final Input input) {
        return input.getPath().flatMap(
                this::readPath
        ).map( RawData::new);
    }

    private Either<CalculationProblem, String> readPath(final Path path) {
        try {
            return Either.right(new String(Files.readAllBytes(path)));
        } catch (IOException e) {
            return Either.left(CalculationProblem.UNABLE_TO_LOAD_DATA);
        }
    }
}
