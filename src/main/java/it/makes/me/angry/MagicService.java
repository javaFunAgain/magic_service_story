package it.makes.me.angry;


import it.makes.me.angry.data.*;
import it.makes.me.angry.hell.GenerationException;
import it.makes.me.angry.processors.*;
import javaslang.collection.List;

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

    public Optional<Output> performComplexCalculations(Input input){
        try {
            RawData rawData = dataCollector.collectData(input);
            List<RelevantData> relevantData = dataExtractor.extractRelevant(rawData);
            List<AccessibleDataFormat> accessibleData = dataTransformer.transformToAccessibleFormat(relevantData);
            List<AccessibleDataFormat> filteredData = dataSelector.filter(accessibleData);
            List<GeneratedResult> generatedData = resultGenerator.generate(filteredData);
            return Optional.of(outputFormatter.formatOutput(generatedData));
        }catch(IOException ex){
            //handling1
        }catch(DataFormatException ex){
            //handling2
        }catch(GenerationException ex){
            //handling3
        }
        return Optional.empty();
    }
}
