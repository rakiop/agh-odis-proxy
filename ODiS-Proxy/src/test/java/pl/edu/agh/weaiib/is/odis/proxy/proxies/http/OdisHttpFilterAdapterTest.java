package pl.edu.agh.weaiib.is.odis.proxy.proxies.http;

import io.netty.handler.codec.http.HttpObject;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class OdisHttpFilterAdapterTest {

    @Test
    public void doNotFilterIfResponseIsNotAggregated(){
        HttpObject object = mock(HttpObject.class);

        OdisHttpFilterAdapter adapter = new OdisHttpFilterAdapter(null, null, null);

        HttpObject response = adapter.proxyToClientResponse(object);

        assertEquals(object, response);

    }

}
