package com.manocorbax.adblocka.filter.dns;

import com.manocorbax.adblocka.core.request.RequestContext;
import com.manocorbax.adblocka.filter.response.FilterDecision;

import java.net.InetAddress;
import java.util.List;

public class DnsFilterEngine {

    private final HostResolutionService resolver;
    private final List<DomainBlocklist> blocklists;

    public DnsFilterEngine(HostResolutionService resolver, List<DomainBlocklist> blocklists) {
        this.resolver = resolver;
        this.blocklists = blocklists;
    }

    public FilterDecision evaluate(RequestContext context) {
        String host = context.getHost();
        List<InetAddress> resolved = resolver.resolve(host);

        return blocklists.stream()
                .filter(blocklist -> blocklist.matches(host, resolved))
                .findFirst()
                .map(blocklist -> FilterDecision.block(host, "DNS", "blocked by " + blocklist.id()))
                .orElseGet(() -> FilterDecision.allow(host));
    }
}
