package pl.edu.agh.weaiib.is.odis.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.Configuration;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.ConfigurationEntry;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.Filter;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.ListenerType;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        Map<String, String> filterProperties = new HashMap<>();
        filterProperties.put("list", "google.com;google.pl");

        List<Filter> filterList = new LinkedList<Filter>();
        filterList.add(filter);

        List<ConfigurationEntry> configurations = new LinkedList<>();
        configurations.add(entry);

        when(configuration.getConfigurations()).thenReturn(configurations);

        when(entry.getTimeFrom()).thenReturn("12AM");
        when(entry.getTimeTo()).thenReturn("12PM");
        when(entry.getPort()).thenReturn(8099);
        when(entry.getType()).thenReturn(ListenerType.HTTP_SERVER);
        when(entry.getFilters()).thenReturn(filterList);

        when(filter.getFilterName()).thenReturn("pl.edu.agh.weaiib.is.odis.proxy.plugins.BlackListUrlsPlugin");
        when(filter.getPlace()).thenReturn(FilterPlace.SERVER_CLIENT_TO_PROXY);
        when(filter.getPriority()).thenReturn(1);
        when(filter.getParameters()).thenReturn(filterProperties);

        return configuration;
    }

}