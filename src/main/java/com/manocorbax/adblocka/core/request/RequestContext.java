package com.manocorbax.adblocka.core.request;

import java.net.Socket;
import java.util.Arrays;
import java.util.Map;

public record RequestContext(Socket clientSocket, String method, String host, int port, String path,
                             Map<String, String> headers, byte[] body) {

    @Override
    public String toString() {
        return "RequestContext{" +
                "clientSocket=" + clientSocket +
                ", method='" + method + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", path='" + path + '\'' +
                ", headers=" + headers +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
