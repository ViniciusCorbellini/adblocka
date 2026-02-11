package com.manocorbax.adblocka.core.request;

import java.net.Socket;

public class RequestContext {

    private final String rawRequest;
    private final Socket clientSocket;
    private final String method;
    private final String host;
    private final int port;

    public RequestContext(String rawRequest, Socket clientSocket,
                          String method, String host, int port) {
        this.rawRequest = rawRequest;
        this.clientSocket = clientSocket;
        this.method = method;
        this.host = host;
        this.port = port;
    }

    public String getRawRequest() { return rawRequest; }
    public Socket getClientSocket() { return clientSocket; }
    public String getMethod() { return method; }
    public String getHost() { return host; }
    public int getPort() { return port; }
}
