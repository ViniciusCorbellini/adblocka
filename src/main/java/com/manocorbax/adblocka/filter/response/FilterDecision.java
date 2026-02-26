package com.manocorbax.adblocka.filter.response;

public record FilterDecision(
        boolean blocked,
        String host,
        String filterName,
        String reason
) {
    public static FilterDecision allow(String host){
        return new FilterDecision(false, host, null, "allowed");
    }

    public  static FilterDecision block(String host, String filterName, String reason){
        return new FilterDecision(true, host, filterName, reason);
    }
}
