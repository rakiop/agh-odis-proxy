package pl.edu.agh.weaiib.is.odis.proxy;

import org.junit.Test;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    private Configuration getValidConfigurationMock(){
        Configuration configuration = mock(Configuration.class);
        ConfigurationEntry entry = mock(ConfigurationEntry.class);
        Filter filter = mock(Filter.class);

        Map<String, Object> filterProperties = new HashMap<>();
        filterProperties.put("list", "google.com;google.pl");

        List<Filter> filterList = new LinkedList<Filter>();
        filterList.add(filter);

        List<ConfigurationEntry> configurations = new LinkedList<>();
        configurations.add(entry);

        when(configuration.getConfigurations()).thenReturn(configurations);
        when(entry.getPort()).thenReturn(8099);
        when(entry.getType()).thenReturn(ListenerType.HTTP_SERVER);
        when(entry.getFilters()).thenReturn(filterList);

        when(filter.getFilterName()).thenReturn("pl.edu.agh.weaiib.is.odis.proxy.plugins.BlackListUrlsPlugin");
        when(filter.getPlace()).thenReturn(FilterPlace.SERVER_CLIENT_TO_PROXY);
        when(filter.getPriority()).thenReturn(1);
        when(filter.getParameters()).thenReturn(filterProperties);

        when(filter.getTimeFrom()).thenReturn("00:00");
        when(filter.getTimeTo()).thenReturn("23:59");

        return configuration;
    }

}