package com.manocorbax.adblocka.core.filter.dns;

import java.net.InetAddress;
import java.util.List;

public interface HostResolutionService {

    List<InetAddress> resolve(String host);
}
