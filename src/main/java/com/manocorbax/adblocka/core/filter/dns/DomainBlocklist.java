package com.manocorbax.adblocka.core.filter.dns;

import java.net.InetAddress;
import java.util.List;

public interface DomainBlocklist {

    boolean matches(String host, List<InetAddress> resolvedIps);

    String id();
}
