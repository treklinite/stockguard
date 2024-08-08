package apap.tk.stockguard.controller;

import apap.tk.stockguard.model.NotificationModel;
import apap.tk.stockguard.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketNotificationController {

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/notify")
    @SendTo("/topic/notifications")
    public NotificationModel sendNotification(NotificationModel notification) {
        NotificationModel savedNotification = notificationService.addNotification(notification);
        return savedNotification;
    }
}
