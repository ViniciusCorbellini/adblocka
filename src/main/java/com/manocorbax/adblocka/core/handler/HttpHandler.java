package com.manocorbax.adblocka.core.handler;

import com.manocorbax.adblocka.core.request.RequestContext;

public class HttpHandler implements RequestHandler{

    @Override
    public boolean supports(RequestContext context) {
        return !"CONNECT".equalsIgnoreCase(context.getMethod());
    }

    @Override
    public void handle(RequestContext context) throws Exception {
        throw new UnsupportedOperationException("its in the TODO list bro, just read it!");
    }
}
