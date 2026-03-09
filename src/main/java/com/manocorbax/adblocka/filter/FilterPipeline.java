package com.manocorbax.adblocka.filter;

import com.manocorbax.adblocka.core.request.RequestContext;
import com.manocorbax.adblocka.filter.response.BlockedRequestResponder;
import com.manocorbax.adblocka.filter.response.FilterDecision;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class FilterPipeline {

    private final BlockedRequestResponder blockedRequestResponder;

    private final List<FilterEngine> filterEngineList;

    public FilterPipeline(List<FilterEngine> filterEngineList, BlockedRequestResponder blockedRequestResponder) {
        this.filterEngineList = filterEngineList;
        this.blockedRequestResponder = blockedRequestResponder;
    }

    private static final Logger LOG = Logger.getLogger(FilterPipeline.class.getName());

    // Returns true if blocked
    public boolean doFilter(RequestContext context) throws IOException {

        for (FilterEngine f : filterEngineList){
            FilterDecision decision = f.evaluate(context);
            boolean blocked = evaluateIfDecisionBlocked(decision, context, f.id());

            if(blocked) return true;
        }

        return false;
    }

    // will log and respond with 403 if the request is blocked
    private boolean evaluateIfDecisionBlocked(FilterDecision decision, RequestContext context, String filtername) throws IOException {
        boolean blocked = decision.blocked();

        if (blocked){
            LOG.info("Blocked request to host " + context.host() + " reason: " + decision.reason() + "\n");
            blockedRequestResponder.respond(context, decision, filtername);
        }

        return blocked;
    }
}
