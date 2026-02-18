package com.manocorbax.adblocka.filter.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JvmHostResolutionService implements HostResolutionService{

    @Override
    public List<InetAddress> resolve(String host) {
        try {
            return Arrays.asList(InetAddress.getAllByName(host));
        } catch (UnknownHostException ignored) {
            return Collections.emptyList();
        }
    }
}
