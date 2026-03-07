package com.manocorbax.adblocka.core;

import com.manocorbax.adblocka.core.handler.ConnectHandler;
import com.manocorbax.adblocka.core.handler.HandlerResolver;
import com.manocorbax.adblocka.core.handler.HttpHandler;
import com.manocorbax.adblocka.core.handler.RequestHandler;
import com.manocorbax.adblocka.core.request.HttpRequestParser;
import com.manocorbax.adblocka.core.request.RequestParser;
import com.manocorbax.adblocka.core.session.ClientSession;
import com.manocorbax.adblocka.filter.FilterEngine;
import com.manocorbax.adblocka.filter.FilterPipeline;
import com.manocorbax.adblocka.filter.dns.*;
import com.manocorbax.adblocka.filter.http.DefaultPatternList;
import com.manocorbax.adblocka.filter.http.HttpFilterEngine;
import com.manocorbax.adblocka.filter.http.PatternBuilder;
import com.manocorbax.adblocka.filter.http.PatternList;
import com.manocorbax.adblocka.filter.response.BlockedRequestResponder;

import java.net.ServerSocket;

import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

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
        // request parser
        HttpRequestParser httpParser = new HttpRequestParser();
        RequestParser parser = new RequestParser();

        // Handler Resolver
        List<RequestHandler> handlers = List.of(
                new ConnectHandler(),
                new HttpHandler()
        );

        HandlerResolver resolver =
                new HandlerResolver(handlers);

        // dns filter
        List<DomainBlocklist> blocklists = List.of(new DefaultDomainBlocklist());
        DnsFilterEngine dnsFilterEngine = new DnsFilterEngine(
                new JvmHostResolutionService(),
                blocklists
        );

        // http filter
        List<PatternList> blockedPAtternLists = List.of(new DefaultPatternList());
        Pattern pattern = PatternBuilder.buildAdPattern(blockedPAtternLists);
        HttpFilterEngine httpFilterEngine = new HttpFilterEngine(blockedPAtternLists, pattern);

        // Filter pipeline
        BlockedRequestResponder blockedRequestResponder = new BlockedRequestResponder();
        List<FilterEngine> filters = List.of(dnsFilterEngine, httpFilterEngine);
        FilterPipeline filterPipeline = new FilterPipeline(filters, blockedRequestResponder);
        // ============================

        while (true) {
            LOG.info("Waiting for clients\n");
            Socket client = ss.accept();

            LOG.info("Client Accepted\n");
            new Thread(
                    new ClientSession(
                            client,
                            httpParser,
                            parser,
                            resolver,
                            filterPipeline
                    )
            ).start();
        }
    }
}
