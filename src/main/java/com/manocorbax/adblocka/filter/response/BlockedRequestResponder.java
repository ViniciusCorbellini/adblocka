package com.manocorbax.adblocka.filter.response;

import com.manocorbax.adblocka.core.request.RequestContext;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BlockedRequestResponder {
    public void respond(RequestContext context, FilterDecision decision, String filterName) throws IOException {
        String reason = decision.reason();
        String body = "Request blocked by " + filterName + " filter: " + reason + "\n";

        String statusLine = "HTTP/1.1 403 Forbidden";

        String response = statusLine + "\r\n"
                + "Content-Type: text/plain; charset=UTF-8\r\n"
                + "Connection: close\r\n"
                + "Content-Length: " + body.getBytes(StandardCharsets.UTF_8).length + "\r\n"
                + "\r\n"
                + body;

        OutputStream out = context.getClientSocket().getOutputStream();
        out.write(response.getBytes(StandardCharsets.UTF_8));
        out.flush();
        context.getClientSocket().close();
    }
}
