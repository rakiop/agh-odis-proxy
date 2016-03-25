package pl.edu.agh.weaiib.is.odis.proxy;

import org.junit.Test;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.*;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExampleConfigurationGenerator {

    @Test
    public void generateConfiguration() throws JAXBException {
        Configuration configuration = new Configuration();

        ConfigurationEntry entry = new ConfigurationEntry("00:00","23:59",8099, ListenerType.HTTP_SERVER);
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
