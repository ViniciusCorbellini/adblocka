package com.manocorbax.adblocka.core.handler;

import com.manocorbax.adblocka.core.request.RequestContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpHandler implements RequestHandler{

    @Override
    public boolean supports(RequestContext context) {
        return !"CONNECT".equalsIgnoreCase(context.method());
    }

    @Override
    public void handle(RequestContext context) throws Exception {
        Socket clientSocket = context.clientSocket();
        Socket serverSocket = new Socket(context.host(), context.port());

        try {
            OutputStream clientOut = clientSocket.getOutputStream();

            InputStream serverIn = serverSocket.getInputStream();
            OutputStream serverOut = serverSocket.getOutputStream();

            String headers = context.headers()
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + ":" + e.getValue())
                    .reduce("", (a, b) -> a + "\n" + b);

            // sends headers
            serverOut.write(headers.getBytes());
            serverOut.flush();

            // if the request has a body, we need to forward it too
            forwardRequestBodyIfPresent(context, serverOut);

            // streams the answer
            stream(serverIn, clientOut);

        } finally {
            serverSocket.close();
        }
    }

    private void forwardRequestBodyIfPresent(RequestContext context, OutputStream serverOut) throws IOException {
        byte[] body = context.body();

        if(body != null && body.length > 0){
            serverOut.write(body);
            serverOut.flush();
        }
    }

    private void stream(InputStream serverIn, OutputStream clientOut) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;

        while ((bytesRead = serverIn.read(buffer)) != -1) {
            clientOut.write(buffer, 0, bytesRead);
            clientOut.flush();
        }
    }
}
