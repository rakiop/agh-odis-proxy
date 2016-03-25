package pl.edu.agh.weaiib.is.odis.proxy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.Configuration;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.ConfigurationEntry;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.Filter;
import pl.edu.agh.weaiib.is.odis.proxy.proxies.Proxies;
import pl.edu.agh.weaiib.is.odis.proxy.proxies.ProxyServer;
import pl.edu.agh.weaiib.is.odis.proxy.proxies.SocketListener;

public class ODiSProxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(ODiSProxy.class);

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

    private List<Proxies> proxies = new LinkedList<>();

    private String configurationFile;

    private Configuration configurations;

    public void determineConfigurationFile(String[] args){
        LOGGER.info(String.format("Application determineConfigurationFile from %s", System.getProperty("user.dir")));

        configurationFile = "configuration.xml";

        if(args.length > 0)
            configurationFile = args[0];

        LOGGER.info(String.format("Read configuration from: %s", configurationFile));

    }

    public String getConfigurationFile(){
        return this.configurationFile;
    }

    public Configuration readConfiguration(InputStream input){
        try{
            return Configuration.ConfigurationSerializer.unserialize(input);
        } catch (Exception e) {
            LOGGER.error(String.format("Error while reading configuration: %s", e.getMessage()));
        }
        return null;
    }

    public void setConfiguration(Configuration configuration){
        this.configurations = configuration;
    }

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
            if(proxy != null)
                proxies.add(proxy);
        }
    }

    public void waitToClose(BufferedReader reader) {
        System.out.println("Pres [ENTER] to shut down...");
        String line = "";
        do{
            try {
                line = reader.readLine();
            } catch (IOException e) { }
        }while(!line.isEmpty());
    }

    public void startProxies() throws Exception {
        LOGGER.info("Start proxies");
        for(Proxies proxy : proxies){
            proxy.start();
        }
    }

    public void closeProxies() throws Exception {
        LOGGER.info("Closing proxies");
        for(Proxies proxy : proxies){
            proxy.close();
        }
    }

    private pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter createFilter(Filter filter) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class filterClass = Class.forName(filter.getFilterName());
        pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter newFilter =  (pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter) filterClass.newInstance();
        newFilter.setPriority(filter.getPriority());
        newFilter.setParameters(filter.getParameters());
        return newFilter;
    }


}
