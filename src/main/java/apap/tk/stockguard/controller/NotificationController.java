package apap.tk.stockguard.controller;

import apap.tk.stockguard.model.BarangModel;
import apap.tk.stockguard.model.NotificationModel;
import apap.tk.stockguard.service.BarangService;
import apap.tk.stockguard.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BarangService barangService;

    @GetMapping("")
    public String viewNotifications(Model model) {
        List<NotificationModel> unreadNotifications = notificationService.getUnreadNotifications();
        List<NotificationModel> readNotifications = notificationService.getReadNotifications();
        model.addAttribute("unreadNotifications", unreadNotifications);
        model.addAttribute("readNotifications", readNotifications);
        return "view-all-notifications";
    }

    @PostMapping("/mark-all-as-read")
    public String markAllAsRead() {
        notificationService.markAllAsRead();
        return "redirect:/notifications";
    }

    @PostMapping("/read/{id}")
    public String markAsRead(@PathVariable Integer id) {
        notificationService.markAsRead(id);
        return "redirect:/notifications";
    }
}
