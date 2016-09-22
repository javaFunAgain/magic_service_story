package it.makes.me.angry.processors;

import it.makes.me.angry.data.AccessibleDataFormat;
import it.makes.me.angry.data.RelevantData;
import javaslang.collection.List;
import javaslang.control.Option;

import java.util.zip.DataFormatException;

public class DataTransformer {
    public List<AccessibleDataFormat> transformToAccessibleFormat(List<RelevantData> relevantData)
            throws DataFormatException {
        return relevantData.map(dt -> parse(dt));
    }

    private AccessibleDataFormat parse(RelevantData dt) {
        final String[] regions = dt.dataColumns.get(1).split("-");
        return new AccessibleDataFormat(
                regions[0].trim(),
                regions.length == 2 ? Option.some(regions[1].trim()) : Option.none(),
                Integer.parseInt(dt.dataColumns.get(2)),
                Integer.parseInt(dt.dataColumns.get(3)));
    }
}
