package it.makes.me.angry.processors;

import it.makes.me.angry.CalculationProblem;
import it.makes.me.angry.data.AccessibleDataFormat;
import it.makes.me.angry.data.RelevantData;
import javaslang.collection.List;
import javaslang.control.Either;
import javaslang.control.Option;

import java.util.zip.DataFormatException;

public class DataTransformer {
    public Either<CalculationProblem,AccessibleDataFormat> transformToAccessibleFormat(RelevantData relevantData) {
        return relevantData
                .rows
                .map(dt -> parse(dt))
                .foldLeft( Either.<CalculationProblem,AccessibleDataFormat>right( new AccessibleDataFormat(List.empty())),
                        this::join);

    }

    private Either<CalculationProblem,AccessibleDataFormat.AccessibleDataRow> parse(RelevantData.DataRow dt) {
        try {
            if ( dt.dataColumns.size() < 4) { //TODO: this if looks bad( multiple return)
                return Either.left(CalculationProblem.BAD_DATA_FORMAT);
            }
            final String[] regions = dt.dataColumns.get(1).split("-");
            return Either.right(new AccessibleDataFormat.AccessibleDataRow(
                    regions[0].trim(),
                    regions.length == 2 ? Option.some(regions[1].trim()) : Option.none(),
                    Integer.parseInt(dt.dataColumns.get(2)),
                    Integer.parseInt(dt.dataColumns.get(3))));
        } catch (final NumberFormatException | IndexOutOfBoundsException dfe) {
            return Either.<CalculationProblem,AccessibleDataFormat.AccessibleDataRow>left(CalculationProblem.BAD_DATA_FORMAT);
        }
    }

    private Either<CalculationProblem,AccessibleDataFormat> join(
            final Either<CalculationProblem,AccessibleDataFormat> existingData,
            final Either<CalculationProblem,AccessibleDataFormat.AccessibleDataRow> newRow)  {
            return existingData.flatMap( existingRows -> newRow.map( element -> new AccessibleDataFormat(existingRows.rows.append(element))) );
    }
}
