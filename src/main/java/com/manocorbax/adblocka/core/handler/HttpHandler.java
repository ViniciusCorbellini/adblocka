package com.manocorbax.adblocka.core.handler;

import com.manocorbax.adblocka.core.request.RequestContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpHandler implements RequestHandler{

    @Override
    public boolean supports(RequestContext context) {
        return !"CONNECT".equalsIgnoreCase(context.getMethod());
    }

    @Override
    public void handle(RequestContext context) throws Exception {
        Socket clientSocket = context.getClientSocket();
        Socket serverSocket = new Socket(context.getHost(), context.getPort());

        try {

            InputStream clientIn = clientSocket.getInputStream();
            OutputStream clientOut = clientSocket.getOutputStream();

            InputStream serverIn = serverSocket.getInputStream();
            OutputStream serverOut = serverSocket.getOutputStream();

            String normalizedRequest = normalizeRequest(context.getRawRequest());

            // sends headers
            serverOut.write(normalizedRequest.getBytes());
            serverOut.flush();

            // if the request has a body, we need to forward it too
            forwardRequestBodyIfPresent(context, clientIn, serverOut);

            // streams the answer
            stream(serverIn, clientOut);

        } finally {
            serverSocket.close();
        }
    }

    private String normalizeRequest(String rawRequest){
        String[] lines = rawRequest.split("\r\n");
        String[] firstLine = lines[0].split(" ");

        String method = firstLine[0];
        String url = firstLine[1];
        String version = firstLine[2];

        if (url.startsWith("http://") || url.startsWith("https://")) {
            int index = url.indexOf("/", url.indexOf("//") + 2);
            url = (index != -1) ? url.substring(index) : "/";
        }

        lines[0] = method + " " + url + " " + version;

        return String.join("\r\n", lines);
    }

    private void forwardRequestBodyIfPresent(RequestContext context, InputStream clientIn, OutputStream serverOut) throws IOException {
        String raw = context.getRawRequest();

        int contentLength = extractContentLength(raw);

        if (contentLength > 0) {

            byte[] buffer = new byte[8192];
            int remaining = contentLength;

            while (remaining > 0) {
                int read = clientIn.read(buffer, 0, Math.min(buffer.length, remaining));
                if (read == -1) break;

                serverOut.write(buffer, 0, read);
                remaining -= read;
            }

            serverOut.flush();
        }

    }

    private int extractContentLength(String raw) {
        for (String line : raw.split("\r\n")) {
            if (line.toLowerCase().startsWith("content-length:")) {
                return Integer.parseInt(line.split(":")[1].trim());
            }
        }
        return 0;
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
