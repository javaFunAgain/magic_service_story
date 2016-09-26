# Historia Magicznego Serwisu

# Konkretny refaktoring
Po tylu odcinkach wstępu.... chyba nadszedł czas przejśc do rzeczy.
Co zrobimy z klasą  MagicService, która ma 6 zależności?

Aby odpowiedzieć na to pytanie warto zastanowić się co ta klasa robi.
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
Gdy na spokojnie popatrzeć na główną metodę to  widać, że w sumie:

1. Przyjmuje ona pewne dane wejściowe  (po nazwie "pliku")

2. Obrabia je

3. Tworzy output

Strasznie to odkrywcze (chyba każda metoda tak robi :-)  ).

Ale to oznacza, że z pewnością nie potrzebujemy 6 zależności.
Wystarczą góra 3:

1.  Jakiś zapodawacz danych - **DataProducer**  stworzy **AccessibleDataFormat**

2. Jakiś obrabiacz danych - **DataProcessor** przerobi je na **GeneratedData**

3. I jakiś wypluwacz - **OutputFormatter** - był i będzie
 
 Do boju!
    
## I w ten sposób powstaje zupełnie nowy **MagicService**
```
 public class MagicService {
     private final DataProducer dataProducer;
 
     private final DataProcessor dataProcessor;
 
     private final OutputFormatter outputFormatter = new OutputFormatter();
 
     public MagicService(DataProducer dataProducer, DataProcessor dataProcessor) {
         this.dataProducer = dataProducer;
         this.dataProcessor = dataProcessor;
     }
 
     public Either<CalculationProblem, Output> performComplexCalculations(Input input){
             final Either<InputProblem, AccessibleDataFormat> inputData = dataProducer.extractData(input);
             final Either<CalculationProblem, GeneratedResult> generatedData =
                     inputData
                             .mapLeft( prob ->new CalculationProblem(prob))
                             .flatMap( data -> dataProcessor.process(data).mapLeft(
                                     prob -> new CalculationProblem(prob)
                             ));
 
             return generatedData.map(outputFormatter::formatOutput);
     }
 }
```
Warto zauważyć, że nie chciało się nam robić konfigurowanej 
zależności od **OutputFormattera**, no bo kto by go zmieniał.


Oczywiście, część logiki wynieslismy np. do:
```
public class ResourceDataProducer implements  DataProducer{
    private final DataCollector dataCollector  = new DataCollector();
    private final DataExtractor dataExtractor  =  new DataExtractor();
    private final DataTransformer dataTransformer  =  new DataTransformer();
    @Override
    public Either<InputProblem, AccessibleDataFormat> extractData(Input input) {
        return dataCollector
                .collectData(input)
                .map(dataExtractor::extractRelevant)
                .flatMap(dataTransformer::transformToAccessibleFormat);
    }
}
```

## Finał 
Mamy teraz klasę, której można wstrzyknąć np. innego DataProducera
```
public class Runner {
    public static void main(final  String ... args) {
        final MagicService service = new MagicService(new ResourceDataProducer(),
                new AverageParticipantsProcessor());
        System.out.println(service.performComplexCalculations( new Input("real_data.csv"))
                .map(out->out.value)
                .getOrElseGet( problem -> "Problem:" + problem.toString()));
    }
}
```

Ale to nie koniec pracy nad naprawianiem kodu!


# Poprzednie odcinki
[ODCINEK 1  Początek](ODCINEK1_PL.md)

[ODCINEK 2  Niech to się chociaż skompiluje](ODCINEK2_PL.md)

[ODCINEK 3  Robimy pierwszy test](ODCINEK3_PL.md)

[ODCINEK 4  Wszystko działa](ODCINEK4_PL.md)

[ODCINEK 4  Naprawiamy](ODCINEK5_PL.md)