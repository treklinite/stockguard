package apap.tk.stockguard.service;
import apap.tk.stockguard.model.BarangModel;
import java.util.List;
import java.util.Map;

public interface BarangService {
    BarangModel createBarang(BarangModel barang);
    BarangModel getBarangById(Integer id);
    BarangModel updateBarang(BarangModel barang);
    void deleteBarangById(Integer id);
    List<BarangModel> getAllBarang();
    List<BarangModel> listBarangFiltered(String nama);
    List<BarangModel> getBarangSegeraKadaluarsa();
    BarangModel saveBarang(BarangModel barang);
    List<BarangModel> getBarangKadaluarsa();
    Map<BarangModel, String> getRekomendasiTindakanMap(List<BarangModel> barangKadaluarsa);
}
