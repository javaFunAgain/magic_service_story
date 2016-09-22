package it.makes.me.angry.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public final class Input {
    public final String resourceName;

    public Input(String resourceName) {
        this.resourceName = resourceName;
    }

    public URI getURI() throws IOException {//TODO: oh no, no checked exceptions anymore
        try {
            final URL resource = getClass().getResource("/"+ this.resourceName);
            if ( resource !=  null) { //TODO: remove this if
                return resource.toURI();
            } else {
                throw new IOException("there is no "  + this.resourceName); //TODO: do not throw
            }

        } catch (URISyntaxException e) {
            throw new IOException(e); //TODO: do not throw, please
        }
    }
}
