package pl.edu.agh.weaiib.is.odis.proxy.helpers;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class URIHelperTest {

    @Test
    public void quickReturnWithValidUrl() throws URISyntaxException {
        String url = "http://google.pl";
        URI uri = URIHelper.getURIFromUrl(url);

        assertNotNull(uri);
        assertEquals("google.pl", uri.getHost());
    }

    @Test
    public void parseUrlWithWrongChars() throws URISyntaxException {
        String url = "http://google.pl/?x=ç&c[1]=p";
        URI uri = URIHelper.getURIFromUrl(url);

        assertNotNull(uri);
        assertEquals("google.pl", uri.getHost());
    }
}
