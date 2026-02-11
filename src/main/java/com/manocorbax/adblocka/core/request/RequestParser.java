package com.manocorbax.adblocka.core.request;

import java.net.Socket;

public class RequestParser {

    public RequestContext parse(String rawRequest, Socket client) {
        String[] lines = rawRequest.split("\r\n");
        String[] firstLine = lines[0].split(" ");

        String method = firstLine[0];

        // CONNECT
        if ("CONNECT".equalsIgnoreCase(method)) {
            String[] hostParts = firstLine[1].split(":");
            String host = hostParts[0];
            int port = Integer.parseInt(hostParts[1]);

            return new RequestContext(rawRequest, client, method, host, port);
        }

        // HTTP
        String host = extractHost(lines);
        return new RequestContext(rawRequest, client, method, host, 80);
    }

    private String extractHost(String[] lines) {
        for (String line : lines) {
            if (line.toLowerCase().startsWith("host:")) {
                return line.substring(5).trim();
            }
        }
        throw new IllegalStateException("Host header not found");
    }
}
