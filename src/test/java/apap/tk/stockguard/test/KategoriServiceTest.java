package apap.tk.stockguard.test;

import apap.tk.stockguard.model.KategoriModel;
import apap.tk.stockguard.repository.KategoriDb;
import apap.tk.stockguard.service.KategoriServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KategoriServiceTest {

    @Mock
    private KategoriDb kategoriDb;

    @InjectMocks
    private KategoriServiceImpl kategoriService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetKategoriList() {
        KategoriModel kategori1 = new KategoriModel();
        KategoriModel kategori2 = new KategoriModel();

        when(kategoriDb.findAll()).thenReturn(Arrays.asList(kategori1, kategori2));

        List<KategoriModel> kategoriList = kategoriService.getKategoriList();

        assertNotNull(kategoriList);
        assertEquals(2, kategoriList.size());
        verify(kategoriDb, times(1)).findAll();
    }

    @Test
    void testGetKategoriById() {
        KategoriModel kategori = new KategoriModel();
        kategori.setId(1);

        when(kategoriDb.findById(1)).thenReturn(Optional.of(kategori));

        KategoriModel foundKategori = kategoriService.getKategoriById(1);

        assertNotNull(foundKategori);
        assertEquals(1, foundKategori.getId());
        verify(kategoriDb, times(1)).findById(1);
    }

    @Test
    void testGetKategoriByIdNotFound() {
        when(kategoriDb.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            kategoriService.getKategoriById(1);
        });

        verify(kategoriDb, times(1)).findById(1);
    }

    @Test
    void testAddKategori() {
        KategoriModel kategori = new KategoriModel();
        kategori.setNama("Kategori 1");

        when(kategoriDb.findByNama("Kategori 1")).thenReturn(null);
        when(kategoriDb.save(kategori)).thenReturn(kategori);

        kategoriService.addKategori(kategori);

        verify(kategoriDb, times(1)).findByNama("Kategori 1");
        verify(kategoriDb, times(1)).save(kategori);
    }

    @Test
    void testAddKategoriAlreadyExists() {
        KategoriModel kategori = new KategoriModel();
        kategori.setNama("Kategori 1");

        when(kategoriDb.findByNama("Kategori 1")).thenReturn(kategori);

        assertThrows(IllegalArgumentException.class, () -> {
            kategoriService.addKategori(kategori);
        });

        verify(kategoriDb, times(1)).findByNama("Kategori 1");
        verify(kategoriDb, times(0)).save(kategori);
    }

    @Test
    void testUpdateKategori() {
        KategoriModel kategori = new KategoriModel();
        kategori.setId(1);
        kategori.setNama("Kategori Updated");

        when(kategoriDb.findByNama("Kategori Updated")).thenReturn(null);
        when(kategoriDb.save(kategori)).thenReturn(kategori);

        KategoriModel updatedKategori = kategoriService.updateKategori(kategori);

        assertNotNull(updatedKategori);
        assertEquals("Kategori Updated", updatedKategori.getNama());
        verify(kategoriDb, times(1)).findByNama("Kategori Updated");
        verify(kategoriDb, times(1)).save(kategori);
    }

    @Test
    void testUpdateKategoriAlreadyExists() {
        KategoriModel existingKategori = new KategoriModel();
        existingKategori.setId(2);
        existingKategori.setNama("Kategori Updated");

        KategoriModel kategori = new KategoriModel();
        kategori.setId(1);
        kategori.setNama("Kategori Updated");

        when(kategoriDb.findByNama("Kategori Updated")).thenReturn(existingKategori);

        assertThrows(IllegalArgumentException.class, () -> {
            kategoriService.updateKategori(kategori);
        });

        verify(kategoriDb, times(1)).findByNama("Kategori Updated");
        verify(kategoriDb, times(0)).save(kategori);
    }

    @Test
    void testDeleteKategori() {
        KategoriModel kategori = new KategoriModel();
        kategori.setId(1);

        doNothing().when(kategoriDb).delete(kategori);

        KategoriModel deletedKategori = kategoriService.deleteKategori(kategori);

        assertNotNull(deletedKategori);
        assertEquals(1, deletedKategori.getId());
        verify(kategoriDb, times(1)).delete(kategori);
    }

    @Test
    void testIsExist() {
        KategoriModel kategori = new KategoriModel();
        kategori.setNama("Kategori 1");

        when(kategoriDb.findByNama("Kategori 1")).thenReturn(kategori);

        boolean exists = kategoriService.isExist("Kategori 1");

        assertTrue(exists);
        verify(kategoriDb, times(1)).findByNama("Kategori 1");
    }

    @Test
    void testIsExistNotFound() {
        when(kategoriDb.findByNama("Kategori 1")).thenReturn(null);

        boolean exists = kategoriService.isExist("Kategori 1");

        assertFalse(exists);
        verify(kategoriDb, times(1)).findByNama("Kategori 1");
    }

    @Test
    void testIsExistExceptCurrent() {
        KategoriModel existingKategori = new KategoriModel();
        existingKategori.setId(2);
        existingKategori.setNama("Kategori 1");

        when(kategoriDb.findByNama("Kategori 1")).thenReturn(existingKategori);

        boolean exists = kategoriService.isExistExceptCurrent(1, "Kategori 1");

        assertTrue(exists);
        verify(kategoriDb, times(1)).findByNama("Kategori 1");
    }

    @Test
    void testIsExistExceptCurrentSameCategory() {
        KategoriModel kategori = new KategoriModel();
        kategori.setId(1);
        kategori.setNama("Kategori 1");

        when(kategoriDb.findByNama("Kategori 1")).thenReturn(kategori);

        boolean exists = kategoriService.isExistExceptCurrent(1, "Kategori 1");

        assertFalse(exists);
        verify(kategoriDb, times(1)).findByNama("Kategori 1");
    }

    @Test
    void testIsExistExceptCurrentNotFound() {
        when(kategoriDb.findByNama("Kategori 1")).thenReturn(null);

        boolean exists = kategoriService.isExistExceptCurrent(1, "Kategori 1");

        assertFalse(exists);
        verify(kategoriDb, times(1)).findByNama("Kategori 1");
    }
}
