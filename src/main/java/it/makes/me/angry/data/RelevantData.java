package it.makes.me.angry.data;

import javaslang.collection.Array;

public class RelevantData {
    public final Array<String> dataColumns;

    public RelevantData(Array<String> dataColumns) {
        this.dataColumns = dataColumns;
    }

    @Override
    public boolean equals(Object o) {
        RelevantData that = (RelevantData) o;
        return dataColumns.equals(that.dataColumns); //TODO: does not fulfil contract of Object.equals
    }

    @Override
    public int hashCode() {
        return dataColumns.hashCode();
    }




}
