package it.makes.me.angry.processors;

import it.makes.me.angry.data.AccessibleDataFormat;
import javaslang.collection.List;
import javaslang.control.Option;

public class DataSelector {
    public AccessibleDataFormat filter(AccessibleDataFormat accessibleData) {
        return new AccessibleDataFormat(accessibleData
                .rows
                .filter(data -> data.type.eq(Option.some("GMINY WIEJSKIE")))
                .filter( data -> data.events > 5000));
    }
}
