package it.makes.me.angry.data;

import javaslang.collection.List;
import javaslang.collection.Traversable;
import javaslang.control.Option;

public class AccessibleDataFormat {
  public final List<AccessibleDataRow> rows;

    public AccessibleDataFormat(List<AccessibleDataRow> rows) {
        this.rows = rows;
    }

    public static final class AccessibleDataRow {
        public final String region;
        public final Option<String> type;
        public final long events;
        public final long participants;

        public AccessibleDataRow(String region, Option<String> type, long events, long participants) {
            this.region = region;
            this.type = type;
            this.events = events;
            this.participants = participants;
        }
    }
}
