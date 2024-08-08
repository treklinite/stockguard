package apap.tk.stockguard.repository;

import apap.tk.stockguard.model.BarangModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BarangDb extends JpaRepository<BarangModel, Integer> {
    Optional<BarangModel> findById(Integer id);
    List<BarangModel> findAll();
    List<BarangModel> findByNamaContainingIgnoreCase(String nama);
    List<BarangModel> findByTanggalKadaluarsaBefore(LocalDateTime date);
}
