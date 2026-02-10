package com.manocorbax.adblocka.core;

import com.manocorbax.adblocka.core.handler.ConnectHandler;
import com.manocorbax.adblocka.core.handler.Handler;
import com.manocorbax.adblocka.core.handler.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientSession implements Runnable {

    private final Socket client;

    public ClientSession(Socket client) {
        this.client = client;
    }

    private static final Logger LOG = Logger.getLogger(ClientSession.class.getName());

    @Override
    public void run() {
        LOG.info("Starting new thread for client\n");

        if (client == null) {
            LOG.warning("Invalid Client\n");
            throw new RuntimeException("Invalid Client");
        }

        try {
            // Sockets's input reader
            BufferedReader in = new BufferedReader(
                    new InputStreamReader( //converts received bytes to characters
                            client.getInputStream()
                    )
            );

            // Reads the request header
            String line = in.readLine();
            LOG.info("REQUEST: " + line + "\n");

            //Iterates until the full message is read
            StringBuilder message = new StringBuilder(line);
            while (!(line = in.readLine()).isEmpty()) {
                message.append(line).append("\n");
            }

            LOG.info("Message received: \n" + message);
            handleMessage(message.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void handleMessage(String message) throws IOException {
        Handler.getHandlerFor(message, client).handle();
    }
}
