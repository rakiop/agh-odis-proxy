package pl.edu.agh.weaiib.is.odis.proxy.configuration;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Created by SG0222582 on 3/24/2016.
 */
public class ConfigurationMarshallerTestIT {

    private static final String xmlValue = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + "<configurations>\n" + "    <configurations>\n" + "        <filters/>\n"
        + "        <port>8080</port>\n" + "        <timeFrom>23:59</timeFrom>\n" + "        <timeTo>00:00</timeTo>\n" + "        <type>HTTP_SERVER</type>\n" + "    </configurations>\n"
        + "</configurations>";

    private static final String fromTime = "23:59";
    private static final String toTime = "00:00";
    private static final int port = 8080;
    private static final ListenerType listenerType = ListenerType.HTTP_SERVER;
    @Test
    public void marshal() throws Exception {
        Configuration configuration = new Configuration();
        configuration.addConfiguration(new ConfigurationEntry(fromTime, toTime, port, listenerType));

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Configuration.ConfigurationMarshaller.marshal(configuration, os);

        String content = new String(os.toByteArray()).trim();

        assertEquals(content, xmlValue.trim());

    }

    @Test
    public void unmarshal() throws Exception {
        InputStream is = new ByteArrayInputStream(xmlValue.getBytes(StandardCharsets.UTF_8));

        Configuration configuration = Configuration.ConfigurationMarshaller.unmarshal(is);

        assertNotNull(configuration);
        assertNotNull(configuration.getConfigurations());
        assertFalse(configuration.getConfigurations().isEmpty());
        ConfigurationEntry entry  = configuration.getConfigurations().get(0);
        assertEquals(entry.getTimeFrom(), fromTime);
        assertEquals(entry.getTimeTo(), toTime);
        assertEquals(entry.getPort(), port);
        assertEquals(entry.getType(), listenerType);

    }
}