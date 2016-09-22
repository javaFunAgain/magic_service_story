package it.makes.me.angry.processors;

import it.makes.me.angry.data.AccessibleDataFormat;
import javaslang.collection.List;
import javaslang.control.Option;

public class DataSelector {
    public List<AccessibleDataFormat> filter(List<AccessibleDataFormat> accessibleData) {
        return accessibleData.filter(data -> data.type.eq(Option.some("GMINA WIEJSKA")));
    }
}
