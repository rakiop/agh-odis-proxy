package pl.edu.agh.weaiib.is.odis.proxy.configuration;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigurationSerializerTestIT {

    private static final String xmlValue = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<configurations>\n" +
            "   <configuration type=\"HTTP_SERVER\" port=\"8080\"/>\n" +
            "</configurations>";

    private static final int port = 8080;
    private static final ListenerType listenerType = ListenerType.HTTP_SERVER;


    @Test
    public void serialize() throws Exception {
        Configuration configuration = new Configuration();
        configuration.addConfiguration(new ConfigurationEntry(port, listenerType));

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Configuration.ConfigurationSerializer.serialize(configuration, os);

        String content = new String(os.toByteArray()).trim();

        assertEquals(content, xmlValue.trim());
    }

    @Test
    public void unserialize() throws Exception {
        InputStream is = new ByteArrayInputStream(xmlValue.getBytes(StandardCharsets.UTF_8));

        Configuration configuration = Configuration.ConfigurationSerializer.unserialize(is);

        assertNotNull(configuration);
        assertNotNull(configuration.getConfigurations());
        assertFalse(configuration.getConfigurations().isEmpty());
        ConfigurationEntry entry  = configuration.getConfigurations().get(0);
        assertEquals(entry.getPort(), port);
        assertEquals(entry.getType(), listenerType);
    }

    @Test(expected = Exception.class)
    public void serializationThrowsException() throws Exception {
        Configuration conf = mock(Configuration.class);
        when(conf.getConfigurations()).thenReturn(null);

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Configuration.ConfigurationSerializer.serialize(conf, os);
    }

    @Test(expected = IllegalAccessException.class)
    public void canNotMakeInstance() throws IllegalAccessException, InstantiationException {
        Configuration.ConfigurationSerializer.class.newInstance();
    }
}