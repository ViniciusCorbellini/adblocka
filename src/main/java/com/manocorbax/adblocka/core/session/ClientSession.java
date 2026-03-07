package com.manocorbax.adblocka.core.session;

import com.manocorbax.adblocka.core.handler.HandlerResolver;
import com.manocorbax.adblocka.core.handler.RequestHandler;
import com.manocorbax.adblocka.core.request.HttpRequestParser;
import com.manocorbax.adblocka.core.request.ParsedHttpRequest;
import com.manocorbax.adblocka.core.request.RequestContext;
import com.manocorbax.adblocka.core.request.RequestParser;
import com.manocorbax.adblocka.filter.FilterEngine;
import com.manocorbax.adblocka.filter.FilterPipeline;
import com.manocorbax.adblocka.filter.http.HttpFilterEngine;
import com.manocorbax.adblocka.filter.response.BlockedRequestResponder;
import com.manocorbax.adblocka.filter.response.FilterDecision;
import com.manocorbax.adblocka.filter.dns.DnsFilterEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

// TODO: HTTP FILTER
public class ClientSession implements Runnable {

    private final Socket client;
    private final HttpRequestParser httpParser;
    private final RequestParser parser;
    private final HandlerResolver resolver;
    private final FilterPipeline filterPipeline;

    public ClientSession(Socket client,
                         HttpRequestParser httpParser,
                         RequestParser parser,
                         HandlerResolver resolver,
                         FilterPipeline filterPipeline) {
        this.client = client;
        this.httpParser = httpParser;
        this.parser = parser;
        this.resolver = resolver;
        this.filterPipeline = filterPipeline;
    }

    private static final Logger LOG = Logger.getLogger(ClientSession.class.getName());

    @Override
    public void run() {
        LOG.info("Starting new ClientSession\n");
        try {
            LOG.info("Reading client's request\n");
            ParsedHttpRequest parsedHttpRequest = httpParser.parse(client.getInputStream());

            LOG.info("REQUEST: " + parsedHttpRequest.getRequestLine() + "\n");

            RequestContext context = parser.buildContext(parsedHttpRequest, client);
            RequestHandler handler = resolver.resolve(context);

            boolean blocked = filterPipeline.doFilter(context);

            if (blocked) {
                LOG.info("Request blocked... ignoring\n");
                return;
            }

            handler.handle(context);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
