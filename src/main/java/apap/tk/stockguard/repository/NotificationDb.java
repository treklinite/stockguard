package apap.tk.stockguard.repository;

import apap.tk.stockguard.model.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationDb extends JpaRepository<NotificationModel, Integer> {
    Optional<NotificationModel> findById(Integer id);
    List<NotificationModel> findByIsReadFalse();
    List<NotificationModel> findByIsReadTrue();
    List<NotificationModel> findByTanggalNotifikasiBefore(LocalDateTime localDateTime);
}
