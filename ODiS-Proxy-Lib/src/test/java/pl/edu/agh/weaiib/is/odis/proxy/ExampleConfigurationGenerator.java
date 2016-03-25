package pl.edu.agh.weaiib.is.odis.proxy;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.junit.Test;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.Configuration;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.ConfigurationEntry;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.Filter;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.ListenerType;

public class ExampleConfigurationGenerator {

    @Test
    public void generateConfiguration() throws JAXBException {
        Configuration configuration = new Configuration();

        ConfigurationEntry entry = new ConfigurationEntry("12AM","12PM",8099, ListenerType.HTTP_SERVER);
        Filter blackListFilter = new Filter("pl.edu.agh.weaiib.is.odis.proxy.plugins.BlackListUrlsPlugin", FilterPlace.SERVER_CLIENT_TO_PROXY, 1);

        Map<String, String> properties = new HashMap<String, String>();

        List<String> blackedUrls = new ArrayList<String>();
        blackedUrls.add("google.pl");
        blackedUrls.add("coachingsport.pl");

        properties.put("list", String.join(";",blackedUrls));
        blackListFilter.setParameters((Map) properties);

        entry.addFilter(blackListFilter);
        configuration.addConfiguration(entry);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Configuration.ConfigurationMarshaller.marshal(configuration, os);

        System.out.println(os.toString());


    }

}
