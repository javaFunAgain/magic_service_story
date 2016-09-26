package it.makes.me.angry.data;

import java.math.BigDecimal;

public class Output {
    public final String value;

    public Output(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        Output output = (Output) o;
        return value.equals(output.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
