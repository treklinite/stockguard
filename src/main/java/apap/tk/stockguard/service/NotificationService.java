package apap.tk.stockguard.service;

import apap.tk.stockguard.model.NotificationModel;
import java.util.List;

public interface NotificationService {
    NotificationModel addNotification(NotificationModel notification);
    List<NotificationModel> getAllNotifications();
    List<NotificationModel> getNotificationsForUser(Integer userId);
    NotificationModel getNotificationById(Integer id);
    NotificationModel updateNotification(NotificationModel notification);
    void generateNotifications();
    List<NotificationModel> getUnreadNotifications();
    List<NotificationModel> getReadNotifications();
    void markAllAsRead();
    void markAsRead(Integer id);
}
