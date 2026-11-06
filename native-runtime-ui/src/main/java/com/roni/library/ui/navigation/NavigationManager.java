package com.roni.library.ui.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages bottom navigation bar tabs, item selection, and badge counters.
 */
public class NavigationManager {

    public static class TabItem {
        private final String tabId;
        private final String label;

        public TabItem(String tabId, String label) {
            this.tabId = tabId != null ? tabId : "";
            this.label = label != null ? label : "";
        }

        public String getTabId() {
            return tabId;
        }

        public String getLabel() {
            return label;
        }
    }

    private final List<TabItem> tabs = new CopyOnWriteArrayList<>();
    private final Map<String, Integer> badges = new ConcurrentHashMap<>();
    private volatile String selectedTab = "";

    public NavigationManager() {}

    public void addTab(String tabId, String label) {
        if (tabId != null) {
            tabs.add(new TabItem(tabId, label));
            if (selectedTab.isEmpty()) {
                selectedTab = tabId;
            }
        }
    }

    public void selectTab(String tabId) {
        if (tabId != null) {
            this.selectedTab = tabId;
        }
    }

    public String getSelectedTab() {
        return selectedTab;
    }

    public int getTabCount() {
        return tabs.size();
    }

    public List<TabItem> getTabs() {
        return new ArrayList<>(tabs);
    }

    public void setBadge(String tabId, int count) {
        if (tabId != null) {
            badges.put(tabId, Math.max(0, count));
        }
    }

    public int getBadge(String tabId) {
        if (tabId == null) return 0;
        Integer b = badges.get(tabId);
        return b != null ? b : 0;
    }

    public void clear() {
        tabs.clear();
        badges.clear();
        selectedTab = "";
    }
}
