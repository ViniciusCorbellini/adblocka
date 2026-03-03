package com.manocorbax.adblocka.filter.http;

import java.util.List;
import java.util.regex.Pattern;

public interface PatternList {

    boolean matches(String payload, Pattern pattern);

    String id();

    // All pattern lists involving blocked keywords/patterns should be returned
    List<String> getLists();
}
