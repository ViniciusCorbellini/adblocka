package com.manocorbax.adblocka.filter.http;

import com.manocorbax.adblocka.core.request.RequestContext;
import com.manocorbax.adblocka.filter.FilterEngine;
import com.manocorbax.adblocka.filter.response.FilterDecision;

import java.util.List;
import java.util.regex.Pattern;

public class HttpFilterEngine implements FilterEngine {

    private final List<PatternList> blockedPatternLists;
    private final Pattern pattern;

    public HttpFilterEngine(List<PatternList> blockedPatternLists, Pattern pattern) {
        this.blockedPatternLists = blockedPatternLists;
        this.pattern = pattern;
    }

    @Override
    public FilterDecision evaluate(RequestContext context) {
        String body = new String(context.body());

        String headers = context.headers()
                .entrySet()
                .stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .reduce("", (a, b) -> a + "\n" + b);

        String requestSurface =
                context.path() + "\n" +
                        headers + "\n" +
                        body;

        return blockedPatternLists.stream()
                .filter(bpl -> bpl.matches(requestSurface, pattern))
                .findFirst()
                .map(bpl -> FilterDecision.block(context.host(), id(), "blocked by " + bpl.id()))
                .orElseGet(() -> FilterDecision.allow(context.host()));
    }

    @Override
    public String id() { return "HTTP"; }
}
