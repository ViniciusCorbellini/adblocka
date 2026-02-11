package com.manocorbax.adblocka.core.handler;

import com.manocorbax.adblocka.core.request.RequestContext;

public interface RequestHandler {

    boolean supports(RequestContext context);

    void handle(RequestContext context) throws Exception;
}
