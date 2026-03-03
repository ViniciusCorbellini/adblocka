package com.manocorbax.adblocka.filter;

import com.manocorbax.adblocka.core.request.RequestContext;
import com.manocorbax.adblocka.filter.response.FilterDecision;

public interface FilterEngine {

    FilterDecision evaluate(RequestContext context);

    String id();

}
