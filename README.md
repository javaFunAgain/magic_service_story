# Historia Magicznego Serwisu

# Początek

Zaczęło się od tego, że kolega *S* rozpoczął flame na forum [4programmers.net](http://forum.4programmers.net/Inzynieria_oprogramowania/276798-wstrzykiwanie_zaleznosci_a_testy_jednostkowe_-_zloty_srodek)

Podał taki kod:
```
public class MagicService {
    private DataCollector dataCollector;
    private DataExtractor dataExtractor;
    private DataTransformer dataTransformer;
    private DataSelector dataSelector;
    private ResultGenerator resultGenerator;
    private OutputFormatter outputFormatter;
 
    public Optional<Output> performComplexCalculations(Input input){
        try {
            RawData rawData = dataCollector.collectData(input);
            List<RelevantData> relevantData = dataExtractor.extractRelevant(rawData);
            List<AccessibleDataFormat> accessibleData = dataTransformer.transformToAccessibleFormat(relevantData);
            List<AccessibleDataFormat> filteredData = dataSelector.filter(accessibleData);
            List<GeneratedResult> generatedData = resultGenerator.generate(filteredData);
            return Optional.of(outputFormatter.formatOutput(generatedData));
        }catch(IOException ex){
            //handling1
        }catch(DataFormatException ex){
            //handling2
        }catch(GenerationException ex){
            //handling3
        }
        return Optional.empty();
    }
}
```

Problem: **Jak to zrobić bez wstrzykiwania (DI frameworku)?**

## Pochylam  się (z troską) nad kodem

Pierwsza myśl:
> Łooo Panie! Kto tu Panu tak spierdolił.

Jakie są problemy:
- Po  pierwsze primo - się nie skompiluje, bo nie wiadomo co to są te klasy typu
*DataCollector*! Ale skucha! Trzeba to  będzie naprawić,

- Po drugie primo, nawet jak się te klasy napisze, wiadomo że kod nie zadziała - walnie NullPointerException, bo *dataCollector* będzie nullem. 
Ok, pewnie autor miał jakiś framework do dependency injection co mu tam coś wstrzykiwał.  
Moglibyśmy tutaj o jakies *CDI* albo *Springa() uzupełnić - ale to bez sensu, bo
powszechnie wiadomo, że magia DI nie działa w piątki i zostaniemy, jak te głupki, nie dość, że z nullami, 
 to jeszcze będzie nam sie jakiś beans.xml pałętał (won z tym!).
(Zakładamy złośliwie, że jest piątek przed pełnią księżyca - żaden @Autowired nie ma szans - trzeba to będzie zrobić lepiej).

- Po trzecie primo -  nie bardzo wiadomo jak kod testować - testów nie było, wiadomo, że 
na frameworkach  DI testować da się tylko na Mockito,  a Mockito przecież jest już dobrze przetestowane [dowód!](https://github.com/mockito/mockito/tree/master/src/test/java/org),
   i nie ma sensu testować tych moków po raz kolejny.
   
- Po czwarte primo co to za wyjątki się pałetają (w 2016) - 
autor wyjaśnia:
> Co do handlingu to jest to szczegół, może jest tam rzucenie customowego wyjątku, może logowanie błędu, nie jest to jakoś specjalnie istotne co konkretnie się tam dzieje (nie chciałem zaciemniać obrazu), ale załóżmy że coś się dzieje i dobrze by było przetestować czy błąd jest obsługiwany poprawnie.

No ale wiemy, że kłamie! No bo co za handling jak rzuca Optional.empty() i kończy.
Nieładnie. Pewnie tylko pisze do *loga* - jak wszyscy - i to jest ta cała obsługa.
      
  




 
 


