package com.roni.library.router.matcher;

import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.router.RouteMatchResult;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Standard implementation of {@link IRouteMatcher} supporting URI schemes
 * (app://, bridge://, native://), path variables ({variable}), and query parameters.
 *
 * @author infoadmstudio2020-cyber
 */
public class PatternRouteMatcher implements IRouteMatcher {

    private static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile("\\{([a-zA-Z0-9_]+)\\}");

    @Override
    public RouteMatchResult match(String pattern, String requestUri, IApiHandler handler) {
        if (pattern == null || requestUri == null) {
            return RouteMatchResult.notFound();
        }

        // Normalize both pattern and requestUri
        String normalizedPattern = normalizeUri(pattern);
        String rawUri = normalizeUri(requestUri);

        // Separate query string from requestUri
        String uriPath = rawUri;
        String queryString = null;
        int queryIndex = rawUri.indexOf('?');
        if (queryIndex >= 0) {
            uriPath = rawUri.substring(0, queryIndex);
            if (queryIndex + 1 < rawUri.length()) {
                queryString = rawUri.substring(queryIndex + 1);
            }
        }

        // Parse query parameters
        Map<String, String> queryParams = parseQueryParams(queryString);

        // Check exact match first
        if (normalizedPattern.equals(uriPath)) {
            return RouteMatchResult.success(pattern, handler, new HashMap<>(), queryParams);
        }

        // Parse path variables from pattern
        Matcher varMatcher = PATH_VARIABLE_PATTERN.matcher(normalizedPattern);
        Map<Integer, String> groupIndexToVarName = new HashMap<>();
        StringBuilder regexBuilder = new StringBuilder("^");
        int lastEnd = 0;
        int groupCount = 1;

        while (varMatcher.find()) {
            regexBuilder.append(Pattern.quote(normalizedPattern.substring(lastEnd, varMatcher.start())));
            String varName = varMatcher.group(1);
            regexBuilder.append("([^/]+)");
            groupIndexToVarName.put(groupCount++, varName);
            lastEnd = varMatcher.end();
        }

        if (groupIndexToVarName.isEmpty()) {
            // No path variables and didn't match directly
            return RouteMatchResult.notFound();
        }

        regexBuilder.append(Pattern.quote(normalizedPattern.substring(lastEnd)));
        regexBuilder.append("$");

        Pattern compiledPattern = Pattern.compile(regexBuilder.toString());
        Matcher uriMatcher = compiledPattern.matcher(uriPath);

        if (uriMatcher.matches()) {
            Map<String, String> pathVariables = new HashMap<>();
            for (Map.Entry<Integer, String> entry : groupIndexToVarName.entrySet()) {
                String rawValue = uriMatcher.group(entry.getKey());
                String decodedValue = decode(rawValue);
                pathVariables.put(entry.getValue(), decodedValue);
            }
            return RouteMatchResult.success(pattern, handler, pathVariables, queryParams);
        }

        return RouteMatchResult.notFound();
    }

    /**
     * Normalizes a URI or pattern by removing redundant slashes and normalizing schemes.
     */
    private String normalizeUri(String input) {
        if (input == null) return "";
        String trimmed = input.trim();

        // Convert scheme prefixes like "app://", "bridge://", "native://" into unified form
        // Strip trailing slash unless it's just "/"
        if (trimmed.length() > 1 && trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }

    /**
     * Parses a query string into key-value pairs.
     */
    private Map<String, String> parseQueryParams(String queryString) {
        Map<String, String> params = new HashMap<>();
        if (queryString == null || queryString.isEmpty()) {
            return params;
        }

        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            if (pair.isEmpty()) continue;
            int idx = pair.indexOf('=');
            if (idx > 0) {
                String key = decode(pair.substring(0, idx));
                String value = idx + 1 < pair.length() ? decode(pair.substring(idx + 1)) : "";
                params.put(key, value);
            } else if (idx < 0) {
                params.put(decode(pair), "");
            }
        }
        return params;
    }

    private String decode(String value) {
        if (value == null) return "";
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return value;
        }
    }
}
