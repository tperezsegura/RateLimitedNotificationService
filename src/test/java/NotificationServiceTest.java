import org.example.Gateway;
import org.example.exception.InvalidNotificationTypeException;
import org.example.exception.RateLimitExceededException;
import org.example.model.Notification;
import org.example.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @Mock
    private Gateway gateway;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    public void setUp() {
        System.setProperty("env", "test");
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void sendNotificationForOneRecipient() {
        Notification newsNotification = new Notification("news", "user", "News Notification");

        assertDoesNotThrow(() -> notificationService.send(newsNotification));

        verify(gateway).send(eq(newsNotification));
    }

    @Test
    public void sendDifferentNotificationsForOneRecipient() {
        Notification newsNotification = new Notification("news", "user", "News Notification");
        Notification statusNotification = new Notification("status", "user", "Status Notification");
        Notification marketingNotification = new Notification("marketing", "user", "Marketing Notification");

        assertDoesNotThrow(() -> notificationService.send(newsNotification));
        assertDoesNotThrow(() -> notificationService.send(statusNotification));
        assertDoesNotThrow(() -> notificationService.send(marketingNotification));

        verify(gateway, times(3)).send(any());
    }

    @Test
    public void sendNotificationsForMultipleRecipients() {
        Notification user1Notification1 = new Notification("status", "user1", "User1 Notification 1");
        Notification user1Notification2 = new Notification("status", "user1", "User1 Notification 2");
        Notification user1ExceedingNotification = new Notification("status", "user1", "User1 Exceeding Notification");
        Notification user2Notification1 = new Notification("status", "user2", "User2 Notification 1");
        Notification user2Notification2 = new Notification("status", "user2", "User2 Notification 2");

        assertDoesNotThrow(() -> notificationService.send(user1Notification1));
        assertDoesNotThrow(() -> notificationService.send(user1Notification2));
        assertThrows(RateLimitExceededException.class, () -> notificationService.send(user1ExceedingNotification));
        assertDoesNotThrow(() -> notificationService.send(user2Notification1));
        assertDoesNotThrow(() -> notificationService.send(user2Notification2));
        verify(gateway, times(4)).send(any());
    }

    @Test
    public void sendNotificationAfterRateLimitReset() {
        Notification validNotification = new Notification("rateLimitReset", "user", "Notification 1");
        Notification anotherValidNotification = new Notification("rateLimitReset", "user", "Notification 2");
        Notification allowedNotificationAfterReset = new Notification("rateLimitReset", "user", "Notification 3");
        notificationService.send(validNotification);
        notificationService.send(anotherValidNotification);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(
                () -> assertDoesNotThrow(() -> notificationService.send(allowedNotificationAfterReset)));

        verify(gateway, times(3)).send(any());
    }

    @Test
    public void sendALargeNumberOfNotificationsWithoutExceedingLimit() {
        for (int i = 0; i < 1000; i++) {
            Notification highLimitNotification = new Notification("highLimit", "user", "High Limit Notification");
            assertDoesNotThrow(() -> notificationService.send(highLimitNotification));
        }
    }

    @Test
    public void sendNotificationThatExceedsRateLimitThrowsException() {
        Notification validNotification = new Notification("rateLimitExceeded", "user", "Notification 1");
        Notification anotherValidNotification = new Notification("rateLimitExceeded", "user", "Notification 2");
        Notification exceedingNotification = new Notification("rateLimitExceeded", "user", "Exceeding Notification");
        notificationService.send(validNotification);
        notificationService.send(anotherValidNotification);

        assertThrows(RateLimitExceededException.class, () -> notificationService.send(exceedingNotification));

        verify(gateway, times(2)).send(any());
    }

    @Test
    public void sendNotificationOfInvalidTypeThrowsException() {
        Notification invalidNotification = new Notification("unknownType", "user", "Invalid notification type");

        assertThrows(InvalidNotificationTypeException.class, () -> notificationService.send(invalidNotification));
    }

}