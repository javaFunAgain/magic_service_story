package it.makes.me.angry.processors;

import it.makes.me.angry.data.GeneratedResult;
import it.makes.me.angry.data.Output;
import javaslang.collection.List;

import java.math.BigDecimal;

public class OutputFormatter {
    public Output formatOutput(List<GeneratedResult> generatedData) {
        return new Output(generatedData
                .map( data->data.value.setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString())
                .reduce((a,b) -> a+b));
    }
}
