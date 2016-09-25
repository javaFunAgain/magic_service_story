# Historia Magicznego Serwisu

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
 2. Różne poziomy abstrackji - z jednej strony przetwarzamy RawData - a zdrugiej  List of coś tam.
 3. Za dużo zależności (6)  i opdowiedzialności w *MagicService*
 
 ## Zacznijmy od Exceptionów 
 Rysuje się jedna opcja - spróbujmy zastąpić Exceptiony *Either*em.
 W zasadzie mógłby być jeszcze *Try*, który zawiera w sobie Exception- Cause. Ale nie!
 Dlaczego: 
 - jak coś zdażyło się w miejscu spodziewanym - to rzucimy tam odpowieni status błedu (i linijka kodu ze stack trace nam niepotrzebna),
 - jak zdażyło się niespodziewanie - to i tak wtedy obsługe załatwiamy *RuntimeException* i stack trace mamy.
 
 Tak, to w zasadzie wniosek z wielu lat praktyki - checked exceptions w javie to pomyłka!!!
 I nie tylko my tak sądzimy:  [Javas biggest mistake](http://literatejava.com/exceptions/checked-exceptions-javas-biggest-mistake/)
 

# Poprzednie odcinki
[ODCINEK 1  Początek](ODCINEK1_PL.md)

[ODCINEK 2  Niech to się chociaż skompiluje](ODCINEK2_PL.md)

[ODCINEK 3  Robimy pierwszy test](ODCINEK3_PL.md)