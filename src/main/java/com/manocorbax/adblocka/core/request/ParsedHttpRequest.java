package com.manocorbax.adblocka.core.request;

import java.util.Map;

public class ParsedHttpRequest {

    private final String requestLine;
    private final Map<String, String> headers;
    private final byte[] body;

    public ParsedHttpRequest(String requestLine,
                             Map<String,String> headers,
                             byte[] body) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
    }

    public String getRequestLine() { return requestLine; }

    public Map<String,String> getHeaders() { return headers; }

    public byte[] getBody() { return body; }

}
