package com.manocorbax.adblocka.core;

import java.io.IOException;
import java.net.ServerSocket;

import java.net.Socket;
import java.util.logging.Logger;

public class ProxyServer {

    final int port = 8080;

    private static final Logger LOG = Logger.getLogger(ProxyServer.class.getName());

    /*
     * Starts a web server that waits for connections and creates a client server for each one
     */
    public void start() throws Exception {
        ServerSocket ss = new ServerSocket(port);
        LOG.info(String.format("Starting ServerSocket on ip:port - > %s:%s \n",
                ss.getInetAddress().getHostAddress(),
                ss.getLocalPort())
        );

        while (true) {
            LOG.info("Waiting for clients\n");
            Socket client = ss.accept();

            LOG.info("Client Accepted, instantiating new Client Session\n");
            new Thread(new ClientSession(client)).start();
        }
    }
}
