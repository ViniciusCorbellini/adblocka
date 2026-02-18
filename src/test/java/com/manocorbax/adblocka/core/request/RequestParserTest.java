package com.manocorbax.adblocka.core.request;

import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestParserTest {

    private final RequestParser parser = new RequestParser();

    @Test
    void shouldParseHttpHostPortFromHostHeader() {
        String raw = "GET http://example.com/path HTTP/1.1\r\n"
                + "Host: example.com:8081\r\n"
                + "\r\n";

        RequestContext context = parser.parse(raw, new Socket());

        assertEquals("GET", context.getMethod());
        assertEquals("example.com", context.getHost());
        assertEquals(8081, context.getPort());
    }

    @Test
    void shouldParseConnectHostAndPort() {
        String raw = "CONNECT api.example.com:443 HTTP/1.1\r\n"
                + "Host: api.example.com:443\r\n"
                + "\r\n";

        RequestContext context = parser.parse(raw, new Socket());

        assertEquals("CONNECT", context.getMethod());
        assertEquals("api.example.com", context.getHost());
        assertEquals(443, context.getPort());
    }
}
