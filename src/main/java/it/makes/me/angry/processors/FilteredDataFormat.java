package it.makes.me.angry.processors;

import javaslang.collection.List;

public class FilteredDataFormat {
    public final List<AccessibleDataFormat.AccessibleDataRow> rows;

    public FilteredDataFormat(List<AccessibleDataFormat.AccessibleDataRow> rows) {
        this.rows = rows;
    }
}
