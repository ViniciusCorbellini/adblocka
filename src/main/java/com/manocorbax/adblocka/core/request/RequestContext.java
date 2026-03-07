package com.manocorbax.adblocka.core.request;

import java.net.Socket;
import java.util.Map;

public class RequestContext {

    // will be removed
    // private final String rawRequest;
    // ===============

    private final Socket clientSocket;

    private final String method;
    private final String host;
    private final int port;

    private final String path;
    private final Map<String, String> headers;
    private final byte[] body;

    public RequestContext(Socket clientSocket,
                          String method,
                          String host,
                          int port,
                          String path,
                          Map<String, String> headers,
                          byte[] body) {
        this.clientSocket = clientSocket;
        this.method = method;
        this.host = host;
        this.port = port;
        this.path = path;
        this.headers = headers;
        this.body = body;
    }

    public Socket getClientSocket() { return clientSocket; }
    public String getMethod() { return method; }
    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getPath() { return path; }
    public Map<String, String> getHeaders() { return headers; }
    public byte[] getBody() { return body; }
}
