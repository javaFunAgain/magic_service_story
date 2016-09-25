package it.makes.me.angry;

import it.makes.me.angry.data.Input;

public class Runner {
    public static void main(final  String ... args) {
        final MagicService service = new MagicService();
        System.out.println(service.performComplexCalculations( new Input("real_data.csv")));
    }
}
