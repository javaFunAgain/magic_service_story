# Historia Magicznego Serwisu

# Niech to się chociaż skompiluje
 
## Zależności
 Zacznijmy od popisania tych wszystkich brakujących klas typu *DataCollector* czy *Extractor*.
 Ponieważ autor nie podał co jakie wyjątki rzuca więc sobie to sami wymyślimy (a co!).
 
 I tu zonk - bo widać, że trzeba by wymyśleć co ten kod robi.
## Co ten kod robi do cholery?
Łatwo widać (po nazwach), że autor miał na myśli ~~tesknąte za ojczyzną i ogólnie taki jakiś martkotny był~~,
 * pobieranie danych z jekiegosś czegoś (pliku),
 * wstępną selekcję ,
 * transformowanie (czyli pewnie parsowanie),
 * śjakieś filtrowanie, 
 * śjakieś obliczenia (kij wie),
 * i na koniec transforomowanie do outputu
  
Dużo,  jak na jedną metodę.... Ale trzeba to ukonkretyzować.

## Bierzemy dane
Dane w 2016 łatwo zdobyć. Idziemy więc na stronę [Banku Danych Lokalnych](https://bdl.stat.gov.pl/BDL/start)
 i zaznaczmy (po kolei):
 * Dane, 
 * Kultura i sztuka
 * DZIAŁALNOŚĆ CENTRÓW, DOMÓW, OŚRODKÓW KULTURY, KLUBÓW I ŚWIETLIC
 * Domy i ośrodki kultury, kluby i świetlice    
 * Dalej
 * Wybieramy *Rok 2015* oraz obydwa *imprezy* i *uczestnicy imprez*
 * Dalej
 * Agregaty
 * Wybieramy wszytkie rodzaje gmin (WIEJSKIE też)
 * Export  CSV Tablica wielowymiarowa -  i już jestesmy królem rocznika statystycznego
  
  ALE CZAD!
  
  ## Teraz troche łatwiej to opisać
  
  
1. ```RawData rawData = dataCollector.collectData(input);```
  Bierzemy dane z pliku i wrzucamy do wielkiego Stringa.
2. ```List<RelevantData> relevantData = dataExtractor.extractRelevant(rawData);```
  Tokenizujemy Stringa do listy stringów wiersze, wyrzucamy wiersze nagłówkowe każdy wiersz tokenizujemy again (kolumny).
3.  ```List<AccessibleDataFormat> accessibleData = dataTransformer.transformToAccessibleFormat(relevantData);```
 Parsujemy to do jakiejśc obiektowej postaci  - {Wojewódzwto, Liczba imprez, Uczestnicy}
4. ```List<AccessibleDataFormat> filteredData = dataSelector.filter(accessibleData);```
  Filtrujemy tylko przypadki gdzie było więcej niż 5 tysięcy imprez i odrzucamy POLSKA (to nie wojewódzwo!).
   Dodatkowo tylko GMINY WIEJSKIE.
5. ```List<GeneratedResult> generatedData = resultGenerator.generate(filteredData);```
Z pozostałych zbieramy średnią.
6. ```return Optional.of(outputFormatter.formatOutput(generatedData));```
I pięknie formatujemy wynik (na Stringa) i wpychamy go w  Optionala. 


Mamy zatem średnią liczbę uczestników imprez w domach kultury, w województwach gdzie było
więcej niż 5tys imprez na Rok 2015, ale tylko  gminy wiejskie! *Aż się nie mogę doczekać ile to wyjdzie*!

## No i do dzieła
Uzupełniamy wszystkie klasy i typy tak, żeby się zgadzały. 
Dla pogłebenia chaosu wszystkie typy danych wrzucamy do pakiet *data*, 
a te dziwne zależności do *processors*.   
Trzeba było jeden własny wyjątek stworzyć, wrzucamy go do pakietu *hell*, niech gnije w piekle. 

## gradle cTJ
I kurde kompiluje się. Czyli pewnie działa..... Wszystkie testy przechodzą!
Ale, że testów jest zero, a kod słaby to niestety nie wiadomo.
Trzeba będzie sprawdzić w nastepnym odcinku.
Ale piersze issue zrobione! 


# W poprzednich odcinkach

# Początek

Zaczęło się od tego, że kolega **S** rozpoczął flame na forum [4programmers.net](http://forum.4programmers.net/Inzynieria_oprogramowania/276798-wstrzykiwanie_zaleznosci_a_testy_jednostkowe_-_zloty_srodek)

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
Moglibyśmy tutaj o jakies **CDI** albo **Springa** uzupełnić - ale to bez sensu, bo
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
Nieładnie. Pewnie tylko pisze do **loga** - jak wszyscy - i to jest ta cała obsługa.
      
  




 
 


