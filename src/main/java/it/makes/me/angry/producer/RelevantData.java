package it.makes.me.angry.producer;

import javaslang.collection.Array;
import javaslang.collection.List;
import javaslang.control.Option;


public final class RelevantData {
    public final List<DataRow> rows;

    public RelevantData(final List<DataRow> rows) {
        this.rows = rows;
    }

    @Override
    public boolean equals(Object o) {
        return Option.of(o)
                .map( right-> rows.equals(((RelevantData)right).rows))
                .getOrElse(false);
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
            return Option.of(dataRow)
                    .map( right-> dataColumns.equals(((DataRow)right).dataColumns))
                    .getOrElse(false);
        }

        @Override
        public int hashCode() {
            return dataColumns.hashCode();
        }
    }
}
