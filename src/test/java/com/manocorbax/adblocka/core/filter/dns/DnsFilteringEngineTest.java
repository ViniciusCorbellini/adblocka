package com.manocorbax.adblocka.core.filter.dns;

import com.manocorbax.adblocka.core.request.RequestContext;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DnsFilteringEngineTest {

    @Test
    void shouldBlockKnownAdDomain() {
        HostResolutionService resolver = host -> List.of();
        DnsFilteringEngine engine = new DnsFilteringEngine(
                resolver,
                List.of(new DefaultDomainBlocklist())
        );

        RequestContext context = new RequestContext(
                "GET / HTTP/1.1\r\nHost: ads.doubleclick.net\r\n\r\n",
                new Socket(),
                "GET",
                "ads.doubleclick.net",
                80
        );

        DnsFilterDecision decision = engine.evaluate(context);

        assertTrue(decision.blocked());
    }

    @Test
    void shouldAllowRegularDomain() {
        HostResolutionService resolver = host -> List.of();
        DnsFilteringEngine engine = new DnsFilteringEngine(
                resolver,
                List.of(new DefaultDomainBlocklist())
        );

        RequestContext context = new RequestContext(
                "GET / HTTP/1.1\r\nHost: example.org\r\n\r\n",
                new Socket(),
                "GET",
                "example.org",
                80
        );

        DnsFilterDecision decision = engine.evaluate(context);

        assertFalse(decision.blocked());
    }
}
