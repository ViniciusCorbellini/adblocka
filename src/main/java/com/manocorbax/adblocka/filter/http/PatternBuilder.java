package com.manocorbax.adblocka.filter.http;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PatternBuilder {

    // this pattern may be too restrictive
    // ex: analytics-company.com -- blocked
    //     /my-analytics-report.pdf -- also blocked
    // TODO: fix this
    public static Pattern buildAdPattern(List<PatternList> blockedPAtternLists) {
        List<String> keywords = new ArrayList<>();

        for (PatternList bpl : blockedPAtternLists) {
            keywords.addAll(bpl.getLists());
        }

        String joined = String.join("|", keywords);

        String regex = "(?i)(^|[\\/\\.\\-_?&=])(" +
                joined +
                ")([\\/\\.\\-_?&=]|$)";

        return Pattern.compile(regex);

    }
}
