package com.manocorbax.adblocka.core;

import com.manocorbax.adblocka.core.handler.ConnectHandler;
import com.manocorbax.adblocka.core.handler.HandlerResolver;
import com.manocorbax.adblocka.core.handler.HttpHandler;
import com.manocorbax.adblocka.core.handler.RequestHandler;
import com.manocorbax.adblocka.core.filter.dns.BlockedRequestResponder;
import com.manocorbax.adblocka.core.filter.dns.DefaultDomainBlocklist;
import com.manocorbax.adblocka.core.filter.dns.DnsFilteringEngine;
import com.manocorbax.adblocka.core.filter.dns.DomainBlocklist;
import com.manocorbax.adblocka.core.filter.dns.JvmHostResolutionService;
import com.manocorbax.adblocka.core.request.RequestParser;
import com.manocorbax.adblocka.core.session.ClientSession;

import java.net.ServerSocket;

import java.net.Socket;
import java.util.List;
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

        // ===== COMPOSITION ROOT =====

        RequestParser parser = new RequestParser();

        List<RequestHandler> handlers = List.of(
                new ConnectHandler(),
                new HttpHandler()
        );

        HandlerResolver resolver =
                new HandlerResolver(handlers);

        List<DomainBlocklist> blocklists = List.of(new DefaultDomainBlocklist());
        DnsFilteringEngine dnsFilteringEngine = new DnsFilteringEngine(
                new JvmHostResolutionService(),
                blocklists
        );
        BlockedRequestResponder blockedRequestResponder = new BlockedRequestResponder();

        // ============================

        while (true) {
            LOG.info("Waiting for clients\n");
            Socket client = ss.accept();

            LOG.info("Client Accepted\n");
            new Thread(new ClientSession(
                    client,
                    parser,
                    resolver,
                    dnsFilteringEngine,
                    blockedRequestResponder
            )).start();
        }
    }
}
