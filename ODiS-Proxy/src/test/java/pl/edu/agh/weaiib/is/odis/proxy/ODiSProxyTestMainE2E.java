package pl.edu.agh.weaiib.is.odis.proxy;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ODiSProxyTestMainE2E {

    @Test
    public void mainRunsCorrectly() throws Exception {
        InputStream is = new ByteArrayInputStream("\n".getBytes());

        System.setIn(is);

        ODiSProxy.main(new String[]{"ODiS-Proxy\\src\\main\\resources\\configuration.xml"});

    }

}
