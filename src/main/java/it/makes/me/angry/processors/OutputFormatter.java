package it.makes.me.angry.processors;

import it.makes.me.angry.data.GeneratedResult;
import it.makes.me.angry.data.Output;
import javaslang.collection.List;

import java.math.BigDecimal;

public class OutputFormatter {
    public Output formatOutput(GeneratedResult generatedData) {
        return new Output(generatedData.value.setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString());
    }
}
