package pl.edu.agh.weaiib.is.odis.proxy.helpers;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
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

    @Test
    public void parseWrongUri() throws URISyntaxException {
        String url = "httpsd://googles.pl: #/?x=ç&c[1]=p";
        URI uri = URIHelper.getURIFromUrl(url);

        assertNotNull(uri);
        assertNull(uri.getHost());
    }

    @Test
    public void parseNormalUrl(){
        String url = "http://google.pl";
        String host = URIHelper.getHost(url);

        assertEquals("google.pl", host);
    }

    @Test
    public void parseWWWUrl(){
        String url = "http://www.google.pl";
        String host = URIHelper.getHost(url);

        assertEquals("google.pl", host);
    }

    @Test
    public void parseIpUrl(){
        String url = "http://8.8.8.8";
        String host = URIHelper.getHost(url);

        assertEquals("google-public-dns-a.google.com", host);
    }

    @Test
    public void parseWrongIpUrl(){
        String url = "http://0.1.2.-1";
        String host = URIHelper.getHost(url);

        assertEquals("0.1.2.-1", host);
    }

    @Test
    public void parseNonFoundableIpUrl(){
        String url = "http://0.1.0.1";
        String host = URIHelper.getHost(url);

        assertEquals("0.1.0.1", host);
    }
}
