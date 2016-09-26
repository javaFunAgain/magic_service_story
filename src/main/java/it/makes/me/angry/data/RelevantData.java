package it.makes.me.angry.data;

import javaslang.collection.Array;
import javaslang.collection.List;


public final class RelevantData {
    public final List<DataRow> rows;

    public RelevantData(final List<DataRow> rows) {
        this.rows = rows;
    }

    @Override
    public boolean equals(Object o) {
        RelevantData that = (RelevantData) o;
        return rows.equals(that.rows); //TODO: does not fulfil contract of Object.equals
    }

    @Override
    public int hashCode() {
        return rows.hashCode();
    }


    public static final class DataRow {
        public final Array<String> dataColumns;

        public DataRow(Array<String> dataColumns) {
            this.dataColumns = dataColumns;
        }

        @Override
        public boolean equals(Object dataRow) {
            return dataColumns.equals(((DataRow)dataRow).dataColumns);
        }

        @Override
        public int hashCode() {
            return dataColumns.hashCode();
        }
    }
}
