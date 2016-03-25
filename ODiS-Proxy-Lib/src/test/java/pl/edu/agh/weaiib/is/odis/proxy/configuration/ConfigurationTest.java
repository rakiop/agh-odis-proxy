package pl.edu.agh.weaiib.is.odis.proxy.configuration;

import java.util.LinkedList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ConfigurationTest {

    @Test
    public void addConfiguration(){
        ConfigurationEntry entry = mock(ConfigurationEntry.class);

        Configuration configuration = new Configuration();
        configuration.addConfiguration(entry);

        assertTrue(configuration.getConfigurations().contains(entry));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullConfigurationThrowsException(){
        Configuration configuration = new Configuration();
        configuration.addConfiguration(null);
    }

    @Test
    public void setConfiguration(){
        LinkedList<ConfigurationEntry> configurations = new LinkedList<ConfigurationEntry>();

        Configuration configuration = new Configuration();
        configuration.setConfigurations(configurations);

        assertEquals(configuration.getConfigurations(), configurations);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullConfigurationsThrowsException(){
        Configuration configuration = new Configuration();
        configuration.setConfigurations(null);
    }

}