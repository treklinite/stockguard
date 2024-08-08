package apap.tk.stockguard.test;

import apap.tk.stockguard.model.BarangModel;
import apap.tk.stockguard.model.KategoriModel;
import apap.tk.stockguard.repository.BarangDb;
import apap.tk.stockguard.service.BarangServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BarangServiceTest {

    @Mock
    private BarangDb barangDb;

    @InjectMocks
    private BarangServiceImpl barangService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBarang() {
        BarangModel barang = new BarangModel();

        when(barangDb.save(barang)).thenReturn(barang);

        BarangModel createdBarang = barangService.createBarang(barang);

        assertNotNull(createdBarang);
        verify(barangDb, times(1)).save(barang);
    }

    @Test
    void testGetBarangById() {
        BarangModel barang = new BarangModel();
        barang.setId(1);

        when(barangDb.findAll()).thenReturn(Arrays.asList(barang));

        BarangModel foundBarang = barangService.getBarangById(1);

        assertNotNull(foundBarang);
        assertEquals(1, foundBarang.getId());
        verify(barangDb, times(1)).findAll();
    }

    @Test
    void testGetBarangByIdNotFound() {
        when(barangDb.findAll()).thenReturn(Arrays.asList());

        BarangModel foundBarang = barangService.getBarangById(1);

        assertNull(foundBarang);
        verify(barangDb, times(1)).findAll();
    }

    @Test
    void testUpdateBarang() {
        BarangModel barang = new BarangModel();
        barang.setId(1);
        barang.setNama("Barang Updated");

        when(barangDb.findAll()).thenReturn(Arrays.asList(barang));
        when(barangDb.save(barang)).thenReturn(barang);

        BarangModel updatedBarang = barangService.updateBarang(barang);

        assertNotNull(updatedBarang);
        assertEquals("Barang Updated", updatedBarang.getNama());
        verify(barangDb, times(1)).save(barang);
    }

    @Test
    void testUpdateBarangNotFound() {
        BarangModel barang = new BarangModel();
        barang.setId(1);

        when(barangDb.findAll()).thenReturn(Arrays.asList());

        BarangModel updatedBarang = barangService.updateBarang(barang);

        assertNull(updatedBarang);
        verify(barangDb, times(1)).findAll();
        verify(barangDb, times(0)).save(barang);
    }

    @Test
    void testDeleteBarangById() {
        doNothing().when(barangDb).deleteById(1);

        barangService.deleteBarangById(1);

        verify(barangDb, times(1)).deleteById(1);
    }

    @Test
    void testGetAllBarang() {
        BarangModel barang1 = new BarangModel();
        BarangModel barang2 = new BarangModel();

        when(barangDb.findAll()).thenReturn(Arrays.asList(barang1, barang2));

        List<BarangModel> barangList = barangService.getAllBarang();

        assertNotNull(barangList);
        assertEquals(2, barangList.size());
        verify(barangDb, times(1)).findAll();
    }

    @Test
    void testListBarangFiltered() {
        BarangModel barang1 = new BarangModel();
        barang1.setNama("Barang 1");

        BarangModel barang2 = new BarangModel();
        barang2.setNama("Barang 2");

        when(barangDb.findByNamaContainingIgnoreCase("Barang")).thenReturn(Arrays.asList(barang1, barang2));

        List<BarangModel> filteredBarangList = barangService.listBarangFiltered("Barang");

        assertNotNull(filteredBarangList);
        assertEquals(2, filteredBarangList.size());
        verify(barangDb, times(1)).findByNamaContainingIgnoreCase("Barang");
    }

    @Test
    void testGetBarangSegeraKadaluarsa() {
        BarangModel barang1 = new BarangModel();
        barang1.setNama("Barang 1");
        barang1.setTanggalKadaluarsa(LocalDateTime.now().plusDays(10));

        BarangModel barang2 = new BarangModel();
        barang2.setNama("Barang 2");
        barang2.setTanggalKadaluarsa(LocalDateTime.now().plusDays(20));

        LocalDateTime thirtyDaysFromNow = LocalDateTime.now().plusDays(30);

        when(barangDb.findByTanggalKadaluarsaBefore(thirtyDaysFromNow)).thenReturn(Arrays.asList(barang1, barang2));

        List<BarangModel> expiringItems = barangService.getBarangSegeraKadaluarsa();

        assertNotNull(expiringItems);
        assertEquals(2, expiringItems.size());
        verify(barangDb, times(1)).findByTanggalKadaluarsaBefore(thirtyDaysFromNow);
    }

    @Test
    void testSaveBarang() {
        BarangModel barang = new BarangModel();

        when(barangDb.save(barang)).thenReturn(barang);

        BarangModel savedBarang = barangService.saveBarang(barang);

        assertNotNull(savedBarang);
        verify(barangDb, times(1)).save(barang);
    }

    @Test
    void testGetBarangKadaluarsa() {
        BarangModel barang1 = new BarangModel();
        barang1.setNama("Barang 1");
        barang1.setTanggalKadaluarsa(LocalDateTime.now().minusDays(10));

        when(barangDb.findByTanggalKadaluarsaBefore(LocalDateTime.now())).thenReturn(Arrays.asList(barang1));

        List<BarangModel> kadaluarsaItems = barangService.getBarangKadaluarsa();

        assertNotNull(kadaluarsaItems);
        assertEquals(1, kadaluarsaItems.size());
        verify(barangDb, times(1)).findByTanggalKadaluarsaBefore(LocalDateTime.now());
    }

    @Test
    void testGetRekomendasiTindakanMap() {
        BarangModel barang1 = new BarangModel();
        barang1.setNama("Barang 1");
        KategoriModel kategoriMakanan = new KategoriModel();
        kategoriMakanan.setNama("Makanan");
        barang1.setKategori(kategoriMakanan);

        BarangModel barang2 = new BarangModel();
        barang2.setNama("Barang 2");
        KategoriModel kategoriObat = new KategoriModel();
        kategoriObat.setNama("Obat");
        barang2.setKategori(kategoriObat);

        List<BarangModel> barangKadaluarsa = Arrays.asList(barang1, barang2);

        Map<BarangModel, String> rekomendasiTindakanMap = barangService.getRekomendasiTindakanMap(barangKadaluarsa);

        assertNotNull(rekomendasiTindakanMap);
        assertEquals(2, rekomendasiTindakanMap.size());
        assertEquals("Segera diskon atau jual barang makanan kadaluarsa.", rekomendasiTindakanMap.get(barang1));
        assertEquals("Hapus barang obat kadaluarsa dan lakukan evaluasi penyimpanan.", rekomendasiTindakanMap.get(barang2));
    }
}