package com.manocorbax.adblocka.core.session;

import com.manocorbax.adblocka.core.handler.HandlerResolver;
import com.manocorbax.adblocka.core.handler.RequestHandler;
import com.manocorbax.adblocka.core.request.RequestContext;
import com.manocorbax.adblocka.core.request.RequestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientSession implements Runnable {

    private final Socket client;
    private final RequestParser parser;
    private final HandlerResolver resolver;

    public ClientSession(Socket client, RequestParser parser, HandlerResolver resolver) {
        this.client = client;
        this.parser = parser;
        this.resolver = resolver;
    }

    private static final Logger LOG = Logger.getLogger(ClientSession.class.getName());

    @Override
    public void run() {
        LOG.info("Starting new ClientSession\n");
        try {
            LOG.info("Reading client's request\n");
            String rawRequest = readRequest(client);

            LOG.info("REQUEST: " + rawRequest + "\n");

            RequestContext context = parser.parse(rawRequest, client);
            RequestHandler handler = resolver.resolve(context);

            handler.handle(context);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String readRequest(Socket s) throws IOException{
        // Sockets's input reader
        BufferedReader in = new BufferedReader(
                new InputStreamReader( //converts received bytes to characters
                        s.getInputStream()
                )
        );

        StringBuilder request = new StringBuilder();
        String line;

        //Iterates until the full message is read
        while (!(line = in.readLine()).isEmpty()) {
            request.append(line).append("\r\n");
        }

        return request.toString();
    }
}
