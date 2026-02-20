package com.manocorbax.adblocka.filter.dns;

import com.manocorbax.adblocka.core.request.RequestContext;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DnsFilterEngineTest {

    @Test
    void shouldBlockKnownAdDomain(){
        HostResolutionService resolver = host -> List.of();
        DnsFilterEngine engine = new DnsFilterEngine(
                resolver,
                List.of(new DefaultDomainBlocklist())
        );

        RequestContext context =  new RequestContext(
                "GET / HTTP/1.1\r\nHost: ads.doubleclick.net\r\n\r\n",
                new Socket(),
                "GET",
                "ads.doubleclick.net",
                80
        );

        DnsFilterDecision decision = engine.evaluate(context);

        assert decision.blocked();
    }

    @Test
    void shouldAllowRegularDomain(){
        HostResolutionService resolver = host -> List.of();
        DnsFilterEngine engine = new DnsFilterEngine(
                resolver,
                List.of(new DefaultDomainBlocklist())
        );

        RequestContext context = new RequestContext(
                "GET / HTTP/1.1\r\nHost: test.org\r\n\r\n",
                new Socket(),
                "GET",
                "test.org",
                80
        );

        DnsFilterDecision decision = engine.evaluate(context);

        assert !decision.blocked();
    }
}
