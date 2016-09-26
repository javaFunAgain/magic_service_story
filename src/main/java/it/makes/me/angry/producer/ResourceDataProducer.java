package it.makes.me.angry.producer;

import it.makes.me.angry.data.Input;
import it.makes.me.angry.processors.AccessibleDataFormat;
import javaslang.control.Either;

public class ResourceDataProducer implements  DataProducer{
    private final DataCollector dataCollector  = new DataCollector();
    private final DataExtractor dataExtractor  =  new DataExtractor();
    private final DataTransformer dataTransformer  =  new DataTransformer();
    @Override
    public Either<InputProblem, AccessibleDataFormat> extractData(Input input) {
        return dataCollector
                .collectData(input)
                .map(dataExtractor::extractRelevant)
                .flatMap(dataTransformer::transformToAccessibleFormat);
    }
}
