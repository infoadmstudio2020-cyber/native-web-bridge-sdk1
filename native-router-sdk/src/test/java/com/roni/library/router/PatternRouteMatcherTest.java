package com.roni.library.router;

import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.router.matcher.PatternRouteMatcher;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link PatternRouteMatcher}.
 *
 * @author infoadmstudio2020-cyber
 */
public class PatternRouteMatcherTest {

    private PatternRouteMatcher matcher;
    private IApiHandler dummyHandler;

    @Before
    public void setUp() {
        matcher = new PatternRouteMatcher();
        dummyHandler = new IApiHandler() {
            @Override
            public String getPath() {
                return "/dummy";
            }

            @Override
            public String getAction() {
                return "dummy";
            }

            @Override
            public NativeApiResponse handleRequest(NativeApiRequest request) {
                return NativeApiResponse.success(request.getRequestId());
            }
        };
    }

    @Test
    public void testExactMatch() {
        RouteMatchResult result = matcher.match("/api/user", "/api/user", dummyHandler);
        assertTrue(result.isMatched());
        assertEquals("/api/user", result.getMatchedPattern());
        assertTrue(result.getPathVariables().isEmpty());
    }

    @Test
    public void testSchemeMatch() {
        RouteMatchResult result = matcher.match("app://settings", "app://settings", dummyHandler);
        assertTrue(result.isMatched());
        assertEquals("app://settings", result.getMatchedPattern());

        RouteMatchResult bridgeResult = matcher.match("bridge://call/rpc", "bridge://call/rpc", dummyHandler);
        assertTrue(bridgeResult.isMatched());
    }

    @Test
    public void testPathVariableExtraction() {
        RouteMatchResult result = matcher.match("/user/{id}/profile", "/user/1024/profile", dummyHandler);
        assertTrue(result.isMatched());
        assertEquals("1024", result.getPathVariable("id"));

        RouteMatchResult multiResult = matcher.match(
                "app://order/{orderId}/item/{itemId}",
                "app://order/ord_99/item/item_555",
                dummyHandler
        );
        assertTrue(multiResult.isMatched());
        assertEquals("ord_99", multiResult.getPathVariable("orderId"));
        assertEquals("item_555", multiResult.getPathVariable("itemId"));
    }

    @Test
    public void testQueryParamsExtraction() {
        RouteMatchResult result = matcher.match("/search", "/search?query=android&page=2", dummyHandler);
        assertTrue(result.isMatched());
        assertEquals("android", result.getQueryParam("query"));
        assertEquals("2", result.getQueryParam("page"));
    }

    @Test
    public void testPathVariablesWithQueryParams() {
        RouteMatchResult result = matcher.match(
                "bridge://article/{articleId}",
                "bridge://article/art_777?format=json&includeComments=true",
                dummyHandler
        );
        assertTrue(result.isMatched());
        assertEquals("art_777", result.getPathVariable("articleId"));
        assertEquals("json", result.getQueryParam("format"));
        assertEquals("true", result.getQueryParam("includeComments"));
    }

    @Test
    public void testNoMatch() {
        RouteMatchResult result = matcher.match("/api/home", "/api/other", dummyHandler);
        assertFalse(result.isMatched());
    }
}
