package it.makes.me.angry.processors;

import it.makes.me.angry.data.Input;
import it.makes.me.angry.data.RawData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class DataCollector {
    public RawData collectData(final Input input) throws IOException{
        return new RawData(new String(Files.readAllBytes(Paths.get(input.getURI()))));
    }
}
