package pl.edu.agh.weaiib.is.odis.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.Configuration;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.ConfigurationEntry;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.Filter;
import pl.edu.agh.weaiib.is.odis.proxy.proxies.Proxies;
import pl.edu.agh.weaiib.is.odis.proxy.proxies.ProxyServer;
import pl.edu.agh.weaiib.is.odis.proxy.proxies.SocketListener;

import java.io.*;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Main class of application
 */
public class ODiSProxy {

    /**
     * Main application logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ODiSProxy.class);

    /**
     * Start point of application
     * @param args          Arguments - first argument can be configuration file path
     * @throws Exception    If something unexpected happend :)
     */
    public static void main(String[] args) throws Exception {
        ODiSProxy application = new ODiSProxy();
        application.determineConfigurationFile(args);
        InputStream input = new FileInputStream(application.getConfigurationFile());
        application.setConfiguration(application.readConfiguration(input));
        application.loadProxies();
        try {
            application.startProxies();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            application.waitToClose(reader);
        } catch (Exception e) {
            LOGGER.error(String.format("Error occured: %s", e.getMessage()));
        }
        finally {
            application.closeProxies();
        }

        LOGGER.info("Shut down...");
    }

    /**
     * List of active proxies / listeners
     */
    private List<Proxies> proxies = new LinkedList<>();

    /**
     * Configuration file path
     */
    private String configurationFile;

    /**
     * Read configuration
     */
    private Configuration configurations;

    /**
     * Set configuration file path - default "configuration.xml" or first parameter from arguments
     * @param args  Applications arguments
     */
    public void determineConfigurationFile(String[] args){
        LOGGER.info(String.format("Application determineConfigurationFile from %s", System.getProperty("user.dir")));

        configurationFile = "configuration.xml";

        if(args.length > 0)
            configurationFile = args[0];

        LOGGER.info(String.format("Read configuration from: %s", configurationFile));

    }

    /**
     * Return configuration file path
     * @return  Configuration file path
     */
    public String getConfigurationFile(){
        return this.configurationFile;
    }

    /**
     * Read configuration from XML file
     * @param input Stream to read
     * @return      Configuration object or null on error
     */
    public Configuration readConfiguration(InputStream input){
        try{
            return Configuration.ConfigurationSerializer.unserialize(input);
        } catch (Exception e) {
            LOGGER.error(String.format("Error while reading configuration: %s", e.getMessage()));
        }
        return null;
    }

    /**
     * Inject Configuration
     * @param configuration Configuration value
     */
    public void setConfiguration(Configuration configuration){
        this.configurations = configuration;
    }

    /**
     * Read proxies / listeners from configuration object
     */
    public void loadProxies(){
        LOGGER.info("Creating proxies");
        proxies = new LinkedList<>();
        if(configurations == null)
            return;

        for(ConfigurationEntry configuration : configurations.getConfigurations()){
            Proxies proxy = null;
            switch (configuration.getType()){
                case HTTP_SERVER:
                    proxy = new ProxyServer(configuration.getPort());
                break;
                case SOCKET:
                    proxy = new SocketListener(configuration.getPort());
                break;
            }

            for(Filter filter : configuration.getFilters()){
                try{
                    pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter newFilter =  createFilter(filter);
                    newFilter.init();
                    proxy.addFilter(newFilter, filter.getPlace());
                } catch (ClassNotFoundException|InstantiationException|IllegalAccessException e) {
                    LOGGER.warn(String.format("Could not create filter %s : %s", filter.getFilterName(), e.getMessage()));
                }
            }
            proxies.add(proxy);
        }
    }

    /**
     * Read from input stream until [ENTER] is pressed
     * @param reader    Input stream
     */
    public void waitToClose(BufferedReader reader) {
        System.out.println("Pres [ENTER] to shut down...");
        String line = "";
        do{
            try {
                line = reader.readLine();
            } catch (IOException e) {
                LOGGER.warn(e.getMessage());
            }
        }while(!line.isEmpty());
    }

    /**
     * Start all proxies / listeners
     * @throws Exception    On unexpected error
     */
    public void startProxies() throws Exception {
        LOGGER.info("Start proxies");
        for(Proxies proxy : proxies){
            proxy.start();
        }
    }

    /**
     * Close all proxies / listeners
     * @throws Exception    On unexpected error
     */
    public void closeProxies() throws Exception {
        LOGGER.info("Closing proxies");
        for(Proxies proxy : proxies){
            proxy.close();
        }
    }

    /**
     * Create Filter object from its configuration
     * @param filter                    Filter Configuration
     * @return                          Filter object
     * @throws ClassNotFoundException   If Filter class name is not present
     * @throws IllegalAccessException   If initializer is not accessible
     * @throws InstantiationException   If initializer is not accessible
     */
    private pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter createFilter(Filter filter) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class filterClass = Class.forName(filter.getFilterName());
        pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter newFilter =  (pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter) filterClass.newInstance();
        newFilter.setPriority(filter.getPriority());
        newFilter.setParameters(filter.getParameters());
        String timeFrom = filter.getTimeFrom();
        if(timeFrom != null && !timeFrom.isEmpty())
            newFilter.setFilterFrom(LocalTime.parse(timeFrom));

        String timeTo = filter.getTimeTo();
        if(timeTo != null && !timeTo.isEmpty())
        newFilter.setFilterTo(LocalTime.parse(timeTo));
        return newFilter;
    }


}
