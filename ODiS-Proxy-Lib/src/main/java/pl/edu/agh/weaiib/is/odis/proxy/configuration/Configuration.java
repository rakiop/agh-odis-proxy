package pl.edu.agh.weaiib.is.odis.proxy.configuration;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "configurations")
public class Configuration {

    private List<ConfigurationEntry> configurations;

    public Configuration(){
        this.configurations = new LinkedList<ConfigurationEntry>();
    }

    public void addConfiguration(ConfigurationEntry entry){
        if(entry == null)
            throw new IllegalArgumentException("Configuration entry cannot be null.");
        this.configurations.add(entry);
    }

    public List<ConfigurationEntry> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<ConfigurationEntry> configurations) {
        if(configurations == null)
            throw new IllegalArgumentException("Configurations list cannot be null");
        this.configurations = configurations;
    }

    public static class ConfigurationMarshaller{

        private static JAXBContext getContext() throws JAXBException {
            return JAXBContext.newInstance(Configuration.class,ConfigurationEntry.class, Filter.class, ArrayList.class);
        }

        public static void marshal(Configuration configuration, OutputStream stream) throws JAXBException {
            Marshaller marshaller = getContext().createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(configuration, stream);
        }

        public static Configuration unmarshal(InputStream stream) throws JAXBException {
            Unmarshaller unmarshaller = getContext().createUnmarshaller();
            return (Configuration) unmarshaller.unmarshal(stream);
        }

    }

}
