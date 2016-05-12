package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import java.net.Socket;

/**
 * Socket filter / plugin interface
 */
public abstract class ODiSSocketFilter extends Filter {

    /**
     * Do something with socket before closing connection
     * @param client    Client socket
     * @return          Unimportant value
     */
    public abstract FilterResponse testSocketRequest(Socket client);

}
