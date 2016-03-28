package pl.edu.agh.weaiib.is.odis.proxy.configuration;

/**
 * Place to inject filter / plugin
 */
public enum FilterPlace {
    /**
     * When request comes to proxy
     */
    SERVER_CLIENT_TO_PROXY,

    /**
     * When proxy is about to send response to client
     */
    SERVER_PROXY_TO_CLIENT,

    /**
     * When socket accept connection
     */
    SOCKET_BEFORE
}
