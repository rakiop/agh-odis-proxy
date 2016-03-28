package pl.edu.agh.weaiib.is.odis.proxy;

import org.junit.Test;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class ODiSProxyTestE2E {

    @Test
    public void basicApplicationTest() throws Exception {
        int serverPort = 8900;
        int socektPort = 8901;
        Configuration configuration = generateConfiguration(serverPort, socektPort);
        ODiSProxy application = new ODiSProxy();
        application.setConfiguration(configuration);

        application.loadProxies();
        application.startProxies();

        checkConnectionTo(serverPort);
        checkConnectionTo(socektPort);

        application.closeProxies();
    }

    private void checkConnectionTo(int port) throws IOException {
        Socket socket = new Socket(InetAddress.getByName("0.0.0.0"), port);

        assertTrue(socket.isBound());
    }

    private Configuration generateConfiguration(int serverPort, int socketPort) {
        Configuration configuration = new Configuration();

        ConfigurationEntry serverEntry = new ConfigurationEntry(serverPort, ListenerType.HTTP_SERVER);
        Filter blackListFilter = new Filter("pl.edu.agh.weaiib.is.odis.proxy.plugins.BlackListUrlsPlugin", FilterPlace.SERVER_CLIENT_TO_PROXY, 1, "00:00","23:59");
        Filter unexistFilter = new Filter("filter.that.does.not.exist", FilterPlace.SERVER_CLIENT_TO_PROXY, 2);
        Map<String, String> properties = new HashMap<String, String>();

        List<String> blackedUrls = new ArrayList<String>();
        blackedUrls.add("google.pl");
        blackedUrls.add("coachingsport.pl");

        properties.put("list", String.join(";",blackedUrls));
        blackListFilter.setParameters((Map) properties);

        serverEntry.addFilter(blackListFilter);
        serverEntry.addFilter(unexistFilter);
        configuration.addConfiguration(serverEntry);

        ConfigurationEntry socketEntry = new ConfigurationEntry(socketPort, ListenerType.SOCKET);
        configuration.addConfiguration(socketEntry);

        return configuration;
    }

}