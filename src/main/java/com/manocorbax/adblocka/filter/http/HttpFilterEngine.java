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
        //TODO: fix bad logic: i dont extract the actual payload yet::i'll do it later
        String rawRequest = context.getRawRequest();

        return blockedPatternLists.stream()
                .filter(bpl -> bpl.matches(rawRequest, pattern))
                .findFirst()
                .map(bpl -> FilterDecision.block(context.getHost(), id(), "blocked by " + bpl.id()))
                .orElseGet(() -> FilterDecision.allow(context.getHost()));
    }

    @Override
    public String id() { return "HTTP"; }
}
