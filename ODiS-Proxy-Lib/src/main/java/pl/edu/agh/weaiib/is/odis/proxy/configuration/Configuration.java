package pl.edu.agh.weaiib.is.odis.proxy.configuration;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Configuration root element
 * that aggregates all configuration
 */
@Root(name = "configurations")
public class Configuration {

    /**
     * List of configurations of HttpServers / SocketListeners
     */
    @ElementList(inline = true, entry = "configuration")
    private List<ConfigurationEntry> configurations;

    /**
     * Default constructor
     * Sets {@code configurations} with empty {@link java.util.LinkedList}
     */
    public Configuration(){
        this.configurations = new LinkedList<>();
    }

    /**
     * Add configuration to list
     * @param entry Configuration entry
     * @throws IllegalArgumentException If configuration entry is null
     */
    public void addConfiguration(ConfigurationEntry entry){
        if(entry == null)
            throw new IllegalArgumentException("Configuration entry cannot be null.");
        this.configurations.add(entry);
    }

    /**
     * Get lists of configurations
     * @return  List of configurations
     */
    public List<ConfigurationEntry> getConfigurations() {
        return configurations;
    }

    /**
     * Set configuration list of not empty
     * @param configurations    Configuration List
     * @throws java.lang.IllegalArgumentException   If list is null
     */
    public void setConfigurations(List<ConfigurationEntry> configurations) {
        if(configurations == null)
            throw new IllegalArgumentException("Configurations list cannot be null");
        this.configurations = configurations;
    }

    /**
     * Helper class to serialize to/from XML
     */
    public static class ConfigurationSerializer {

        /**
         * Private constructor to prevent initialization
         */
        private ConfigurationSerializer() { }

        /**
         * Serialize configuration to XML
         * @param configuration Configuration
         * @param stream        Stream to output
         * @throws Exception    If serialization fails or on stream problems
         */
        public static void serialize(Configuration configuration, OutputStream stream) throws Exception {
            Serializer serializer = new Persister();
            stream.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n".getBytes());
            serializer.write(configuration, stream);
        }

        /**
         * Deserialization of XML to Configurations object
         * @param stream        Stream to read XML
         * @return              Configuration object
         * @throws Exception    On stream or deserialization error
         */
        public static Configuration unserialize(InputStream stream) throws Exception {
            Serializer serializer = new Persister();
            return serializer.read(Configuration.class, stream);

        }

    }

}
