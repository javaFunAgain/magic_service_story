package it.makes.me.angry.producer;

import it.makes.me.angry.data.Input;
import javaslang.control.Either;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class DataCollector {
    public Either<InputProblem, RawData> collectData(final Input input) {
        return input.getPath().flatMap(
                this::readPath
        ).map( RawData::new);
    }

    private Either<InputProblem, String> readPath(final Path path) {
        try {
            return Either.right(new String(Files.readAllBytes(path)));
        } catch (IOException e) {
            return Either.left(InputProblem.UNABLE_TO_LOAD_DATA);
        }
    }
}
