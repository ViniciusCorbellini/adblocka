package com.manocorbax.adblocka.core.filter.dns;

import com.manocorbax.adblocka.core.request.RequestContext;

import java.net.InetAddress;
import java.util.List;

public class DnsFilteringEngine {

    private final HostResolutionService resolver;
    private final List<DomainBlocklist> blocklists;

    public DnsFilteringEngine(HostResolutionService resolver, List<DomainBlocklist> blocklists) {
        this.resolver = resolver;
        this.blocklists = blocklists;
    }

    public DnsFilterDecision evaluate(RequestContext context) {
        String host = context.getHost();
        List<InetAddress> resolved = resolver.resolve(host);

        return blocklists.stream()
                .filter(blocklist -> blocklist.matches(host, resolved))
                .findFirst()
                .map(blocklist -> DnsFilterDecision.block(host, "blocked by " + blocklist.id()))
                .orElseGet(() -> DnsFilterDecision.allow(host));
    }
}
