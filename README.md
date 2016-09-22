# Historia Magicznego Serwisu

# Robimy pierwszy test

No więc łatwo widać, że pierwszy trywialny test mógłby polegać na podaniu jakiegoś bezsensownego
pliku i sprawdzeniu czy dostaniemy ```empty```.

## Do it
Dla ułatwienia skonfigurujemy sobie [Junit5](http://junit.org/junit5/docs/current/user-guide/#writing-tests-dynamic-tests).
I to jeszcze na dynamic testach (cokolwiek by to nie było). Na pewno będzie zabawnie.

Po chwili zabawy z konfiguracją **gradle** mam takiego potworka:
```@TestFactory
    Iterable<DynamicTest> magicServiceBasics() {
        final MagicService theTestedService = new MagicService();
        return Arrays.asList(
                dynamicTest("should return empty it file does not exist",
                        () -> {
                             assertFalse(
                                     theTestedService.performComplexCalculations(
                                             new Input("bad_file")).isPresent());
                        })
        );
    }
```

Po odpaleniu testu mamy: 
```
      JUnit Jupiter:MagicServiceTests:magicServiceBasics():should return empty it file does not exist
        JavaMethodSource [javaClass = 'it.makes.me.angry.MagicServiceTests', javaMethodName = 'magicServiceBasics', javaMethodParameterTypes = '']
        => java.lang.NullPointerException
```
Dokładnie tak jak to było przewidziane. Albowiem nikt nie ustawił  zależności (jeszcze).
Na razie odchaczam sobie kolejny issue (Test już jakiś jest).
Ale dopisuje nowy (trzeba to porządnie potestować).

## No to weźmy się za zależności
Nieważne co by nam tu kłamał autor oryginalnego postu - my wiemy, że ma po jednej implementacji
dla kazdej zależności. Gdyby miał więcej to by od razu napisał ( a nie coś tam kręcił w komentarzu).
Poza tym wszysscy mają po jednej - bo kto ma czas takie dataCollectory pisać rożne  jak jeden dobry starczy.
Pozostałe zależności zresztą tak samo!

Dlatego zasadniczy problem autora rozwiążemy ultra prosto.
```
    private final DataCollector dataCollector  = new DataCollector();
    private final DataExtractor dataExtractor  =  new DataExtractor();
    private final DataTransformer dataTransformer = new DataTransformer();
    private final DataSelector dataSelector = new DataSelector();
    private final ResultGenerator resultGenerator  = new ResultGenerator();
    private final OutputFormatter outputFormatter = new OutputFormatter();
```
O matko! Jak prosto...   
Autor pisał co prawda:
> Na potrzeby dyskusji załóżmy że wszystkie te klasy będące zależnościami są mocno skomplikowane - możliwe że to są entry pointy do całych modułów z kupą kodu.

No, ale nieważne jak skomplikowane są te moduły, jak są to klasy Javy, to się przez **new** da podnieść!
Super słówko kluczowe *new*  - (warto zapamiętać).

Jest oczywiście jeden zonk. Gdyby te moduły, klasy nie były klasami javy - tylko dziwactwami 
CDI/Springa. Wtedy **new** nie pomoże, i trzeba by najpierw je uratować i przemienić w zwykłą Javę. Ale tu na szczęście
tak źle nie ma (autor nic nie pisał) więc jedziemy.

## Jeszcze jedno odpalenie Testu `gradle clean test` 
I mamy UnsupportedOperationException. Pewien postęp - sam tego *Unsuppor Teda* napisałem.
No to - pierwsza poprawka. Zmieniamy Unsupported w  DataCollector na IOException.
```
public class DataCollector {
    public RawData collectData(Input input) throws IOException{
        throw new IOException();
    }
}
```
I notujemy kolejny poważny sukces.
```
[         1 tests found      ]
[         0 tests skipped    ]
[         1 tests started    ]
[         0 tests aborted    ]
[         1 tests successful ]
[         0 tests failed     ]
[         0 containers failed]
```

## Co dalej
Niby sukces, ale zadowolenia nie  ma. Bo:

1. Nadal nie wiem jaka jest średnia liczbę uczestników imprez w domach kultury, w województwach gdzie było
więcej niż 5tys imprez na Rok 2015, w  gminach wiejskich! (daje issue)
2. Te 6 zależności w klasie MagicService bije po oczach (dodaje issue)
3. No i nadal mamy Exceptiony (jak było)



# W poprzednich odcinkach

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
      
  




 
 


