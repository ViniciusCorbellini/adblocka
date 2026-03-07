package com.manocorbax.adblocka.filter.dns;

import com.manocorbax.adblocka.core.request.RequestContext;
import com.manocorbax.adblocka.filter.response.FilterDecision;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DnsFilterEngineTest {

    @Test
    void shouldBlockKnownAdDomain() {
        HostResolutionService resolver = host -> List.of();
        DnsFilterEngine engine = new DnsFilterEngine(
                resolver,
                List.of(new DefaultDomainBlocklist())
        );

        RequestContext context = contextForHost("ads.doubleclick.net");

        FilterDecision decision = engine.evaluate(context);

        assertTrue(decision.blocked());
    }

    @Test
    void shouldAllowRegularDomain() {
        HostResolutionService resolver = host -> List.of();
        DnsFilterEngine engine = new DnsFilterEngine(
                resolver,
                List.of(new DefaultDomainBlocklist())
        );

        RequestContext context = contextForHost("test.org");

        FilterDecision decision = engine.evaluate(context);

        assertFalse(decision.blocked());
    }

    private RequestContext contextForHost(String host) {
        return new RequestContext(
                new Socket(),
                "GET",
                host,
                80,
                "/",
                Map.of("host", host),
                new byte[0]
        );
    }
}
