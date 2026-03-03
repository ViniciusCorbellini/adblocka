package com.manocorbax.adblocka.filter.http;

import com.manocorbax.adblocka.core.request.RequestContext;
import com.manocorbax.adblocka.filter.response.FilterDecision;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

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
                "GET /ads/banner.js HTTP/1.1\r\nHost: example.com\r\n\r\n",
                new Socket(),
                "GET",
                "example.com",
                80
        );

        FilterDecision decision = engine.evaluate(context);

        assertTrue(decision.blocked());
    }

    @Test
    void shouldBlockRequestContainingAdKeywordInHost() {

        HttpFilterEngine engine = buildEngine();

        RequestContext context = new RequestContext(
                "GET / HTTP/1.1\r\nHost: ads.doubleclick.net\r\n\r\n",
                new Socket(),
                "GET",
                "ads.doubleclick.net",
                80
        );

        FilterDecision decision = engine.evaluate(context);

        assertTrue(decision.blocked());
    }

    @Test
    void shouldBlockTrackingKeywordInQueryString() {

        HttpFilterEngine engine = buildEngine();

        RequestContext context = new RequestContext(
                "GET /collect?tracking_id=123 HTTP/1.1\r\nHost: example.com\r\n\r\n",
                new Socket(),
                "GET",
                "example.com",
                80
        );

        FilterDecision decision = engine.evaluate(context);

        assertTrue(decision.blocked());
    }

    @Test
    void shouldAllowRegularRequest() {

        HttpFilterEngine engine = buildEngine();

        RequestContext context = new RequestContext(
                "GET /home HTTP/1.1\r\nHost: example.com\r\n\r\n",
                new Socket(),
                "GET",
                "example.com",
                80
        );

        FilterDecision decision = engine.evaluate(context);

        assertFalse(decision.blocked());
    }

    @Test
    void shouldNotBlockWordContainingAdsAsSubstring() {

        HttpFilterEngine engine = buildEngine();

        // downloADS
        RequestContext context = new RequestContext(
                "GET /downloads HTTP/1.1\r\nHost: example.com\r\n\r\n",
                new Socket(),
                "GET",
                "example.com",
                80
        );

        FilterDecision decision = engine.evaluate(context);

        assertFalse(decision.blocked());
    }
}