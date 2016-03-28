package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

public class SocketConnectionLogger extends ODiSSocketFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketConnectionLogger.class);

    private String fileName;

    @Override
    public boolean testSocketRequest(Socket client) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            File file = new File(fileName);
            if (!file.exists())
            {
                    file.createNewFile();
            }

            if(!file.canWrite()){
                LOGGER.error(String.format("File has write protection: %s", fileName));
                return true;
            }

            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            bw.write(String.format("Connection to socket %s:%s from %s\n", client.getInetAddress(), client.getLocalPort(), client.getRemoteSocketAddress()));
            bw.close();
            fw.close();

        } catch (IOException e) {
            LOGGER.warn(String.format("Can not write to log %s - exception: %s", fileName, e.getMessage()));
        }
        finally {
            try{
                if(bw != null)
                    bw.close();
                if(fw != null)
                    fw.close();

            }catch(IOException e){}
        }

        return true;
    }

    @Override
    public void init() {
        String fileName = (String) parameters.get("file");
        if(fileName == null)
            throw new IllegalArgumentException("This Plugin needs file to write");

        this.fileName = fileName;
    }
}
