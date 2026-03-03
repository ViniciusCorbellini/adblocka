package com.manocorbax.adblocka.filter.http;

import java.util.List;
import java.util.regex.Pattern;

public class DefaultPatternList implements PatternList {

    private static final List<String> BLOCKED_KEYWORDS = List.of(
            "ads",
            "adservice",
            "banner",
            "doubleclick",
            "tracking",
            "analytics",
            "sponsor",
            "popunder",
            "adserver");

    public boolean matches(String payload, Pattern pattern) {
//        String normalized = payload.toLowerCase(); useless, as my regex contains (?i)
        return pattern.matcher(payload).find();
    }

    public String id() {
        return "default-pattern";
    }

    public List<String> getLists(){
        return BLOCKED_KEYWORDS;
    }
}
