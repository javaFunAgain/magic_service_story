# Niech to wszystko  - jakoś zadziała - potem naprawimy kod
 
 W poprzednich odcinkach udało się doprowadzić do kompilowania kodu, 
 ba! poznalismy co kod w ogóle miał robić, no i nawet jest test 
 (czyli jesteśmy już full profesjonal). 
 
 Nadal niestety kod jest troszkę nie w pytkę, no i nie robi tego co miał.
  Powstał dylemat  - czy najpierw naprawiamy strukturę,  a potem funkcjonalność ( z pewnością krótsza droga),
  czy też idziemy na około z tym badziewnym designem do końca, a potem to będziemy naprawiać.
  
  Droga druga jest z pewnością bardziej męcząca, ale ma taką zaletę, że będzie więcej widać i można będzie porównać rozwiązania.
   Więc wybrane ! Idziemy dookoła.
   
## Czyli najpierw dociągniemy do końca funkcjonalność
   
### Zaczniemy od obrobienia **DataCollector**
  Więc test:
```
@TestFactory
    Iterable<DynamicTest> dataCollectorTests() {
        final DataCollector theCollector = new DataCollector();
        return Arrays.asList(
                dynamicTest("should return string for simple file",
                        () -> {
                            assertEquals(new RawData("nothing special"),
                                    theCollector.collectData(new Input("raw_file.csv")));
                        })
                //We do not test Exception - because we do not want it at first place!
        );
    }  
```
    
 assertEquals jednakowoż zadziała tylko jak ***RawData*** będzie miało **equals**.
### Miejsce: **RawData** Misja: wprowadzić  ~~komunizm,~~ ~~równość,~~ equals   
 Więc go generujemy (ale skucha - Java niestety):
 ```
 @Override
     public boolean equals(Object o) {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;
         RawData rawData = (RawData) o;
         return fileContent.equals(rawData.fileContent);
     }
 ```
 Uczciwie powiedzmy  - to jest koszmar, nie możemy tego tak zostawić.
 
 Więc primo: 
 ``` if (this == o) return true;```, a co mnie to obchodzi  - przecież jeśli 
 porównujemy obiekt sam z sobą to na pewno equals na stringach z ostatniej
 lini zadziała (```fileContent.equals(rawData.fileContent);```). Wywalamy!
 Nie ma tej linjki.
 
 ```  if (o == null || getClass() != o.getClass()) return false;``` - też pięknie,
 a niby skąd się ten null miałby wziąć? Wywalamy! (Jak ktoś używa **nulla** to powinien dostać 
 **NullPointerException** i my mu go chętnie podamy).
 
  Zostajemy z:
  ```if (getClass() != o.getClass()) return false;``` - w zasadzie mogłoby zostać, 
  ale  nie rozumiem na kiego grzyba ktoś miałby nasze RawData z czymś innym porównywać!
  Niech mu to się lepiej wywali **classcastem** prosto w twarz! Wywalamy!
  
 I mamy:
 ```
     @Override
     public boolean equals(Object o) {
         return fileContent.equals(((RawData)o).fileContent);
     }
```
Kod jest już ładny - ale niestety narusza kontrakt equals (z klasy Object): 
>  For any non-null reference value x, x.equals(null) should return false.

Co za banda głupków taki kontrakt obmyśliła. Ale zrobimy coś z tym potem (markujemy sobie TODO). 

 Z hashCode, za to, jakoś nie ma problemu! Kod jest ładny.
 ```
 @Override
     public int hashCode() {
         return fileContent.hashCode();
     }
```
     
 Jak już mamy **RawData** i Testy  to implementacja **DataCollector** jest prosta:
 ```
 public final class DataCollector {
     public RawData collectData(final Input input) throws IOException{
         return new RawData(new String(Files.readAllBytes(Paths.get(input.getURI()))));
     }
 }
 ```
 
 
 Tylko trzeba było **Input** podrasować - dorzucając mu metodę:
 ```
     public URI getURI() throws IOException {//TODO: oh no, no checked exceptions anymore
         try {
             final URL resource = getClass().getResource("/" + this.resourceName);
             if (resource != null) { //TODO: remove this if
                 return resource.toURI();
             } else {
                 throw new IOException("there is no " + this.resourceName); //TODO: do not throw
             }
         } catch (URISyntaxException e) {
             throw new IOException(e); //TODO: do not throw, please
         }
     }
 ```
 Jak widać jest to metoda śmietnik, ale na razie działa, oznaczmy wiele rzeczy do poprawy (TODOsy).
 
## Testy przechodzą
 
 Całe dwa!
 
 Czas więc pójść o krok dalej i zabrać się za **DataExtractor**.
 
## Średnia historia **DataExtractor** 
 
 Zgodnie z opisem, z jednego z poprzednich odcinków, **DataExtractor** ma za zadanie
 wstępnie przeparsować **RawData** (mówmy String)  i zrobić listę czegoś co się nazywa:
 **AccessibleDataFormat**.
 
 ### Najpierw przygotujmy jakies proste dane testowe
 Te np. dotyczące przyszłego województwa *Prabuty południowe*
 ```
 "Kod";"Nazwa";"imprezy;2015;[szt.]";"uczestnicy imprez;2015;[osoba]";
 3210000000;"PRABUTY POL";7237;1094693;
 3210000000;"PRABUTY POL - GMINY MIEJSKIE";1872;284292;
 3210000000;"PRABUTY POL - GMINY MIEJSKO-WIEJSKIE";3623;436798;
 3210000000;"PRABUTY POL - GMINY WIEJSKIE";1742;373603;
 3210000000;"PRABUTY POL - MIASTO";3783;687915;
 3210000000;"PRABUTY POL - WIEŚ";3454;406778;
 ```
 
 Jak to ugryźć? Widać miejsce na 2 String splity:
  1. enterami - oddzielamy wiersze
  2. średnikami - oddzielamy kolumny
  
### Od razu toczymy boj z klasą **RelevantData** o equals!
 I podobnie jak poprzednio kończymy z czymś takim:
 ```
@Override
    public boolean equals(Object o) {
        RelevantData that = (RelevantData) o;
        return dataColumns.equals(that.dataColumns); //TODO: does not fulfil contract of Object.equals
    }
```
 
### Testy są w sumie łatwe
 
 
 Testy zróbmy takie jak poniżej:
 ```
  @TestFactory
     Iterable<DynamicTest> dataExtractorTests() throws IOException {
         final DataExtractor theExtractor = new DataExtractor();
         final RawData inputData = new DataCollector().collectData(new Input("prabuty_poludniowe.csv"));
 
         return Arrays.asList(
                 dynamicTest("result should have 6 rows",
                         () -> {
                             assertEquals(6, theExtractor.extractRelevant(inputData).size());
                         }),
                 dynamicTest("result should have 6 rows",
                         () -> {
                             assertEquals("7237", theExtractor.extractRelevant(inputData).get(0).dataColumns.get(2));//TODO: we know too much about RelevantData
                         }),
                 dynamicTest("result should have 6 rows",
                         () -> {
                             assertEquals("406778", theExtractor.extractRelevant(inputData).get(5).dataColumns.get(3));
                         }),
 ```
 Widać problem z enkapsulacją - z dużo wyłazi nam z klasy RelevantData...
 
### A **DataExtractor** jest prosty
 ```
 public List<RelevantData> extractRelevant(RawData rawData) {
         final String[] rows = rawData.fileContent.split("\n");
         return List.of(rows).drop(1).map( this::splitRow);
     }
 
     private  RelevantData splitRow(final String row) {
         return new RelevantData(Array.of (row.split(";")).map(s->s.replaceAll("\"(.*)\"","$1")) );
     }
 ```
 
Done!
 
### To teraz **DataTransformer**
  
Ta klasa będzie brała **RelevantData** (czyli nie oszukujmy się, po prostu tablicę stringów) i przewalała na postać obiektową  
**AccessibleDataFormat**. Najgorsze, że robi to na liście! Czyli nieładnie się bawimy functorem (potem się to naprawi). 
Ale jak to jakoś przeżujemy  - to kod jest prosty (prawie):

```
private AccessibleDataFormat parse(RelevantData dt) {
        final String[] regions = dt.dataColumns.get(1).split("-");
        return new AccessibleDataFormat(
                regions[0].trim(),
                regions.length == 2 ? Option.some(regions[1].trim()) : Option.none(),
                Integer.parseInt(dt.dataColumns.get(2)),
                Integer.parseInt(dt.dataColumns.get(3)));
    }
```
### Dochodzimy do DataSelectora
Który załatwiamy prostacko:
```
public class DataSelector {
    public List<AccessibleDataFormat> filter(List<AccessibleDataFormat> accessibleData) {
        return accessibleData
                .filter(data -> data.type.eq(Option.some("GMINY WIEJSKIE")))
                .filter( data -> data.events > 5000);
    }
}
```
Łatwo też było napisać testy tej funkcjonalności. (Jest do zobaczenia w źródłach).

### I robimy **ResultGenerator**
Tu z automatu (przypominam, że liczymy średnią). A cudo wygląda tak:
```
public class ResultGenerator {
    public List<GeneratedResult> generate(List<AccessibleDataFormat> filteredData)
            throws GenerationException {
        final BigDecimal sum = new BigDecimal(filteredData.map(data -> data.participants).reduce(
                (a,b)-> a+ b).intValue());
        if ( filteredData.size() > 0 ) {
            return List.of(new GeneratedResult(sum.divide( new BigDecimal(filteredData.size()))));
        } else {
            throw new GenerationException();
        }
    }
}
```
Paskudny... ale da się naprawić (potem).

###  Zostaje tylko OutputFormatter
Który to jest trywialny, ale  za to zobaczmy testy, które pokazują: po co bawić sie w *JUNIT5* i dynamic tests.
 ```
  @TestFactory
     Iterable<DynamicTest> testOutput() {
         final OutputFormatter theOututFormatter = new OutputFormatter();
         final Array<Tuple2<BigDecimal, String>> testCases = Array.of(
                 Tuple.of(new BigDecimal("665.99"), "666"),
                 Tuple.of(new BigDecimal("666"), "666"),
                 Tuple.of(new BigDecimal("0.00"), "0")
         );
         return testCases.map(
                 (Tuple2<BigDecimal, String> testCase) -> dynamicTest("should return empty if file does not exist", () -> {
                     final List<GeneratedResult> sampleResult = List.of(new GeneratedResult(testCase._1));
                     assertEquals(testCase._2, theOututFormatter.formatOutput(sampleResult).value);
                 }));
     }
 ```
Jak widać tanio można zrobić testy parametryczne (i to bez magii  - jak to było we wcześniejszym JUnit).
Tak zrobimy potem  z resztą testów, ale dopiero po naprawieniu API i wywaleniu checked exceptions.

## Ufff  - koniec
 Teraz mamy już aplikację, która w ogólności chyba przypomina zamysł autora. Jedyna różnica - to to, że pewnie
  autor miał tam jakiegoś Springa, Guice czy inny framework do wstrzykiwania. 
  A my nie mamy - bo nie było potrzeby.
  Tu wrzucamy stosowny cytat:
  > "A good architecture maximizes the number of decisions NOT made".
    - Robert C. Martin, 
  
  
  Dorobimy sobie później taką taką potrzebę, ale najpierw trzeba kod gruntownie ponaprawiać.  
 
 Ale to w kolejnym odcinku.
