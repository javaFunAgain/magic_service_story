package it.makes.me.angry.data;

import javaslang.control.Option;


public class Output {
    public final String value;

    public Output(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        return Option.of(o)
                .map( right-> value.equals(((Output)right).value))
                .getOrElse(false);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
