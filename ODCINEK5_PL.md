# Naprawiamy

W poprzednim odcinku udało się doprowadzić do końca implementację.
W ferworze walki i zadowolenia zupełnie zapomnieliśmy, że juz mamy program, który potrafi
rozwiązać nurtującą nas zagadkę.

*Ile było średnio uczestników imprez w 2015w gminach wiejskich, w takich województwach, że
 imprez było ponad 5 tysięcy?*
 
 ## Not to zobaczmy
  Piszemy takiego runnera:
  ```
    public class Runner {
      public static void main(final  String ... args) {
          final MagicService service = new MagicService();
          service.performComplexCalculations( new Input("real_data.csv"))
                  .map(out->out.value)
                  .ifPresent( System.out::println);
      }
  }
  ```
I po  odpaleniu okazjue się ... error. Coż za zdziwienie - przecież mamy testy.
Ale nic to  - nie pierwszy raz - zawinił fałszywy selektor - szukaliśmy 
"GMINA WIEJSKA" -  a w danych jest "GMINY WIEJSKIE". Głupie testy.

Po naprawieniu  błędu mamy wynik:
2440498

Rewelacja.

## My jednak nie spoczywamy na laurach 
 Spróbujmy teraz nieco popoprawiać ten kod.
 Rzeczy które najbardziej dołują to:
 
 1. Używanie Exceptionów (przy czym złe - bo na wyjściu w razie czego jest Optional.empty)
 2. Różne poziomy abstrakcyj - z jednej strony przetwarzamy RawData - a zdrugiej  List of coś tam.
 3. Za dużo zależności (6)  i opdowiedzialności w *MagicService*
 
 ## Zacznijmy od Exceptionów 
 Rysuje się jedna opcja - spróbujmy zastąpić Exceptiony *Either*em.
 W zasadzie mógłby być jeszcze *Try*, który zawiera w sobie Exception- Cause. Ale nie!
 Dlaczego: 
 - jak coś zdażyło się w miejscu spodziewanym - to rzucimy tam odpowieni status błedu (i linijka kodu ze stack trace nam niepotrzebna),
 - jak zdażyło się niespodziewanie - to i tak wtedy obsługe załatwiamy *RuntimeException* i stack trace mamy.
 
 Tak, to w zasadzie wniosek z wielu lat praktyki - checked exceptions w javie to pomyłka!!!
 I nie tylko my tak sądzimy:  [Javas biggest mistake](http://literatejava.com/exceptions/checked-exceptions-javas-biggest-mistake/)
 
 ## Totalna *Either*yzacja
 Najpierw wprowadźmy klasę opisującą status obliczeń, a właściwie - co poszło nie tak.
 ```
 public enum CalculationProblem {
     RESOURCE_NOT_FOUND,
     WRONG_RESOURCE_NAME,
     UNABLE_TO_LOAD_DATA,
     BAD_DATA_FORMAT,
     NOT_ENOUGH_DATA
 }
```

Można by być bardziej szczegółowym i zamiast Enuma - dać Status z opisem (np. która linia się zepsuła),
ale tu sobie odpuścimy.

Teraz nasza główna metoda może zwracać:
```public Either<CalculationProblem, Output> performComplexCalculations(Input input){```
Czyli albo wynik, albo jaki problem.

## Przekształcanie na Eithery
Jak to wygląda można zobaczyć na przykładzie klasy Input:
```
public final class Input {
    public final String resourceName;

    public Input(String resourceName) {
        this.resourceName = resourceName;
    }

    public Either<CalculationProblem, Path> getPath() {
        return getURI().map( Paths::get);
    }

    private Either<CalculationProblem, URI> getURI() {
        final Option<URL> resource = Option.of(getClass().getResource("/" + this.resourceName));
        return resource
                .map(this::resourceToURI)
                .getOrElse(Either.left(CalculationProblem.RESOURCE_NOT_FOUND));
    }

    private Either<CalculationProblem, URI> resourceToURI(final URL resource) {
        try {
            final URI uri = resource.toURI();
            return Either.right(uri);
        } catch (URISyntaxException e) {
            return Either.left(it.makes.me.angry.CalculationProblem.WRONG_RESOURCE_NAME);
        }
    }
}
```
Czyli dość prostacko - jak coś sie wywali (Exception) - to nie rzucamy go dalej tylko zwracamy
odpowiedni Either.left.

Mogło by się wydawać głupie - ale zysk widać w metodzie głównej:
```
public Either<CalculationProblem, Output> performComplexCalculations(Input input){
            final Either<CalculationProblem, RawData> rawData = dataCollector.collectData(input);
            final Either<CalculationProblem, List<RelevantData>> relevantData =
                    rawData.map( raw->dataExtractor.extractRelevant(raw));
            Either<CalculationProblem,List<AccessibleDataFormat>> accessibleData =
                     relevantData.flatMap( dataTransformer::transformToAccessibleFormat);
            Either<CalculationProblem,List<AccessibleDataFormat>> filteredData =
                    accessibleData.map( dataSelector::filter);
            Either<CalculationProblem, List<GeneratedResult>> generatedData =
                    filteredData.flatMap( data  -> resultGenerator.generate(data));
            return generatedData.map(outputFormatter::formatOutput);
    }
```
Ta metoda to teraz prosta seria obliczeń. Wyleciały wszystkie catch. 
I testowanie staje się prostsze: zamiast
 sprawdzania kolejnych exceptionów itp. po prostu testujemy kolejne przypadki tak jak normalne wartości!

```
 @TestFactory
    Iterable<DynamicTest> magicServiceBasics() {
        final MagicService theTestedService = new MagicService();
        return Array.of(
                forInputProblem("bad_file" ,CalculationProblem.RESOURCE_NOT_FOUND ),
                forInputProblem("bad_file.csv" ,CalculationProblem.BAD_DATA_FORMAT ),
                forInputProblem("too_short.csv" ,CalculationProblem.NOT_ENOUGH_DATA),
                forInputOutput("prabuty_poludniowe.csv" ,"373603")
                )
                .map( testCase -> dynamicTest("Case :" + testCase, () -> {
                    assertEquals(testCase._1,
                            theTestedService.performComplexCalculations(
                                    new Input(testCase._2)));
                }) );
    }
```
Chociaż, pewne problemy które potencjalnie mogą występować (mamy catch w kodzie). Nie dają się przetestować 
(czyżby trzeba było Mocki podpiąć ? :-) .
Dokładnie to - nie udaje się uzyskać normalnie sytuacji, żeby wystąpiło:
 ```
  public enum CalculationProblem {
      ...
      WRONG_RESOURCE_NAME,
      UNABLE_TO_LOAD_DATA,
      ...
  }
 ```
Mamy na to w zanadrzu rozwiązanie, ale jeszcze przyjdzie nie nie czas (odcinek 78).

## Poprawmy trochę Typy
W trakcie prac zauważyliśmy, że trochę kuleje enkapsulacja. 
Niezbyt fajnie wyglądają wyrzucone na wierzch Listy w głównej metodzie.

Powinno się albo pisać listaX.map( transformruj element  ), albo operować
na Typach enkapsulujących listę.
czyli taki kod:
 ```
 public List<AccessibleDataFormat> transformToAccessibleFormat(List<RelevantData> relevantData)
 ```
 To średni pomysł. 
 Lepiej już zrobić metodę konwertującą wiersz **RelevantData** na**AccessibleDataFormat**.
 I wywoływać map( ... ).
  
  Można też całkiem ukryć Listy w API (enkapsulując je). Potworzy to mnóstwo nowych klas - ale
  może być zabawne. (Czyli tak zrobim).
 
## Efekty
Teraz **MagicService** wygląda tak:
```
public Either<CalculationProblem, Output> performComplexCalculations(Input input){
            final Either<CalculationProblem, RawData> rawData = dataCollector.collectData(input);
            final Either<CalculationProblem, RelevantData> relevantData =
                    rawData.map( raw->dataExtractor.extractRelevant(raw));
            Either<CalculationProblem,AccessibleDataFormat> accessibleData =
                     relevantData.flatMap( dataTransformer::transformToAccessibleFormat);
            Either<CalculationProblem,AccessibleDataFormat> filteredData =
                    accessibleData.map( dataSelector::filter);
            Either<CalculationProblem, GeneratedResult> generatedData =
                    filteredData.flatMap( data  -> resultGenerator.generate(data));
            return generatedData.map(outputFormatter::formatOutput);
    }
```
Znikły listy - został Either - można z tym żyć. 
I chyba w następnym odcinku dojdziemy do konkretów.

