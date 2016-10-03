# Historia Magicznego Serwisu

# Czyścimy

W poprzednich odcinkach udało się poprawić kod wyjściowego zadania i 
osiągnęlismy coś co jest w miarę testowalne i przejrzyste.
Zostało jednak trochę *TODO* w kodzie, które można ogarnąć.

## Equalsy
Na pierwszy ogień nie pójda metody equals.
Najlepszy przykład to klasa **RawData**
```
public class RawData {
    public final String fileContent;

    public RawData(final String fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public boolean equals(Object o) {
        return fileContent.equals(((RawData)o).fileContent);//TODO - does not conform to Object.equals  - null problem
    }

    @Override
    public int hashCode() {
        return fileContent.hashCode();
    }
}
```
Wszystko fajnie ale **equals** nie spełnia kontraktu.
Który to wygląda tak:
> The equals method implements an equivalence relation on non-null object references:
  
>-  It is reflexive: for any non-null reference value x, x.equals(x) should return true.
>- It is symmetric: for any non-null reference values x and y, x.equals(y) should return true if and only if y.equals(x) returns true.
>-  It is transitive: for any non-null reference values x, y, and z, if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true.
>-  It is consistent: for any non-null reference values x and y, multiple invocations of x.equals(y) consistently return true or consistently return false, provided no information used in equals comparisons on the objects is modified.
>-  For any non-null reference value x, x.equals(null) should return false.

Jak się dobrze przyjrzeć to ostatni punkt nie jest spełniony.
Przy okazji, uważam, że ten kontrakt jest głupi - jeśli ktoś jest takim szaleńcem, żeby używać **null** to
**NullPointerException** należy mu się jak psu zupa! To są jednak zaszłości  historyczne i fakt.
A skoro taki kontrakt ustalono - to już niestety trzeba go przestrzegać - bo można się spoktać z dziwnymi efektami 
( a nuż - widelec jakaś dziwna mapa, czy lista tego założenia używa).

## Napiszmy więc Test
Przy pomocy **JUnit5** można zmontować całkiem zgrabne testy na wszystkie  i jeszcze troche przypadków.
```
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
```
Jak widać jednym testem możemy obrobić wszystkie klasy, w których nieopatrznie
zaimplementowaliśmy **equals**.
Ktoś może powiedzieć: 
> ale przeciez to zwykłe testy parametryczne - to już było np. w **JUnit 4**

Jest jednak  pewna różnica - w **JUnit4** testy parametryczne
 działały dzięki *magii*  (zwanej też refleksją). Jeśli coś nie zadziałało 
 to w zasadzie można było się tylko odnieść do kodu JUnit i tam grzebać.
 W **JUnit5** dzięki podejściu funkcyjnemu ilość magii zdecydowanie
 się zmniejsza - sami jesteśmy odpowiedzialni za wygenerowanie testów.
 Dodatkowo cały czas  ładnie podpowiada nam kompilator.
Nie musimy pamiętać np. żeby jakiś konstruktor miał X parametrów itp.,
jak się ilośc parametrów nie zgodzi to się nie skompiluje - tak powinno być!!!

## Czyli jakten **equals** wygląda

Dzięki **Option** z javaslang (**Optional** z java.util podobnie), możemy to całkiem
 ładnie napisać:
 ```
 @Override
     public boolean equals(final Object o) {
         return Option.of((RawData)o)
                 .map( right-> fileContent.equals(right.fileContent))
                 .getOrElse(false);
     }
```


## Jak testować *internalsy*
(Ogólnie nie wiem co znaczy *internalsy*, ale podoba mi się ta nazwa).
W jednym z komentarzy do wątku pojawiło się zagadnienie testowania czy np. metoda filter się wykona.
Chodzi konkretnie o to:
```
public class DataSelector {
    public AccessibleDataFormat filter(AccessibleDataFormat accessibleData) {
        return new AccessibleDataFormat(accessibleData
                .rows
                .filter(data -> data.type.eq(Option.some("GMINY WIEJSKIE")))
                .filter( data -> data.events > 5000));
    }
}
```
I jej użycie w **AverageParticipantsProcessor**:
```
public Either<GenerationProblem, GeneratedResult> process(final AccessibleDataFormat data) {
        return resultGenerator.generate(dataSelector.filter(data));
    }
```

Zasadniczo odpowiedź jest jedna - najlepiej tego nie testować.

No bo co nas obchodzi czy metoda wywołała ten filter - czy nie.
Istotne sa wyniki - sprawdźmy  i na chwilę wywalmy ten **filter**...
``` Failures (2):
    JUnit Jupiter:MagicServiceTests:magicServiceBasics():Case :(Left(CalculationProblem{inputProblem=None, generationProblem=Some(NOT_ENOUGH_DATA)}), too_short.csv)
      JavaMethodSource [javaClass = 'it.makes.me.angry.MagicServiceTests', javaMethodName = 'magicServiceBasics', javaMethodParameterTypes = '']
      => org.opentest4j.AssertionFailedError: expected: <Left(CalculationProblem{inputProblem=None, generationProblem=Some(NOT_ENOUGH_DATA)})> but was: <Right(it.makes.me.angry.data.Output@5f558937)>
    JUnit Jupiter:MagicServiceTests:magicServiceBasics():Case :(Right(it.makes.me.angry.data.Output@5a26340a), prabuty_poludniowe.csv)
      JavaMethodSource [javaClass = 'it.makes.me.angry.MagicServiceTests', javaMethodName = 'magicServiceBasics', javaMethodParameterTypes = '']
      => org.opentest4j.AssertionFailedError: expected: <Right(it.makes.me.angry.data.Output@5a26340a)> but was: <Right(it.makes.me.angry.data.Output@5d67657e)>
```
Pięknie - testy nie przeszły! Mamy już najlepsze sprawdzenie - bo wywalenie filter zabuża wyniki i testy się wykrzaczają.
Niepotrzeba żadnego sprawdzania typu *verify method called exactly once*!
Chola natręci - zostawcie  metody w spokoju! 
Sprawdzacie input, output, a jak sobie metoda do tego doszła i co wywołała to jej sprawa.

W istocie, im bardziej natrętne testy, czyli takie, które sprawdzaja np. czy odpowiednia metoda była z środka wywołana, tym trudniejszy
refaktoring. Takie testy to tylko sposób na zabetonowanie kodu.
A to wcale nie jest takie dobre! Kod jeśli ma być dobry to musi być refaktorowalny.
A jak refaktorować kod - jesli każda zmiana wymusza przerycie testów?
Dlatego testom tzw. [*londyńskim*]([http://programmers.stackexchange.com/questions/123627/what-are-the-london-and-chicago-schools-of-tdd) warto powiedzieć twarde i zdecydowane:
> chyba nie...

# A może jednak
A co jeśli z jakiś dziwnych powodów chcielibysmy upewnić się, że jednak filtrowanie zaszło?
Istnieje lepsza metoda niż test!!!

Popatrzmy na deklarację metody:
```
public class DataSelector {
       public AccessibleDataFormat filter(AccessibleDataFormat accessibleData) 
```
 
Z wyników tej metody korzysta generator:
```
 public Either<GenerationProblem, GeneratedResult> generate(AccessibleDataFormat filteredData) {
```    

A  co jesli  by wymusić wywołanie filtrowania przez wprowadzenie typu:
**FilteredDataFormat**.

Wówczas mamy:
```
 public FilteredDataFormat filter(AccessibleDataFormat accessibleData) {
```
 oraz
 ```
 public Either<GenerationProblem, GeneratedResult> generate(FilteredDataFormat filteredData) {
```
I teraz nie da się łatwo o filtrowaniu zapomnieć, bo na straży stoi kompilator.     
A  jest to narzędzie o wiele potężniejsze od testów, bo nie tylko 
szybciej się wywali. Programista pisząc od razu widzi (po typach) co mu jest potrzebne.
Działają wszystkie podpowiadacze składni!

Ogólnie typy wygrywają nad testami - jeśli tylko da się coś opisać typami.
I tu ciekawostka: wszystko da sie opisać [typami](https://spin.atomicobject.com/2012/11/11/unifying-programming-and-math-the-dependent-type-revolution/) , tylko na razie  jest to przeważnie zbyt trudne.
Ale może, któregoś dnia dojdzie do tego, że napisanie testu to będzie po prostu przyznanie sie do porażki!
Tak jak przyzwyczailismy sie już, że pisanie komentarzy to po prostu przyznanie:
> nie umiem tego dobrze i jasno napisać - więc skomentuję

 I na dziś tyle....
 
 






# Poprzednie odcinki
[ODCINEK 1  Początek](ODCINEK1_PL.md)

[ODCINEK 2  Niech to się chociaż skompiluje](ODCINEK2_PL.md)

[ODCINEK 3  Robimy pierwszy test](ODCINEK3_PL.md)

[ODCINEK 4  Wszystko działa](ODCINEK4_PL.md)

[ODCINEK 5  Naprawiamy](ODCINEK5_PL.md)

[ODCINEK 6  Konkretny refaktoring](ODCINEK6_PL.md)