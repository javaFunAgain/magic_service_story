package it.makes.me.angry;


import it.makes.me.angry.data.*;
import it.makes.me.angry.hell.GenerationException;
import it.makes.me.angry.processors.*;
import javaslang.collection.List;
import javaslang.control.Either;

import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;

public class MagicService {
    private final DataCollector dataCollector  = new DataCollector();
    private final DataExtractor dataExtractor  =  new DataExtractor();
    private final DataTransformer dataTransformer = new DataTransformer();
    private final DataSelector dataSelector = new DataSelector();
    private final ResultGenerator resultGenerator  = new ResultGenerator();
    private final OutputFormatter outputFormatter = new OutputFormatter();

    public Either<CalculationProblem, Output> performComplexCalculations(Input input){
            final Either<CalculationProblem, RawData> rawData = dataCollector.collectData(input);
            final Either<CalculationProblem, List<RelevantData>> relevantData =
                    rawData.map( raw->dataExtractor.extractRelevant(raw));
            Either<CalculationProblem,List<AccessibleDataFormat>> accessibleData =
                     relevantData.flatMap( dataTransformer::transformToAccessibleFormat);
            Either<CalculationProblem,List<AccessibleDataFormat>> filteredData =
                    accessibleData.map( dataSelector::filter);
            Either<CalculationProblem, List<GeneratedResult>> generatedData =
                    filteredData.flatMap( data  -> resultGenerator.generate(data));
            return generatedData.map(outputFormatter::formatOutput);
    }
}
