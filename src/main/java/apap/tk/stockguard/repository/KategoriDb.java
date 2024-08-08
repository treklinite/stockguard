package apap.tk.stockguard.repository;

import apap.tk.stockguard.model.KategoriModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KategoriDb extends JpaRepository<KategoriModel, Integer> {
    Optional<KategoriModel> findById(Integer id);
    List<KategoriModel> findAll();
    KategoriModel findByNama(String nama);
}
