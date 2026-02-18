package com.manocorbax.adblocka.filter.dns;

import java.net.InetAddress;
import java.util.List;

public interface HostResolutionService {

    List<InetAddress> resolve(String host);
}
