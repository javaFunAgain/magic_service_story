# Historia Magicznego Serwisu

# Robimy pierwszy test

No więc łatwo widać, że pierwszy trywialny test mógłby polegać na podaniu jakiegoś bezsensownego
pliku i sprawdzeniu czy dostaniemy ```empty```.

## Do it
Dla ułatwienia skonfigurujemy sobie [Junit5](http://junit.org/junit5/docs/current/user-guide/#writing-tests-dynamic-tests).
I to jeszcze na dynamic testach (cokolwiek by to nie było). Na pewno będzie zabawnie.

Po chwili zabawy z konfiguracją **gradle** mamy takiego potworka:
```
    @TestFactory
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
Ha! Dokładnie tak, jak to było przewidziane. Albowiem, nikt nie ustawił  zależności (jeszcze).
Na razie odchaczam sobie kolejny issue (Test już jakiś jest).
Ale dopisuje nowy (trzeba to porządniej potestować).

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
Super słówko kluczowe *new*  - (warto zapamiętać). (Pomijamy sytuację, kiedy klasy te mają np. prywatny konstruktor, 
nie było nic takiego  w założeniach, a poza tym by nas nie powstrzymało...).

Jest oczywiście jeden zonk. Gdyby te moduły, klasy nie były klasami javy - tylko dziwactwami 
CDI/Springa. Wtedy **new** nie pomoże, i trzeba by najpierw je uratować i przemienić w zwykłą Javę. Ale tu na szczęście
tak źle nie ma (autor nic nie pisał) więc jedziemy.

Przy okazji, nieważne ile godzin trwa podnoszenie tych klas - nie musimy tego robić więcej niż raz, bo
**MagicService**, jak na razie, jest całkiem bezstanowy! (I całkiem bez sensu, ale to się da naprawić).

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


# Poprzednie odcinki
[ODCINEK 1  Początek](ODCINEK1_PL.md)

[ODCINEK 2  NIech to się chociaż skompiluje](ODCINEK2_PL.md)