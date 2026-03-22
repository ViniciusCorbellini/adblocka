package com.manocorbax.adblocka.filter.http;

import com.manocorbax.adblocka.core.request.RequestContext;
import com.manocorbax.adblocka.filter.FilterEngine;
import com.manocorbax.adblocka.filter.response.FilterDecision;

import java.util.List;
import java.util.regex.Pattern;
import java.lang.StringBuilder;

public class HttpFilterEngine implements FilterEngine {

    private final List<PatternList> blockedPatternLists;
    private final Pattern pattern;

    public HttpFilterEngine(List<PatternList> blockedPatternLists, Pattern pattern) {
        this.blockedPatternLists = blockedPatternLists;
        this.pattern = pattern;
    }

    @Override
    public FilterDecision evaluate(RequestContext context) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(context.path()).append('\n');

        context.headers().forEach((k,v) ->
            sb.append(k).append(':').append(v).append('\n')
        );

        sb.append(new String(context.body()));

        String requestSurface = sb.toString();

        return blockedPatternLists.stream()
                .filter(bpl -> bpl.matches(requestSurface, pattern))
                .findFirst()
                .map(bpl -> FilterDecision.block(
                        context.host(),
                        id(),
                        "blocked by " + bpl.id()
                ))
                .orElseGet(() -> FilterDecision.allow(context.host()));
    }

    @Override
    public String id() { return "HTTP"; }
}
