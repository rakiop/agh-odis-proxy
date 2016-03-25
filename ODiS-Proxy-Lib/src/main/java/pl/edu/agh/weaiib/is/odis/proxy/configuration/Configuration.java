package pl.edu.agh.weaiib.is.odis.proxy.configuration;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

@Root(name = "configurations")
public class Configuration {

    @ElementList(inline = true, entry = "configuration")
    private List<ConfigurationEntry> configurations;

    public Configuration(){
        this.configurations = new LinkedList<>();
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

    public static class ConfigurationSerializer {

        public static void serialize(Configuration configuration, OutputStream stream) throws Exception {
            Serializer serializer = new Persister();
            stream.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n".getBytes());
            serializer.write(configuration, stream);
        }

        public static Configuration unserialize(InputStream stream) throws Exception {
            Serializer serializer = new Persister();
            return serializer.read(Configuration.class, stream);

        }

    }

}
