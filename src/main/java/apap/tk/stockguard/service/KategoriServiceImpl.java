package apap.tk.stockguard.service;

import apap.tk.stockguard.model.KategoriModel;
import apap.tk.stockguard.repository.KategoriDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class KategoriServiceImpl implements KategoriService {
    @Autowired
    private KategoriDb kategoriDb;

    @Override
    public List<KategoriModel> getKategoriList() {
        return kategoriDb.findAll();
    }

    @Override
    public KategoriModel getKategoriById(Integer id) {
        Optional<KategoriModel> kategori = kategoriDb.findById(id);
        if(kategori.isPresent()){
            return kategori.get();
        }else {
            throw new NoSuchElementException("Kategori dengan ID " + id + " tidak ditemukan.");
        }
    }

    @Override
    public void addKategori(KategoriModel kategori) {
        if (isExist(kategori.getNama())) {
            throw new IllegalArgumentException("Kategori dengan nama " + kategori.getNama() + " sudah ada.");
        }
        kategoriDb.save(kategori);
    }

    @Override
    public KategoriModel updateKategori(KategoriModel kategori) {
        if (isExistExceptCurrent(kategori.getId(), kategori.getNama())) {
            throw new IllegalArgumentException("Katgori dengan nama " + kategori.getNama() + " sudah ada.");
        }
        kategoriDb.save(kategori);
        return kategori;
    }

    @Override
    public KategoriModel deleteKategori(KategoriModel kategori) {
        kategoriDb.delete(kategori);
        return kategori;
    }

    @Override
    public boolean isExist(String nama) {
        return kategoriDb.findByNama(nama) != null;
    }

    @Override
    public boolean isExistExceptCurrent(Integer id, String nama) {
        KategoriModel existingCategory = kategoriDb.findByNama(nama);
        return existingCategory != null && !existingCategory.getId().equals(id);
    }
}
