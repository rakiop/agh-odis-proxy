package pl.edu.agh.weaiib.is.odis.proxy.proxies;

import org.junit.Test;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.net.Socket;

import static org.junit.Assert.assertTrue;

public class SocketListenerTestE2E {

    private static final int testPort = 8989;

    @Test
    public void tryConnectToSocket() throws Exception {
        SocketListener listener = new SocketListener(testPort);
        listener.start();
        assertTrue(listener.isStarted());

        Socket socket = new Socket("0.0.0.0", testPort);

        assertTrue(socket.isBound());
        assertTrue(socket.isConnected());

        socket.close();
        listener.close();
    }

    @Test(expected = EOFException.class)
    public void socketIsClosedImmediately() throws Exception {
        SocketListener listener = new SocketListener(testPort);
        listener.start();
        assertTrue(listener.isStarted());

        Socket socket = new Socket("0.0.0.0", testPort);

        assertTrue(socket.isBound());
        assertTrue(socket.isConnected());

        Thread.sleep(1000);

        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        // Exception is thrown if server socket is closed
        ois.readObject();

    }

}