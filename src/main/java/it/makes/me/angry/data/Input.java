package it.makes.me.angry.data;

import it.makes.me.angry.CalculationProblem;
import javaslang.control.Either;
import javaslang.control.Option;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Input {
    public final String resourceName;

    public Input(String resourceName) {
        this.resourceName = resourceName;
    }

    public Either<CalculationProblem, Path> getPath() {
        return getURI().map( Paths::get);
    }

    private Either<CalculationProblem, URI> getURI() {
        final Option<URL> resource = Option.of(getClass().getResource("/" + this.resourceName));
        return resource
                .map(this::resourceToURI)
                .getOrElse(Either.left(CalculationProblem.RESOURCE_NOT_FOUND));
    }

    private Either<CalculationProblem, URI> resourceToURI(final URL resource) {
        try {
            final URI uri = resource.toURI();
            return Either.right(uri);
        } catch (URISyntaxException e) {
            return Either.left(it.makes.me.angry.CalculationProblem.WRONG_RESOURCE_NAME);
        }
    }
}
