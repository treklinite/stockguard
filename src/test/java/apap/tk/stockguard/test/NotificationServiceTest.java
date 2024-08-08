package apap.tk.stockguard.test;

import apap.tk.stockguard.dto.NotificationDTO;
import apap.tk.stockguard.model.NotificationModel;
import apap.tk.stockguard.model.UserModel;
import apap.tk.stockguard.model.BarangModel;
import apap.tk.stockguard.repository.BarangDb;
import apap.tk.stockguard.repository.NotificationDb;
import apap.tk.stockguard.repository.UserDb;
import apap.tk.stockguard.service.BarangService;
import apap.tk.stockguard.service.NotificationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private NotificationDb notificationDb;

    @Mock
    private UserDb userDb;

    @Mock
    private BarangDb barangDb;

    @Mock
    private BarangService barangService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNotification() {
        NotificationModel notification = new NotificationModel();
        when(notificationDb.save(notification)).thenReturn(notification);

        NotificationModel result = notificationService.addNotification(notification);
        assertEquals(notification, result);
        verify(notificationDb, times(1)).save(notification);
    }

    @Test
    void testGetAllNotifications() {
        List<NotificationModel> notifications = new ArrayList<>();
        when(notificationDb.findAll()).thenReturn(notifications);

        List<NotificationModel> result = notificationService.getAllNotifications();
        assertEquals(notifications, result);
        verify(notificationDb, times(1)).findAll();
    }

    @Test
    void testGetNotificationsForUser() {
        UserModel user = new UserModel();
        user.setId(1);
        List<NotificationModel> notifications = new ArrayList<>();
        user.setListNotification(notifications);
        when(userDb.findById(1)).thenReturn(Optional.of(user));

        List<NotificationModel> result = notificationService.getNotificationsForUser(1);
        assertEquals(notifications, result);
        verify(userDb, times(1)).findById(1);
    }

    @Test
    void testGetNotificationById() {
        NotificationModel notification = new NotificationModel();
        when(notificationDb.findById(1)).thenReturn(Optional.of(notification));

        NotificationModel result = notificationService.getNotificationById(1);
        assertEquals(notification, result);
        verify(notificationDb, times(1)).findById(1);
    }

    @Test
    void testUpdateNotification() {
        NotificationModel notification = new NotificationModel();
        when(notificationDb.save(notification)).thenReturn(notification);

        NotificationModel result = notificationService.updateNotification(notification);
        assertEquals(notification, result);
        verify(notificationDb, times(1)).save(notification);
    }

    @Test
    void testGenerateNotifications() {
        BarangModel barang = new BarangModel();
        barang.setNama("Test Barang");
        barang.setTanggalKadaluarsa(LocalDateTime.now().plusDays(29));
        List<BarangModel> allBarang = List.of(barang);
        when(barangDb.findAll()).thenReturn(allBarang);

        NotificationModel notification = new NotificationModel();
        when(notificationService.addNotification(any(NotificationModel.class))).thenReturn(notification);

        notificationService.generateNotifications();

        verify(barangDb, times(1)).findAll();
        verify(notificationService, times(1)).addNotification(any(NotificationModel.class));
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/notifications"), any(NotificationDTO.class));
    }

    @Test
    void testGetUnreadNotifications() {
        List<NotificationModel> notifications = new ArrayList<>();
        when(notificationDb.findByIsReadFalse()).thenReturn(notifications);

        List<NotificationModel> result = notificationService.getUnreadNotifications();
        assertEquals(notifications, result);
        verify(notificationDb, times(1)).findByIsReadFalse();
    }

    @Test
    void testGetReadNotifications() {
        List<NotificationModel> notifications = new ArrayList<>();
        when(notificationDb.findByIsReadTrue()).thenReturn(notifications);

        List<NotificationModel> result = notificationService.getReadNotifications();
        assertEquals(notifications, result);
        verify(notificationDb, times(1)).findByIsReadTrue();
    }

    @Test
    void testMarkAllAsRead() {
        List<NotificationModel> unreadNotifications = new ArrayList<>();
        NotificationModel notification = new NotificationModel();
        unreadNotifications.add(notification);
        when(notificationDb.findByIsReadFalse()).thenReturn(unreadNotifications);

        notificationService.markAllAsRead();

        assertTrue(notification.getIsRead());
        verify(notificationDb, times(1)).findByIsReadFalse();
        verify(notificationDb, times(1)).save(notification);
    }

    @Test
    void testMarkAsRead() {
        NotificationModel notification = new NotificationModel();
        when(notificationDb.findById(1)).thenReturn(Optional.of(notification));

        notificationService.markAsRead(1);

        assertTrue(notification.getIsRead());
        verify(notificationDb, times(1)).findById(1);
        verify(notificationDb, times(1)).save(notification);
    }
}
