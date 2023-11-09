package org.example;

import org.example.model.Notification;

public class Gateway {

    public void send(Notification notification) {
        System.out.println("Sending " + notification.toString());
    }

}