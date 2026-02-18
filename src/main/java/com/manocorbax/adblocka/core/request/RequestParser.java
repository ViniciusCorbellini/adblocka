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
        HostAndPort hostAndPort = extractHostAndPort(lines);
        return new RequestContext(rawRequest, client, method, hostAndPort.host(), hostAndPort.port());
    }

    private HostAndPort extractHostAndPort(String[] lines) {
        for (String line : lines) {
            if (line.toLowerCase().startsWith("host:")) {
                String hostHeader = line.substring(5).trim();

                if (hostHeader.contains(":")) {
                    String[] hostParts = hostHeader.split(":");
                    return new HostAndPort(hostParts[0], Integer.parseInt(hostParts[1]));
                }

                return new HostAndPort(hostHeader, 80);
            }
        }

        throw new IllegalStateException("Host header not found");
    }

    private record HostAndPort(String host, int port) {
    }
}
