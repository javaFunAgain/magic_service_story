package it.makes.me.angry.processors;

import javaslang.control.Either;

import java.math.BigDecimal;

public class ResultGenerator {
    public Either<GenerationProblem, GeneratedResult> generate(FilteredDataFormat filteredData) {
        final BigDecimal sum = new BigDecimal(filteredData.rows.map(data -> data.participants).foldLeft( 0L,
                (a, b) -> a + b).intValue());
        if (filteredData.rows.size() > 0) {
            return Either.right(new GeneratedResult(sum.divide(new BigDecimal(filteredData.rows.size()))));
        } else {
            return Either.left(GenerationProblem.NOT_ENOUGH_DATA);
        }
    }
}
