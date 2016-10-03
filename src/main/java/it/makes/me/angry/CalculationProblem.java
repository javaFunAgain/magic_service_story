package it.makes.me.angry;

import it.makes.me.angry.processors.GenerationProblem;
import it.makes.me.angry.producer.InputProblem;
import javaslang.control.Option;

public final class CalculationProblem {
    public final Option<InputProblem> inputProblem;
    public final Option<GenerationProblem> generationProblem;

    private CalculationProblem(Option<InputProblem> inputProblem, Option<GenerationProblem> generationProblem) {
        this.inputProblem = inputProblem;
        this.generationProblem = generationProblem;
    }

    public CalculationProblem(final GenerationProblem  generationProblem) {
        this( Option.none(), Option.some(generationProblem));
    }

    public CalculationProblem(final InputProblem inputProblem) {
        this(Option.some(inputProblem), Option.none());
    }

    @Override
    public boolean equals(Object o) {
        CalculationProblem that = (CalculationProblem) o;
        return Option.of( that).map (
                right ->
                    inputProblem.equals(that.inputProblem)
                            && generationProblem.equals(that.generationProblem)
        ).getOrElse(false);
    }

    @Override
    public int hashCode() {
        int result = inputProblem.hashCode();
        result = 31 * result + generationProblem.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CalculationProblem{" +
                "inputProblem=" + inputProblem +
                ", generationProblem=" + generationProblem +
                '}';
    }
}
