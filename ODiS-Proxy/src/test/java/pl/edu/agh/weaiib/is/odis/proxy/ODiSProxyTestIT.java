package pl.edu.agh.weaiib.is.odis.proxy;

import java.io.InputStream;
import org.junit.Test;
import static org.mockito.Mockito.mock;

public class ODiSProxyTestIT {

    @Test
    public void configurationFileNotFoundNoException(){
        ODiSProxy proxy = new ODiSProxy();

        InputStream is = mock(InputStream.class);
        proxy.readConfiguration(is);
    }

}