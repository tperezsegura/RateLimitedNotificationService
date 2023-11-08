package org.example.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.Gateway;
import org.example.exception.InvalidNotificationTypeException;
import org.example.exception.RateLimitExceededException;
import org.example.exception.RateLimitRulesLoadingException;
import org.example.model.Notification;
import org.example.model.RateLimitRule;
import org.example.service.NotificationService;
import org.example.util.FileUtil;

import java.io.IOException;
import java.util.Map;

public class NotificationServiceImpl implements NotificationService {

    private final Gateway gateway;
    private final Map<String, RateLimitRule> rateLimitRules;

    public NotificationServiceImpl(Gateway gateway) {
        this.gateway = gateway;
        this.rateLimitRules = loadRateLimitRules();
    }

    @Override
    public void send(Notification notification) {
        RateLimitRule rule = rateLimitRules.get(notification.getType());
        if (rule == null) {
            throw new InvalidNotificationTypeException("Invalid notification type.");
        }
        if (rule.isRateLimited(notification.getUserId())) {
            throw new RateLimitExceededException("Rate limit exceeded. Notification rejected.");
        } else {
            gateway.send(notification);
            rule.incrementNotificationCount(notification.getUserId());
        }
    }

    private Map<String, RateLimitRule> loadRateLimitRules() {
        Map<String, RateLimitRule> rateLimitRuleMap;
        try {
            rateLimitRuleMap = FileUtil.readJsonFile(new TypeReference<>() {});
        } catch (IOException exception) {
            throw new RateLimitRulesLoadingException("Error loading rate limit rules from the external source.");
        }
        return rateLimitRuleMap;
    }
}

