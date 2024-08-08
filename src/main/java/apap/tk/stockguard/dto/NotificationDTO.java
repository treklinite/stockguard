// NotificationDTO.java
package apap.tk.stockguard.dto;

import java.time.LocalDateTime;

public class NotificationDTO {
    private String message;
    private LocalDateTime tanggalNotifikasi;
    private String barangNama;

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTanggalNotifikasi() {
        return tanggalNotifikasi;
    }

    public void setTanggalNotifikasi(LocalDateTime tanggalNotifikasi) {
        this.tanggalNotifikasi = tanggalNotifikasi;
    }

    public String getBarangNama() {
        return barangNama;
    }

    public void setBarangNama(String barangNama) {
        this.barangNama = barangNama;
    }
}
