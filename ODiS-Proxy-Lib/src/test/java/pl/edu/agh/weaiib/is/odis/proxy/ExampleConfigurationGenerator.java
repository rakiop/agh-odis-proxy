package pl.edu.agh.weaiib.is.odis.proxy;

import org.junit.Ignore;
import org.junit.Test;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExampleConfigurationGenerator {

    @Test
    @Ignore
    public void generateConfiguration() throws Exception {
        Configuration configuration = new Configuration();

        ConfigurationEntry entry = new ConfigurationEntry("00:00","23:59",8099, ListenerType.HTTP_SERVER);
        Filter blackListFilter = new Filter("pl.edu.agh.weaiib.is.odis.proxy.plugins.BlackListUrlsPlugin", FilterPlace.SERVER_CLIENT_TO_PROXY, 1);

        Map<String, Object> properties = new HashMap<>();

        List<String> blackedUrls = new ArrayList<>();
        blackedUrls.add("google.pl");
        blackedUrls.add("coachingsport.pl");

        properties.put("list", new SerializableList<>(blackedUrls));
        blackListFilter.setParameters((Map) properties);

        entry.addFilter(blackListFilter);
        configuration.addConfiguration(entry);

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Configuration.ConfigurationSerializer.serialize(configuration, os);

        System.out.println(os.toString());
    }

}
