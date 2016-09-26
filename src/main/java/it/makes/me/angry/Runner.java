package it.makes.me.angry;

import it.makes.me.angry.data.Input;
import it.makes.me.angry.processors.AverageParticipantsProcessor;
import it.makes.me.angry.producer.ResourceDataProducer;

public class Runner {
    public static void main(final  String ... args) {
        final MagicService service = new MagicService(new ResourceDataProducer(),
                new AverageParticipantsProcessor());
        System.out.println(service.performComplexCalculations( new Input("real_data.csv"))
                .map(out->out.value)
                .getOrElseGet( problem -> "Problem:" + problem.toString()));
    }
}
