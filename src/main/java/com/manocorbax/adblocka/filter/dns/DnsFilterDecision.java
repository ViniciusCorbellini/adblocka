package com.manocorbax.adblocka.filter.dns;

public record DnsFilterDecision(
        boolean blocked,
        String host,
        String reason
) {
    public static DnsFilterDecision allow(String host){
        return new DnsFilterDecision(false, host, "allowed");
    }

    public  static DnsFilterDecision block(String host, String reason){
        return new DnsFilterDecision(true, host, reason);
    }
}
