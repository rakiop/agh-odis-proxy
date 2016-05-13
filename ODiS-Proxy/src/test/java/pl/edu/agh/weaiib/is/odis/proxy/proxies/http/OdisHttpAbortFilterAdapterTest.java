package pl.edu.agh.weaiib.is.odis.proxy.proxies.http;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OdisHttpAbortFilterAdapterTest {

    @Test
    public void validateForbiddenResponse(){
        OdisHttpAbortFilterAdapter adapter = new OdisHttpAbortFilterAdapter(null, null, null);
        HttpResponse response = adapter.clientToProxyRequest(null);

        assertEquals(response.getStatus(), HttpResponseStatus.FORBIDDEN);
    }

}