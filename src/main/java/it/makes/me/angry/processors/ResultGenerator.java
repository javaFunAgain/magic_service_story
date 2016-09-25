package it.makes.me.angry.processors;

import it.makes.me.angry.CalculationProblem;
import it.makes.me.angry.data.AccessibleDataFormat;
import it.makes.me.angry.data.GeneratedResult;
import javaslang.collection.List;
import javaslang.control.Either;

import java.math.BigDecimal;

public class ResultGenerator {
    public Either<CalculationProblem, List<GeneratedResult>> generate(List<AccessibleDataFormat> filteredData) {
        final BigDecimal sum = new BigDecimal(filteredData.map(data -> data.participants).reduce(
                (a, b) -> a + b).intValue());
        if (filteredData.size() > 0) {
            return Either.right(List.of(new GeneratedResult(sum.divide(new BigDecimal(filteredData.size())))));
        } else {
            return Either.left(CalculationProblem.NOT_ENOUGH_DATA);
        }
    }
}
