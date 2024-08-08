package apap.tk.stockguard.service;

import apap.tk.stockguard.model.KategoriModel;
import java.util.List;

public interface KategoriService {
    List<KategoriModel> getKategoriList();
    KategoriModel getKategoriById(Integer id);
    void addKategori(KategoriModel kategori);
//    KategoriModel updateKategori(Integer id, KategoriModel kategori);
    KategoriModel updateKategori(KategoriModel kategori);
    KategoriModel deleteKategori(KategoriModel kategori);
    boolean isExist(String nama);
    boolean isExistExceptCurrent(Integer id, String nama);
}
