package it.makes.me.angry.processors;

import it.makes.me.angry.CalculationProblem;
import it.makes.me.angry.data.AccessibleDataFormat;
import it.makes.me.angry.data.GeneratedResult;
import javaslang.collection.List;
import javaslang.control.Either;

import java.math.BigDecimal;

public class ResultGenerator {
    public Either<CalculationProblem, GeneratedResult> generate(AccessibleDataFormat filteredData) {
        final BigDecimal sum = new BigDecimal(filteredData.rows.map(data -> data.participants).foldLeft( 0L,
                (a, b) -> a + b).intValue());
        if (filteredData.rows.size() > 0) {
            return Either.right(new GeneratedResult(sum.divide(new BigDecimal(filteredData.rows.size()))));
        } else {
            return Either.left(CalculationProblem.NOT_ENOUGH_DATA);
        }
    }
}
