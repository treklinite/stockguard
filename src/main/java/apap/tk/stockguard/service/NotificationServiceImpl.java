package apap.tk.stockguard.service;

import apap.tk.stockguard.dto.NotificationDTO;
import apap.tk.stockguard.model.NotificationModel;
import apap.tk.stockguard.model.UserModel;
import apap.tk.stockguard.model.BarangModel;
import apap.tk.stockguard.repository.BarangDb;
import apap.tk.stockguard.repository.NotificationDb;
import apap.tk.stockguard.repository.UserDb;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationDb notificationDb;

    @Autowired
    private UserDb userDb;

    @Autowired
    private BarangDb barangDb;

    @Autowired
    private BarangService barangService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public NotificationModel addNotification(NotificationModel notification) {
        return notificationDb.save(notification);
    }

    @Override
    public List<NotificationModel> getAllNotifications() {
        return notificationDb.findAll();
    }

    @Override
    public List<NotificationModel> getNotificationsForUser(Integer userId) {
        UserModel user = userDb.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getListNotification();
    }

    @Override
    public NotificationModel getNotificationById(Integer id) {
        return notificationDb.findById(id).orElse(null);
    }

    @Override
    public NotificationModel updateNotification(NotificationModel notification) {
        return notificationDb.save(notification);
    }

    @Override
    @Transactional
    public void generateNotifications() {
        List<BarangModel> allBarang = barangDb.findAll();
        for (BarangModel barang : allBarang) {
            if (barang.getTanggalKadaluarsa().isBefore(LocalDateTime.now().plusDays(30))) {
                NotificationModel notification = new NotificationModel();
                notification.setMessage("Barang " + barang.getNama() + " will expire soon.");
                notification.setTanggalNotifikasi(LocalDateTime.now());
                notification.setBarang(barang);
                notification.setIsRead(false);

                NotificationModel savedNotification = notificationService.addNotification(notification);
                System.out.println("Created notification for barang: " + barang.getNama());

                // Create NotificationDTO
                NotificationDTO notificationDTO = new NotificationDTO();
                notificationDTO.setMessage(savedNotification.getMessage());
                notificationDTO.setTanggalNotifikasi(savedNotification.getTanggalNotifikasi());
                notificationDTO.setBarangNama(barang.getNama());

                // Send notification to WebSocket clients
                messagingTemplate.convertAndSend("/topic/notifications", notificationDTO);
                System.out.println("Send notification for barang: " + barang.getNama());
            }
        }
    }

    @Override
    public List<NotificationModel> getUnreadNotifications() {
        return notificationDb.findByIsReadFalse();
    }

    @Override
    public List<NotificationModel> getReadNotifications() {
        return notificationDb.findByIsReadTrue();
    }

    @Override
    public void markAllAsRead() {
        List<NotificationModel> unreadNotifications = notificationDb.findByIsReadFalse();
        for (NotificationModel notification : unreadNotifications) {
            notification.setIsRead(true);
            notificationDb.save(notification);
        }
    }

    @Override
    public void markAsRead(Integer id) {
        Optional<NotificationModel> notificationOpt = notificationDb.findById(id);
        if (notificationOpt.isPresent()) {
            NotificationModel notification = notificationOpt.get();
            notification.setIsRead(true);
            notificationDb.save(notification);
        }
    }
}
