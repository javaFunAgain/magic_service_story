package it.makes.me.angry.data;

import javaslang.control.Option;

public class AccessibleDataFormat {
    public final String region;
    public final Option<String> type;
    public final long events;
    public final long participants;

    public AccessibleDataFormat(String region, Option<String> type, long events, long participants) {
        this.region = region;
        this.type = type;
        this.events = events;
        this.participants = participants;
    }
}
