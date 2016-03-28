package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import java.net.Socket;

public abstract class ODiSSocketFilter extends Filter {

    public abstract boolean testSocketRequest(Socket client);

}
