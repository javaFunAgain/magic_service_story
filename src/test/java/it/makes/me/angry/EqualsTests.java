package it.makes.me.angry;

import it.makes.me.angry.data.Output;
import it.makes.me.angry.processors.GenerationProblem;
import it.makes.me.angry.producer.InputProblem;
import it.makes.me.angry.producer.RawData;
import it.makes.me.angry.producer.RelevantData;
import javaslang.collection.Array;
import javaslang.collection.List;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class EqualsTests {

    @TestFactory
    Iterable<DynamicTest> equalsTests() {
        return Array.of(
                equalityFor(() -> "a", () -> "b"),
                equalityFor(
                        () -> new RawData("content1"),
                        () -> new RawData("content2")),
                equalityFor(
                        () -> new Output("content1"),
                        () -> new Output("content2")),
                equalityFor(
                        () -> new RelevantData.DataRow(Array.of("e1", "e2")),
                        () -> new RelevantData.DataRow(Array.of("e3", "e4", "e5"))),
                equalityFor(
                        () -> new RelevantData(List.of(new RelevantData.DataRow(Array.of("e1", "e2")))),
                        () -> new RelevantData(List.of(new RelevantData.DataRow(Array.of("e3", "e4", "e5"))))),
                equalityFor(
                        () -> new CalculationProblem(InputProblem.BAD_DATA_FORMAT),
                        () ->new CalculationProblem(GenerationProblem.NOT_ENOUGH_DATA))
            )
                .flatMap(val -> tests(val,
                        this::inequalityTest,
                        this::reflectivityTest,
                        this::symetryTest,
                        this::transitivityTest,
                        this::consistencyTest,
                        this::nullTest));
    }


    private DynamicTest reflectivityTest(final ValuesSuplier supplier) {
        return dynamicTest(createCaseName("Reflectivity", supplier), () -> {
            final Object a = supplier.createAValue();

            assertEquals(a, a);
        });
    }

    private DynamicTest symetryTest(final ValuesSuplier supplier) {
        return dynamicTest(createCaseName("Symmetry", supplier), () -> {
            final Object a1 = supplier.createAValue();
            final Object a2 = supplier.createAValue();
            assertEquals(a1, a2);
            assertEquals(a2, a1);
        });
    }

    private DynamicTest transitivityTest(final ValuesSuplier supplier) {
        return dynamicTest(createCaseName("Transitivity", supplier), () -> {
            final Object a1 = supplier.createAValue();
            final Object a2 = supplier.createAValue();
            final Object a3 = supplier.createAValue();
            assertEquals(a1, a2);
            assertEquals(a2, a3);
            assertEquals(a1, a3);
        });
    }

    private DynamicTest consistencyTest(final ValuesSuplier supplier) {
        return dynamicTest(createCaseName("Consistency", supplier), () -> {
            final Object a1 = supplier.createAValue();
            final Object a2 = supplier.createAValue();
            assertEquals(a1, a2);
            assertEquals(a1, a2);
        });
    }

    private DynamicTest inequalityTest(final ValuesSuplier supplier) {
        return dynamicTest(createCaseName("Inequality", supplier), () -> {
            final Object a = supplier.createAValue();
            final Object b = supplier.createBValue();
            assertNotEquals(a, b);
        });
    }

    private DynamicTest nullTest(final ValuesSuplier supplier) {
        return dynamicTest(createCaseName("Null ref", supplier), () -> {
            final Object a = supplier.createAValue();

            assertNotEquals(a, null);
        });
    }

    private ValuesSuplier equalityFor(final Supplier<Object> a, final Supplier<Object> b) {
        return new ValuesSuplier(a, b);
    }

    private Array<DynamicTest> tests(final ValuesSuplier val, final Function<ValuesSuplier, DynamicTest>... tests) {
        return Array.of(tests).map(func -> func.apply(val));
    }

    private String createCaseName(final String property, ValuesSuplier supplier) {
        return property + " of " + supplier.getName() + ".equals";
    }

    private static final class ValuesSuplier {
        final Supplier<Object> a;
        final Supplier<Object> b;

        public ValuesSuplier(final Supplier<Object> a, final Supplier<Object> b) {
            this.a = a;
            this.b = b;
        }

        public String getName() {
            return this.a.get().getClass().getName();
        }

        public Object createAValue() {
            return this.a.get();
        }

        public Object createBValue() {
            return this.b.get();
        }
    }

}
