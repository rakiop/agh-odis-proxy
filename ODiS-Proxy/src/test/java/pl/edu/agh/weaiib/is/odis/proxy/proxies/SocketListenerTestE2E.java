package pl.edu.agh.weaiib.is.odis.proxy.proxies;

import java.net.Socket;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Created by SG0222582 on 3/24/2016.
 */
public class SocketListenerTestE2E {

    private static final int testPort = 8989;

    @Test
    public void tryConnectToSocket() throws Exception {
        SocketListener listener = new SocketListener(testPort);
        listener.start();
        assertTrue(listener.isStarted());

        Socket socket = new Socket("0.0.0.0", testPort);

        assertTrue(socket.isBound());

        socket.close();

        listener.close();
    }

}