package it.makes.me.angry.processors;

import javaslang.control.Option;

public class DataSelector {
    public FilteredDataFormat filter(AccessibleDataFormat accessibleData) {
        return new FilteredDataFormat(accessibleData
                .rows
                .filter(data -> data.type.eq(Option.some("GMINY WIEJSKIE")))
                .filter( data -> data.events > 5000));
    }
}
