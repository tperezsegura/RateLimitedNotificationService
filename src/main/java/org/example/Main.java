package org.example;

import org.example.exception.NotificationException;
import org.example.model.Notification;
import org.example.service.NotificationService;
import org.example.service.impl.NotificationServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int successfulNotificationsCount = 0;
        int errorNotificationsCount = 0;
        NotificationService notificationService = new NotificationServiceImpl(new Gateway());
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("unknown", "user", "???"));
        notifications.add(new Notification("news", "user", "This is a new"));
        notifications.add(new Notification("status", "user", "This is a status update"));
        notifications.add(new Notification("marketing", "user", "This is a marketing message"));
        notifications.add(new Notification("marketing", "user", "This another marketing message"));
        notifications.add(new Notification("marketing", "user", "This another marketing message"));
        notifications.add(new Notification("marketing", "user", "This marketing message will be rejected"));
        for (Notification notification : notifications) {
            try {
                notificationService.send(notification);
                successfulNotificationsCount++;
            } catch (NotificationException exception) {
                System.out.println("Notification error: " + exception.getMessage());
                errorNotificationsCount++;
            }
        }
        System.out.println("Sent " + successfulNotificationsCount + " notifications.");
        System.out.println("Encountered " + errorNotificationsCount + " errors.");
    }
}