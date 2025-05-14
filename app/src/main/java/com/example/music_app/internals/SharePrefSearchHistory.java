package com.example.music_app.internals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.music_app.activities.auth.LoginActivity;
import com.example.music_app.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class SharePrefSearchHistory {

    private static final String SHARED_PREFS_NAME = "SearchHistory";
    private static final String KEY_SEARCH_HISTORY = "searchHistory";
    private static final int MAX_HISTORY_SIZE = 10;

    private SharedPreferences preferences;

    public SharePrefSearchHistory(Context context) {
        preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void addSearchQuery(String newSearchQuery) {
        Set<String> searchHistory = preferences.getStringSet(KEY_SEARCH_HISTORY, new HashSet<String>());
        searchHistory.add(newSearchQuery);

        if (searchHistory.size() > MAX_HISTORY_SIZE) {
            Iterator<String> iterator = searchHistory.iterator();
            iterator.next();
            iterator.remove();
        }
        preferences.edit()
                .putStringSet(KEY_SEARCH_HISTORY, searchHistory)
                .apply();
    }

    public Set<String> getSearchHistory() {
        return preferences.getStringSet(KEY_SEARCH_HISTORY, new HashSet<String>());
    }
    public void clearSearchHistory() {
        preferences.edit()
                .remove(KEY_SEARCH_HISTORY)
                .apply();
    }
}
