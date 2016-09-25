package it.makes.me.angry.processors;

import it.makes.me.angry.data.AccessibleDataFormat;
import it.makes.me.angry.data.GeneratedResult;
import it.makes.me.angry.hell.GenerationException;
import javaslang.collection.List;

import java.math.BigDecimal;

public class ResultGenerator {
    public List<GeneratedResult> generate(List<AccessibleDataFormat> filteredData)
            throws GenerationException {
        final BigDecimal sum = new BigDecimal(filteredData.map(data -> data.participants).reduce(
                (a,b)-> a+ b).intValue());
        if ( filteredData.size() > 0 ) {
            return List.of(new GeneratedResult(sum.divide( new BigDecimal(filteredData.size()))));
        } else {
            throw new GenerationException();
        }
    }
}
