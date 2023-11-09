package org.example.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RateLimitRule {

    private int limit;
    private long intervalInSeconds;
    private final Map<String, Queue<Long>> userTimestamps = new ConcurrentHashMap<>();

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
