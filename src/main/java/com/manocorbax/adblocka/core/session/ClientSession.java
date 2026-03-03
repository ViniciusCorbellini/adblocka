package com.manocorbax.adblocka.core.session;

import com.manocorbax.adblocka.core.handler.HandlerResolver;
import com.manocorbax.adblocka.core.handler.RequestHandler;
import com.manocorbax.adblocka.core.request.RequestContext;
import com.manocorbax.adblocka.core.request.RequestParser;
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
    private final RequestParser parser;
    private final HandlerResolver resolver;
    private final DnsFilterEngine dnsFilterEngine;
    private final BlockedRequestResponder blockedRequestResponder;
    private final HttpFilterEngine httpFilterEngine;

    public ClientSession(Socket client,
                         RequestParser parser,
                         HandlerResolver resolver,
                         DnsFilterEngine dnsFilterEngine,
                         BlockedRequestResponder blockedRequestResponder,
                         HttpFilterEngine httpFilterEngine) {
        this.client = client;
        this.parser = parser;
        this.resolver = resolver;
        this.dnsFilterEngine = dnsFilterEngine;
        this.blockedRequestResponder = blockedRequestResponder;
        this.httpFilterEngine = httpFilterEngine;
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

            FilterDecision dnsDecision = dnsFilterEngine.evaluate(context);
            boolean blocked = evaluateIfDecisionBlocked(dnsDecision, context, "DNS");
            if (blocked) return;

            FilterDecision regexBlocked = httpFilterEngine.evaluate(context);
            blocked = evaluateIfDecisionBlocked(regexBlocked, context, "HTTP-REGEX");
            if (blocked) return;

            handler.handle(context);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean evaluateIfDecisionBlocked(FilterDecision decision, RequestContext context, String filtername) throws IOException {
        boolean blocked = decision.blocked();

        if (blocked){
            LOG.info("Blocked request to host " + context.getHost() + " reason: " + decision.reason() + "\n");
            blockedRequestResponder.respond(context, decision, filtername);
        }

        return blocked;
    }

    private String readRequest(Socket s) throws IOException {
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
