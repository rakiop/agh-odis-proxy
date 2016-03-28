package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import com.google.common.io.Files;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.FileSystemException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SocketConnectionLoggerTestIT {

    private static final String fileName = String.format("test-%s-%s.txt", SocketConnectionLoggerTestIT.class.getCanonicalName(), LocalTime.now().toSecondOfDay());

    @After
    public void removeTestFile() throws FileSystemException {
        File file = new File(fileName);

        if(file.exists()){
            if(!file.delete()){
                throw new FileSystemException("Can not delete file");
            }
        }
    }

    @Test
    public void loggerWritesToFile(){

        Map<String, Object> properties = new HashMap<>();
        properties.put("file", fileName);

        Socket socket = mock(Socket.class);

        when(socket.getInetAddress()).thenReturn(null);
        when(socket.getLocalPort()).thenReturn(0);
        when(socket.getRemoteSocketAddress()).thenReturn(null);

        SocketConnectionLogger logger = new SocketConnectionLogger();
        logger.setParameters(properties);

        logger.testSocketRequest(socket);

        assertEquals(getLinesNumber(), 1);
    }

    @Test
    public void loggerAppendsToFile(){

        Map<String, Object> properties = new HashMap<>();
        properties.put("file", fileName);

        Socket socket = mock(Socket.class);

        when(socket.getInetAddress()).thenReturn(null);
        when(socket.getLocalPort()).thenReturn(0);
        when(socket.getRemoteSocketAddress()).thenReturn(null);

        SocketConnectionLogger logger = new SocketConnectionLogger();
        logger.setParameters(properties);

        logger.testSocketRequest(socket);
        logger.testSocketRequest(socket);

        assertEquals(getLinesNumber(), 2);
    }

    @Test
    public void multithreadingWriteToFile(){

        Map<String, Object> properties = new HashMap<>();
        properties.put("file", fileName);

        SocketConnectionLogger logger = new SocketConnectionLogger();
        logger.setParameters(properties);

        int threadsNumber = 1000;

        List<Thread> threads = new LinkedList<>();
        for(int i=0; i< threadsNumber; i++){
            threads.add(new Thread(() -> {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {

                }
                Socket socket = mock(Socket.class);

                when(socket.getInetAddress()).thenReturn(null);
                when(socket.getLocalPort()).thenReturn(0);
                when(socket.getRemoteSocketAddress()).thenReturn(null);
                logger.testSocketRequest(socket);
            }));
        }

        for(Thread t : threads){
            t.start();
        }

        long counter;
        do{
            counter = threads.stream().filter(thread -> !thread.isAlive()).count();
        }while(counter != threadsNumber);

        assertEquals(getLinesNumber(), threadsNumber);
    }


    public int getLinesNumber(){
        File file = new File(fileName);
        if(!file.exists()) return -1;

        try {
            List<String> lines = Files.readLines(file, Charset.defaultCharset());
            return lines.size();
        } catch (IOException e) {

        }

        return -1;
    }




}
