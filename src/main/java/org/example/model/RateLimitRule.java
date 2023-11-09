package org.example.model;

import lombok.Setter;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@Setter
public class RateLimitRule {

    private static final long MILLISECONDS_PER_SECOND = 1000;

    private int limit;
    private long intervalInSeconds;
    private final Map<String, Queue<Long>> userTimestamps = new ConcurrentHashMap<>();

    public boolean isRateLimited(String userId) {
        Queue<Long> timestamps = userTimestamps.get(userId);
        if (timestamps == null) return false;
        int count = timestamps.size();
        removeExpiredTimestamps(timestamps, System.currentTimeMillis());
        return count >= limit;
    }

    public void incrementNotificationCount(String userId) {
        userTimestamps.computeIfAbsent(userId, key -> new LinkedList<>()).add(System.currentTimeMillis());
    }

    private void removeExpiredTimestamps(Queue<Long> timestamps, long currentTime) {
        timestamps.removeIf(timestamp -> isExpiredTimestamp(currentTime, timestamp));
    }

    private boolean isExpiredTimestamp(long currentTime, Long timestamp) {
        return currentTime - timestamp > intervalInSeconds * MILLISECONDS_PER_SECOND;
    }

}