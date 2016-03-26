package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class SocketConnectionLoggerTest {

    @Test
    public void settingParametrsCallInitAndReadFileName(){
        Map<String, Object> properties = mock(HashMap.class);

        when(properties.get("file")).thenReturn("filename.txt");

        SocketConnectionLogger logger = new SocketConnectionLogger();
        logger.setParameters(properties);

        verify(properties, times(1)).get("file");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ifFileNameIsNotPresentThrowException(){
        Map<String, Object> properties = mock(HashMap.class);

        when(properties.get("file")).thenReturn(null);

        SocketConnectionLogger logger = new SocketConnectionLogger();
        logger.setParameters(properties);
    }

}
