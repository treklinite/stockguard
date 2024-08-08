package apap.tk.stockguard.controller;

import apap.tk.stockguard.model.KategoriModel;
import apap.tk.stockguard.service.KategoriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import apap.tk.stockguard.model.BarangModel;
import apap.tk.stockguard.service.BarangService;
import apap.tk.stockguard.model.KategoriModel;
import apap.tk.stockguard.service.KategoriService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class BarangController {
    @Autowired
    private BarangService barangService;

    @Autowired
    private KategoriService kategoriService;

    @GetMapping("/barang")
    public String viewAllBarang(Model model){
        List<BarangModel> listBarang = barangService.getAllBarang();
        for (BarangModel barang : listBarang) {
            System.out.println("Barang: " + barang.getNama());
            System.out.println("Kategori: " + barang.getKategori().getNama());
        }
        model.addAttribute("listBarang",listBarang);
        return  "view-all-barang";
    }

    @GetMapping("/barang/add")
    public String addBarangForm(Model model){
        List<KategoriModel> listKategori = kategoriService.getKategoriList();
        model.addAttribute("listKategori", listKategori);
        model.addAttribute("barang", new BarangModel());
        return "form-add-barang";

    }

    @PostMapping("/barang/add")
    public String addBarangSubmit(@ModelAttribute BarangModel barang, Model model){
        barangService.createBarang(barang);
        String namaBarang = barang.getNama();
        model.addAttribute("nama",namaBarang);
        return "success-create-barang";

    }

    @GetMapping("barang/{id}/delete")
    public String deleteBarang(@PathVariable("id") Integer id, Model model){
        barangService.deleteBarangById(id);
        model.addAttribute("id",id);
        model.addAttribute("currentPage", "barang");
        return "sukses-hapus-barang";
    }

    @GetMapping("barang/{id}/update")
    public String formUpdateBarang(@PathVariable("id") Integer id, Model model){

        var barang = barangService.getBarangById(id);
        model.addAttribute("barang", barang);
        List<KategoriModel> listKategori = kategoriService.getKategoriList();
        model.addAttribute("listKategori", listKategori);
        return "form-update-barang";
    }

    @PostMapping("/barang/update")
    public String updateBarangSubmit(@ModelAttribute BarangModel barang, Model model){
        barangService.updateBarang(barang);
        String namaBarang = barang.getNama();
        model.addAttribute("nama",namaBarang);
        return "success-update-barang";
    }

    @GetMapping("/barang/search")
    public String filteredByNama(@RequestParam(value = "query") String nama, Model model){
        List<BarangModel> listBarangFiltered = barangService.listBarangFiltered(nama);
        model.addAttribute("listBarang", listBarangFiltered);
        return "view-all-barang-filtered";
    }

    @GetMapping("/barang/{id}/detail")
    public String viewBarangDetail(@PathVariable Integer id, Model model) {
        BarangModel barang = barangService.getBarangById(id);
        model.addAttribute("barang", barang);
        return "view-barang-detail";
    }

    @GetMapping("/barang/{id}/diskon")
    public String diskonBarang(@PathVariable Integer id, Model model){
        BarangModel barang = barangService.getBarangById(id);
        String message = "Barang " + barang.getNama() + " berhasil didiskon";
        model.addAttribute("message", message);
        model.addAttribute("pageTitle", "Daftar Barang");
        model.addAttribute("url", "/barang");
        return "success-page";
    }

    @GetMapping("/barang/{id}/retur")
    public String returBarang(@PathVariable Integer id, Model model){
        BarangModel barang = barangService.getBarangById(id);
        String message = "Barang " + barang.getNama() + " berhasil diretur";
        model.addAttribute("message", message);
        model.addAttribute("pageTitle", "Daftar Barang");
        model.addAttribute("url", "/barang");
        return "success-page";
    }

}
