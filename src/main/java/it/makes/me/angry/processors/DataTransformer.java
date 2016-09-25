package it.makes.me.angry.processors;

import it.makes.me.angry.CalculationProblem;
import it.makes.me.angry.data.AccessibleDataFormat;
import it.makes.me.angry.data.RelevantData;
import javaslang.collection.List;
import javaslang.control.Either;
import javaslang.control.Option;

import java.util.zip.DataFormatException;

public class DataTransformer {
    public Either<CalculationProblem,List<AccessibleDataFormat>> transformToAccessibleFormat(List<RelevantData> relevantData) {
        return relevantData
                .map(dt -> parse(dt))
                .foldLeft( Either.<CalculationProblem,List<AccessibleDataFormat>>right( List.empty()),
                        ( Either<CalculationProblem,List<AccessibleDataFormat>> a,
                          Either<CalculationProblem,AccessibleDataFormat> b) -> a.flatMap( list->b.map( el -> list.append(el) )));
    }

    private Either<CalculationProblem,AccessibleDataFormat> parse(RelevantData dt) {
        try {
            if ( dt.dataColumns.size() < 4) { //TODO: this if looks bad( multiple return)
                return Either.left(CalculationProblem.BAD_DATA_FORMAT);
            }
            final String[] regions = dt.dataColumns.get(1).split("-");
            return Either.right(new AccessibleDataFormat(
                    regions[0].trim(),
                    regions.length == 2 ? Option.some(regions[1].trim()) : Option.none(),
                    Integer.parseInt(dt.dataColumns.get(2)),
                    Integer.parseInt(dt.dataColumns.get(3))));
        } catch (final NumberFormatException | IndexOutOfBoundsException dfe) {
            return Either.<CalculationProblem,AccessibleDataFormat>left(CalculationProblem.BAD_DATA_FORMAT);
        }

    }
}
