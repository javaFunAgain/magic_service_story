package it.makes.me.angry.producer;


import javaslang.control.Option;

public class RawData {
    public final String fileContent;

    public RawData(final String fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public boolean equals(final Object o) {
        return Option.of((RawData)o)
                .map( right-> fileContent.equals(right.fileContent))
                .getOrElse(false);
    }

    @Override
    public int hashCode() {
        return fileContent.hashCode();
    }
}
