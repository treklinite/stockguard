package apap.tk.stockguard.service;
import apap.tk.stockguard.model.BarangModel;
import apap.tk.stockguard.repository.BarangDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BarangServiceImpl implements BarangService {
    @Autowired
    private BarangDb barangDb;

    @Override
    public BarangModel createBarang(BarangModel barang){
        return barangDb.save(barang);
    }

    @Override
    public BarangModel getBarangById(Integer id) {
        for (BarangModel barang:getAllBarang()){
            if (barang.getId().equals(id)){
                return barang;
            }
        }
        return null;
    }

    @Override
    public BarangModel updateBarang(BarangModel barang){
        BarangModel barang2 = getBarangById(barang.getId());
        if (barang2 != null){
            barang2.setStok(barang.getStok());
            barang2.setNama(barang.getNama());
            barang2.setKategori(barang.getKategori());
            barang2.setTanggalKadaluarsa(barang.getTanggalKadaluarsa());
            barangDb.save(barang2);
        }
        return barang2;
    }

    @Override
    public void deleteBarangById(Integer id){
        barangDb.deleteById(id);
    }

    @Override
    public List<BarangModel> getAllBarang() {
        return barangDb.findAll();
    }

    @Override
    public List<BarangModel> listBarangFiltered(String nama) {
        return barangDb.findByNamaContainingIgnoreCase(nama);
    }

    @Override
    public List<BarangModel> getBarangSegeraKadaluarsa() {
        List<BarangModel> expiringItems = barangDb.findByTanggalKadaluarsaBefore(LocalDateTime.now().plusDays(30));
//        Logger logger = LoggerFactory.getLogger(BarangServiceImpl.class);
        System.out.println("Expiring items: " + expiringItems.size());
        for (BarangModel barang : expiringItems) {
            System.out.println("Expiring item: " + barang.getNama() + ", Expiry Date: " + barang.getTanggalKadaluarsa());
        }
        return expiringItems;
    }

    @Override
    public BarangModel saveBarang(BarangModel barang) {
        return barangDb.save(barang);
    }

    @Override
    public List<BarangModel> getBarangKadaluarsa(){
        return barangDb.findByTanggalKadaluarsaBefore(LocalDateTime.now());
    }

    @Override
    public Map<BarangModel, String> getRekomendasiTindakanMap(List<BarangModel> barangKadaluarsa) {
        return barangKadaluarsa.stream()
                .collect(Collectors.toMap(
                        barang -> barang,
                        barang -> {
                            if (barang.getKategori() != null) {
                                switch (barang.getKategori().getNama()) {
                                    case "Makanan":
                                        return "Segera diskon atau jual barang makanan kadaluarsa.";
                                    case "Obat":
                                        return "Hapus barang obat kadaluarsa dan lakukan evaluasi penyimpanan.";
                                    default:
                                        return "Periksa dan lakukan tindakan sesuai kebijakan.";
                                }
                            } else {
                                return "Periksa barang kadaluarsa dan tindak lanjuti sesuai kebijakan.";
                            }
                        }
                ));
    }
}
