package it.makes.me.angry.producer;

import javaslang.collection.Array;
import javaslang.collection.List;

public class DataExtractor {
    public RelevantData extractRelevant(RawData rawData) {
        final String[] rows = rawData.fileContent.split("\n");
        return new RelevantData(List.of(rows).drop(1).map( this::splitRow));
    }

    private  RelevantData.DataRow splitRow(final String row) {
        return new RelevantData.DataRow(Array.of (row.split(";")).map(s->s.replaceAll("\"(.*)\"","$1")) );
    }
}
