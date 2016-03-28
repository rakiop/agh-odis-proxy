package pl.edu.agh.weaiib.is.odis.proxy;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ODiSProxyTest {

    @Test
    public void ifNoArgumentIsPassedReadFromDefault(){
        String configurationFile = "configuration.xml";
        ODiSProxy proxy = new ODiSProxy();
        proxy.determineConfigurationFile(new String[]{});

        assertEquals(configurationFile, proxy.getConfigurationFile());
    }

    @Test
    public void readConfigurationFileNameFromArgs(){
        String configurationFile = "Target/configuration.xml";
        ODiSProxy proxy = new ODiSProxy();
        proxy.determineConfigurationFile(new String[]{configurationFile});

        assertEquals(configurationFile, proxy.getConfigurationFile());
    }

    @Test
    public void loadProxiesNullExceptionProved(){
        ODiSProxy proxy = new ODiSProxy();
        proxy.loadProxies();
    }

    @Test
    public void waitToCloseWaitsForEmptyLine() throws IOException {
        ODiSProxy proxy = new ODiSProxy();
        BufferedReader br = mock(BufferedReader.class);

        when(br.readLine()).thenReturn("line","line2","");

        proxy.waitToClose(br);

        verify(br, times(3)).readLine();
    }

    @Test
    public void waitToCloseReturnSilentlyOnException() throws IOException {
        ODiSProxy proxy = new ODiSProxy();
        BufferedReader br = mock(BufferedReader.class);

        when(br.readLine()).thenThrow(IOException.class);

        proxy.waitToClose(br);

        verify(br, times(1)).readLine();
    }
}