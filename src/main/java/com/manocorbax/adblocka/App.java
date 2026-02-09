package com.manocorbax.adblocka;

import java.util.logging.Logger;

import com.manocorbax.adblocka.core.ProxyServer;

/**
 * Hello world!
 */
public class App {

    private static final Logger LOG = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        LOG.info("Startig Proxy Server");
        ProxyServer ps = new ProxyServer();
        try {
            ps.start();
        } catch (Exception e) {
            LOG.warning("I f'ed up");
            throw new RuntimeException(e);
        }
    }
}
