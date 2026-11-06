package com.roni.library.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Synchronizes, manages, and purges runtime HTTP/Session cookies.
 */
public class CookieSyncManager {

    private final Map<String, List<String>> cookieStore = new ConcurrentHashMap<>();

    public CookieSyncManager() {}

    public void setCookie(String domain, String cookie) {
        if (domain != null && cookie != null) {
            cookieStore.computeIfAbsent(domain, k -> new ArrayList<>()).add(cookie);
        }
    }

    public List<String> getCookies(String domain) {
        if (domain == null) return new ArrayList<>();
        List<String> list = cookieStore.get(domain);
        return list != null ? new ArrayList<>(list) : new ArrayList<String>();
    }

    public void clearCookies(String domain) {
        if (domain != null) {
            cookieStore.remove(domain);
        }
    }

    public void clearAllCookies() {
        cookieStore.clear();
    }

    public Map<String, List<String>> getAllCookies() {
        return new HashMap<>(cookieStore);
    }
}
