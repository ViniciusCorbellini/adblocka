package com.manocorbax.adblocka.core;

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
            String requestHeader = in.readLine();
            LOG.info("REQUEST: " + requestHeader + "\n");

            //Iterates until the communication is done
            String line;
            while (!(line = in.readLine()).isEmpty()) {
                LOG.info("Message received: \n\t" + line);
            }

            LOG.info("Communication ended\n");
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
