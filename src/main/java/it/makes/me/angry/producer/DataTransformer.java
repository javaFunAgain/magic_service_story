package it.makes.me.angry.producer;

import it.makes.me.angry.CalculationProblem;
import it.makes.me.angry.processors.AccessibleDataFormat;
import it.makes.me.angry.processors.GenerationProblem;
import it.makes.me.angry.producer.InputProblem;
import it.makes.me.angry.producer.RelevantData;
import javaslang.collection.List;
import javaslang.control.Either;
import javaslang.control.Option;

public class DataTransformer {
    public Either<InputProblem,AccessibleDataFormat> transformToAccessibleFormat(RelevantData relevantData) {
        return relevantData
                .rows
                .map(dt -> parse(dt))
                .foldLeft( Either.right( new AccessibleDataFormat(List.empty())),
                        this::join);

    }

    private Either<InputProblem,AccessibleDataFormat.AccessibleDataRow> parse(RelevantData.DataRow dt) {
        try {
            if ( dt.dataColumns.size() < 4) { //TODO: this if looks bad( multiple return)
                return Either.left(InputProblem.BAD_DATA_FORMAT);
            }
            final String[] regions = dt.dataColumns.get(1).split("-");
            return Either.right(new AccessibleDataFormat.AccessibleDataRow(
                    regions[0].trim(),
                    regions.length == 2 ? Option.some(regions[1].trim()) : Option.none(),
                    Integer.parseInt(dt.dataColumns.get(2)),
                    Integer.parseInt(dt.dataColumns.get(3))));
        } catch (final NumberFormatException | IndexOutOfBoundsException dfe) {
            return Either.left(InputProblem.BAD_DATA_FORMAT);
        }
    }

    private Either<InputProblem,AccessibleDataFormat> join(
            final Either<InputProblem,AccessibleDataFormat> existingData,
            final Either<InputProblem,AccessibleDataFormat.AccessibleDataRow> newRow)  {
            return existingData.flatMap( existingRows -> newRow.map( element -> new AccessibleDataFormat(existingRows.rows.append(element))) );
    }
}
