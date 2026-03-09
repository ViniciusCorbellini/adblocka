package com.manocorbax.adblocka.core.request;

import java.net.Socket;

// becomes an "adapter"
public class RequestParser {

    public RequestContext buildContext(ParsedHttpRequest request, Socket client) {
        String[] firstLine = request.getRequestLine().split(" ");

        String method = firstLine[0];
        String uri = firstLine[0];
        String host = request.getHeaders().get("host").split(":")[0];

        return new RequestContext(
                client,
                method,
                host,
                ("CONNECT".equalsIgnoreCase(method) ? 443 : 80),
                uri,
                request.getHeaders(),
                request.getBody()
        );
    }
}
