package it.makes.me.angry.output;

import it.makes.me.angry.processors.GeneratedResult;
import it.makes.me.angry.data.Output;

import java.math.BigDecimal;

public class OutputFormatter {
    public Output formatOutput(GeneratedResult generatedData) {
        return new Output(generatedData.value.setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString());
    }
}
