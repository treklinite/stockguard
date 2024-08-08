package apap.tk.stockguard.config;

import apap.tk.stockguard.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {
    @Autowired
    private NotificationService notificationService;

    @Scheduled(cron = "0 0 0 * * *") // Runs daily at midnight
//    @Scheduled(fixedRate = 120000)
    public void generateNotifications() {
        System.out.println("Running scheduled task: Generating notifications...");
        notificationService.generateNotifications();
    }
}
