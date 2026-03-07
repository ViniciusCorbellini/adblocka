package com.manocorbax.adblocka.filter.http;

import com.manocorbax.adblocka.core.request.RequestContext;
import com.manocorbax.adblocka.filter.response.FilterDecision;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

//TODO

public class HttpFilterEngineTest {

    private HttpFilterEngine buildEngine() {
        PatternList defaultList = new DefaultPatternList();
        Pattern pattern = PatternBuilder.buildAdPattern(List.of(defaultList));

        return new HttpFilterEngine(
                List.of(defaultList),
                pattern
        );
    }

    @Test
    void shouldBlockRequestContainingAdKeywordInPath() {

        HttpFilterEngine engine = buildEngine();

        RequestContext context = new RequestContext(
                new Socket(),
                "GET",
                "example.com",
                80,
                "/ads/banner.js",
                Map.of(
                        "host", "example.com"
                ),
                new byte[0]
        );

        FilterDecision decision = engine.evaluate(context);

        assertTrue(decision.blocked());
    }

    @Test
    void shouldBlockTrackingKeywordInQueryString() {

        HttpFilterEngine engine = buildEngine();

        RequestContext context = new RequestContext(
                new Socket(),
                "GET",
                "example.com",
                80,
                "/collect?tracking_id=123",
                Map.of(
                        "host", "example.com"
                ),
                new byte[0]
        );

        FilterDecision decision = engine.evaluate(context);

        assertTrue(decision.blocked());
    }

    @Test
    void shouldAllowRegularRequest() {

        HttpFilterEngine engine = buildEngine();

        byte[] body = """
            {
              "script": "test"
            }
            """.getBytes();

        RequestContext context = new RequestContext(
                new Socket(),
                "GET",
                "example.com",
                80,
                "/test",
                Map.of(
                        "host", "example.com"
                ),
                body
        );
        FilterDecision decision = engine.evaluate(context);

        assertFalse(decision.blocked());
    }

    @Test
    void shouldNotBlockWordContainingAdsAsSubstring() {

        HttpFilterEngine engine = buildEngine();


        byte[] body = """
            {
              "script": "downloads"
            }
            """.getBytes();

        RequestContext context = new RequestContext(
                new Socket(),
                "GET",
                "example.com",
                80,
                "/downloads",
                Map.of(
                        "host", "downloads.com"
                ),
                body
        );

        FilterDecision decision = engine.evaluate(context);

        assertFalse(decision.blocked());
    }
}