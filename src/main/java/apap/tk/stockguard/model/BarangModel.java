package apap.tk.stockguard.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "barang")
public class BarangModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nama", nullable = false)
    private String nama;

    @NotNull
    @Column(name = "stok", nullable = false)
    private Integer stok;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "tanggalKadaluarsa", nullable = false)
    private LocalDateTime tanggalKadaluarsa;

    @OneToMany(mappedBy = "barang", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<NotificationModel> listNotification;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "kategori_id", referencedColumnName = "id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotFound(action = NotFoundAction.IGNORE)
    private KategoriModel kategori;

    @PrePersist
    @PreUpdate
    public void validateStok() {
        if (this.stok < 0) {
            throw new IllegalArgumentException("Stok tidak boleh negatif!");
        }
    }

    @Transient
    public String getStatus() {
        if (tanggalKadaluarsa.isBefore(LocalDateTime.now().plusDays(7))) {
            return "Segera Kadaluarsa";
        }
        return "Normal";
    }
}
