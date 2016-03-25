package pl.edu.agh.weaiib.is.odis.proxy.proxies;

import java.io.IOException;
import java.net.Socket;
import org.junit.Test;
import org.littleshoot.proxy.mitm.RootCertificateException;
import static org.junit.Assert.assertTrue;

public class ProxyServerTestE2E {

    private static final int testPort = 8989;

    @Test
    public void connectToServer() throws RootCertificateException, IOException {
        ProxyServer server = new ProxyServer(testPort);
        server.start();

        assertTrue(server.isStarted());

        Socket socket = new Socket(server.getAddress().getAddress(), testPort);

        assertTrue(socket.isBound());

        socket.close();
        server.close();

    }

}