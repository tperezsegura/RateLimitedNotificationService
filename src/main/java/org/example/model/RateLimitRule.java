package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitRule {
    @JsonProperty("limit")
    private int limit;
    @JsonProperty("intervalInSeconds")
    private long intervalInSeconds;
    private final Map<String, Queue<Long>> userTimestamps;

    public RateLimitRule() {
        this.userTimestamps = new ConcurrentHashMap<>();
    }

    public RateLimitRule(int limit, long intervalInSeconds) {
        this.limit = limit;
        this.intervalInSeconds = intervalInSeconds;
        this.userTimestamps = new ConcurrentHashMap<>();
    }

    public boolean isRateLimited(String userId) {
        Queue<Long> timestamps = userTimestamps.get(userId);
        if (timestamps == null) return false;
        long currentTime = System.currentTimeMillis();
        int count = timestamps.size();
        removeExpiredTimestamps(timestamps, currentTime);
        return count >= limit;
    }

    public void incrementNotificationCount(String userId) {
        userTimestamps.computeIfAbsent(userId, key -> new LinkedList<>()).add(System.currentTimeMillis());
    }

    private void removeExpiredTimestamps(Queue<Long> timestamps, long currentTime) {
        while (!timestamps.isEmpty()) {
            long intervalInMilliseconds = intervalInSeconds * 1000;
            if (!(currentTime - timestamps.peek() > intervalInMilliseconds)) break;
            timestamps.poll();
        }
    }
}
