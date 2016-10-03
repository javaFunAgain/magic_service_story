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
( a nuż widelec jakaś dziwna mapa, czy lista tego porównania używa).

## Napiszmy więc Test

# Poprzednie odcinki
[ODCINEK 1  Początek](ODCINEK1_PL.md)

[ODCINEK 2  Niech to się chociaż skompiluje](ODCINEK2_PL.md)

[ODCINEK 3  Robimy pierwszy test](ODCINEK3_PL.md)

[ODCINEK 4  Wszystko działa](ODCINEK4_PL.md)

[ODCINEK 5  Naprawiamy](ODCINEK5_PL.md)

[ODCINEK 6  Konkretny refaktoring](ODCINEK6_PL.md)