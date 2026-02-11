package com.manocorbax.adblocka.core.handler;

import com.manocorbax.adblocka.core.request.RequestContext;

import java.util.List;

public class HandlerResolver {

    private final List<RequestHandler> handlers;

    public HandlerResolver(List<RequestHandler> handlers) {
        this.handlers = handlers;
    }

    public RequestHandler resolve(RequestContext context) {
        return handlers.stream()
                .filter(h -> h.supports(context))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("No handler found"));
    }
}
