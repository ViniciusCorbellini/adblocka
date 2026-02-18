package com.manocorbax.adblocka.core.filter.dns;

import java.net.InetAddress;
import java.util.List;
import java.util.Set;

public class DefaultDomainBlocklist implements DomainBlocklist {

    private static final Set<String> BLOCKED_SUFFIXES = Set.of(
            "doubleclick.net",
            "googlesyndication.com",
            "googleadservices.com",
            "adservice.google.com",
            "ads.yahoo.com",
            "adnxs.com",
            "taboola.com",
            "outbrain.com",
            "criteo.com",
            "scorecardresearch.com"
    );

    private static final Set<String> BLOCKED_KEYWORDS = Set.of(
            "ads",
            "analytics",
            "tracking",
            "telemetry",
            "pixel"
    );

    @Override
    public boolean matches(String host, List<InetAddress> resolvedIps) {
        String normalized = host.toLowerCase();

        if (BLOCKED_SUFFIXES.stream().anyMatch(suffix -> normalized.equals(suffix) || normalized.endsWith("." + suffix))) {
            return true;
        }

        return BLOCKED_KEYWORDS.stream().anyMatch(normalized::contains);
    }

    @Override
    public String id() {
        return "default-domain-blocklist";
    }
}
